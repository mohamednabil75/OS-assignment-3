import java.util.*;

class Result {
    List<String> executionOrder;
    List<processResult2> processResults;
    double averageWaitingTime;
    double averageTurnaroundTime;
}

class processResult2 {
    String name;
    int waitingTime;
    int turnaroundTime;

    processResult2(Process p) {
        this.name = p.name;
        this.waitingTime = p.waitingtime;
        this.turnaroundTime = p.turnaroundtime;
    }
}

class ShortestJob implements CpuScheduling {
    private int contextSwitch = 0;
    List<Process> processes ;
    List<String> order ; 

    public ShortestJob(int contextSwitch ,List<Process> processes) {
        this.contextSwitch = contextSwitch;
        this.processes = processes ;
        order = new ArrayList<>();
    }

    public ShortestJob() {
        this.contextSwitch = 0;
    }

    @Override
    public void process() {
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
    public Result getResults() {
    Result result = new Result();

    if (order == null || order.isEmpty()) {
        result.executionOrder = new ArrayList<>();
        result.processResults = new ArrayList<>();
        result.averageWaitingTime = 0.0;
        result.averageTurnaroundTime = 0.0;
        return result;
    }

    List<String> exec = new ArrayList<>();
    exec.add(order.get(0));
    for (int i = 1; i < order.size(); i++) {
        if (!order.get(i).equals(order.get(i - 1))) {
            exec.add(order.get(i));
        }
    }
    result.executionOrder = exec;

    if (processes != null && !processes.isEmpty()) {
        processes.sort((a, b) -> a.name.compareTo(b.name));
        List<processResult2> pr = new ArrayList<>();

        int totalWaiting = 0, totalTurnaround = 0;

        for (Process p : processes) {
            pr.add(new processResult2(p));
            totalWaiting += p.waitingtime;
            totalTurnaround += p.turnaroundtime;
        }

        result.processResults = pr;
        result.averageWaitingTime = (double) totalWaiting / processes.size();
        result.averageTurnaroundTime = (double) totalTurnaround / processes.size();
    } else {
        result.processResults = new ArrayList<>();
        result.averageWaitingTime = 0.0;
        result.averageTurnaroundTime = 0.0;
    }

    return result;
}    

}


