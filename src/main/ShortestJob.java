import java.util.*;

class ShortestJob implements CpuScheduling {
    private int contextSwitch = 0;

    public ShortestJob(int contextSwitch) {
        this.contextSwitch = contextSwitch;
    }

    public ShortestJob() {
        this.contextSwitch = 0;
    }

    @Override
    public void process(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        for (Process p : processes) {
            p.remaining = p.burst;
            p.waitingtime = 0;
            p.turnaroundtime = 0;
        }

        List<Process> list = new ArrayList<>(processes);
        list.sort(Comparator.comparingInt(p -> p.arrival));

        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).burst <= 0) {
                Process p = list.get(i);
                p.turnaroundtime = 0;
                p.waitingtime = 0;
                list.remove(i);
            }
        }

        if (list.isEmpty()) {
            System.out.println("\n=== Preemptive Shortest Job First (SJF) ===");
            System.out.println("All processes have zero burst time.");
            return;
        }

        PriorityQueue<Process> ready = new PriorityQueue<>(
            (p1, p2) -> {
                if (p1.remaining != p2.remaining) {
                    return p1.remaining - p2.remaining;
                }
                return p1.arrival - p2.arrival;
            }
        );

        List<String> order = new ArrayList<>();
        String lastExecuted = "";

        int time = 0;
        int i = 0;
        int finished = 0;
        int n = list.size();

        Process running = null;

        while (finished < n) {
            while (i < n && list.get(i).arrival <= time) {
                ready.add(list.get(i));
                i++;
            }

            if (running == null && ready.isEmpty()) {
                if (i < n) {
                    time = list.get(i).arrival;
                    continue;
                } else {
                    break;
                }
            }

            if (running == null && !ready.isEmpty()) {
                running = ready.poll();
                if (contextSwitch > 0 && !lastExecuted.isEmpty() && !lastExecuted.equals(running.name)) {
                    time += contextSwitch;
                    while (i < n && list.get(i).arrival <= time) {
                        ready.add(list.get(i));
                        i++;
                    }
                    if (!ready.isEmpty() && ready.peek().remaining < running.remaining) {
                        ready.add(running);
                        running = ready.poll();
                    }
                }
            }

            if (!running.name.equals(lastExecuted)) {
                order.add(running.name);
                lastExecuted = running.name;
            }

            running.remaining--;
            time++;

            while (i < n && list.get(i).arrival <= time) {
                ready.add(list.get(i));
                i++;
            }

            if (running.remaining == 0) {
                running.turnaroundtime = time - running.arrival;
                running.waitingtime = running.turnaroundtime - running.burst;
                finished++;
                running = null;
            } else if (!ready.isEmpty() && ready.peek().remaining < running.remaining) {
                ready.add(running);
                running = ready.poll();
                if (contextSwitch > 0) {
                    time += contextSwitch;
                    while (i < n && list.get(i).arrival <= time) {
                        ready.add(list.get(i));
                        i++;
                    }
                }
                if (!running.name.equals(lastExecuted)) {
                    order.add(running.name);
                    lastExecuted = running.name;
                }
            }
        }

        System.out.println("\n=== Preemptive Shortest Job First (SJF) ===");
        System.out.print("Execution Order: ");
        for (String s : order) {
            System.out.print(s + " ");
        }
        System.out.println();

        processes.sort(Comparator.comparing(p -> p.name));

        double totalWait = 0, totalTurn = 0;
        for (Process p : processes) {
            System.out.println(p.name + ": waiting = " + p.waitingtime + ", turnaround = " + p.turnaroundtime);
            totalWait += p.waitingtime;
            totalTurn += p.turnaroundtime;
        }

        System.out.printf("Average waiting time = %.2f%n", totalWait / processes.size());
        System.out.printf("Average turnaround time = %.2f%n", totalTurn / processes.size());
    }
}
