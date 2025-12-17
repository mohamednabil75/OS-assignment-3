import java.util.*;

class AGScheduler implements CpuScheduling {
    public void process(List<Process> processes) {

        Deque<Process> readyProcesses = new LinkedList<>();
        List<String> orderExecution = new ArrayList<>();
        List<Integer> time = new ArrayList<>();
        Process[] prevProcess = new Process[0];
        int currentTime = 0;
        int completed = 0;
        readyProcesses = updateReadyQueue(currentTime, readyProcesses,processes,1) ;
        while (completed < processes.size()) {
            System.out.println(completed);
            readyProcesses = updateReadyQueue(currentTime, readyProcesses, processes,1) ;

            System.out.print("Ready queue at time " + currentTime + ": ");
            for (Process p : readyProcesses) {
                System.out.print(p.name + "(" + p.currentPhase + ") ");
            }
            System.out.println();
            if (readyProcesses.isEmpty()) {
                currentTime++;
                continue;
            }
            Process currentProcess = readyProcesses.poll();
            
            if (currentProcess == null) {
                currentTime++;
                continue;
            }            
            System.out.println("At Time " + currentTime + " the current process = " + currentProcess.name);
            int quantumToUse = 0;//is the percentage of quantum
            int usedQuantum = 0; // compare it the the remaining time and that is the actual time that affect remaining time 

            if (currentProcess.currentPhase == 1) {
                // Phase 1: FCFS 
                quantumToUse = (int) Math.ceil(currentProcess.quantum * 0.25);
                usedQuantum = Math.min(quantumToUse, currentProcess.remaining);
                if (usedQuantum == 0) {
                    if (!currentProcess.completed) {
                        currentProcess.completed = true;
                        completed++;
                        currentProcess.turnaroundtime = currentTime - currentProcess.arrival;
                    }
                    readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,1);
                    continue;
                }
                currentTime += usedQuantum;
                currentProcess.remaining -= usedQuantum;
                currentProcess.usedQuantum += usedQuantum;
                if(currentProcess.remaining <= 0){
                    currentProcess.remaining = 0 ;
                    if(!currentProcess.completed){
                        currentProcess.completed = true ;
                        completed++ ;
                    }
                    currentProcess.turnaroundtime = currentTime - currentProcess.arrival ;
                    readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,1) ;
                }
                if (!currentProcess.completed) {
                    currentProcess.currentPhase++;
                    if (currentProcess.currentPhase > 3)
                        currentProcess.currentPhase = 1;
                }
                checkQuantumExhusted(currentProcess);
                readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,1) ;
                System.out.println("Phase 1: " + currentProcess.name + " executed for " + usedQuantum + " units"); 
            
            } 
            if (currentProcess.currentPhase == 2) {
                // Phase 2: Priority Scheduling 
                prevProcess = readyProcesses.toArray(new Process[0]);
                Process HighestPriorityProcess = getHighPriority(prevProcess);

                if (HighestPriorityProcess != null && HighestPriorityProcess != currentProcess) {
                    System.out.println("Phase 2 Preemption : " + currentProcess.name + " priority = "+ currentProcess.priority + " and priority of " + HighestPriorityProcess.name + " = "+HighestPriorityProcess.priority );
                    System.out.println("Phase 2 Preemption: " + currentProcess.name + " preempted by " + HighestPriorityProcess.name);
                    currentProcess.quantum += (int) Math.ceil((currentProcess.quantum - currentProcess.usedQuantum) / 2);
                    currentProcess.isPermitted = true ;
                    readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,2) ;
                    //continue ;
                }
                quantumToUse = (int) Math.ceil(currentProcess.quantum * 0.25);
                usedQuantum = Math.min(quantumToUse, currentProcess.remaining);
                if (usedQuantum == 0) {
                    if (!currentProcess.completed) {
                        currentProcess.completed = true;
                        completed++;
                        currentProcess.turnaroundtime = currentTime - currentProcess.arrival;
                    }
                    readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,1);
                    continue;
                }
                currentTime += usedQuantum;
                currentProcess.remaining -= usedQuantum;
                if(currentProcess.remaining <= 0){
                    currentProcess.remaining = 0 ;
                    currentProcess.completed = true ;
                    completed++ ;
                    currentProcess.turnaroundtime = currentTime - currentProcess.arrival ;
                    currentProcess.waitingtime = currentProcess.turnaroundtime - currentProcess.burst ;
                    readyProcesses = updateReadyQueue(currentTime, readyProcesses,processes,1) ;
                } 
                if (!currentProcess.completed) {
                    currentProcess.currentPhase++;
                    if (currentProcess.currentPhase > 3)
                        currentProcess.currentPhase = 1;
                }
                currentProcess.usedQuantum = usedQuantum;
                checkQuantumExhusted(currentProcess);
                readyProcesses = updateReadyQueue(currentTime,readyProcesses, processes,1) ;

                System.out.println("Phase 2: " + currentProcess.name + " executed for " + usedQuantum + " units");

            } 
            if(currentProcess.currentPhase == 3) {
                // Phase 3: SJF 
                prevProcess = readyProcesses.toArray(new Process[0]);
                Process ShortestJob = getShortestjob(prevProcess);

                if (ShortestJob != null && ShortestJob != currentProcess) {
                    // Preemption case
                    System.out.println("Phase 3 Preemption : " + currentProcess.name + " remain job = "+ currentProcess.remaining + " and reamin job of " + ShortestJob.name + " = "+ShortestJob.remaining );
                    System.out.println("Phase 3 Preemption: " + currentProcess.name + " preempted by " + ShortestJob.name);
                    currentProcess.quantum += (currentProcess.quantum - currentProcess.usedQuantum);
                    currentProcess.isPermitted = true ;
                    readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,3) ;
                    //continue ;
                }
                quantumToUse = (int) Math.ceil(currentProcess.quantum * 0.5);
                usedQuantum = Math.min(quantumToUse, currentProcess.remaining);
                if (usedQuantum == 0) {
                    if (!currentProcess.completed) {
                        currentProcess.completed = true;
                        completed++;
                        currentProcess.turnaroundtime = currentTime - currentProcess.arrival;
                    }
                    currentProcess.waitingtime = currentProcess.turnaroundtime - currentProcess.burst ;
                    readyProcesses = updateReadyQueue(currentTime, readyProcesses,processes,1);
                    continue;
                }
                currentTime += usedQuantum;
                currentProcess.remaining -= usedQuantum;
                if(currentProcess.remaining <= 0){
                    currentProcess.remaining = 0 ; 
                    currentProcess.turnaroundtime = currentTime - currentProcess.arrival ;
                    
                    if(!currentProcess.completed){
                        currentProcess.completed = true ;
                        completed++ ;
                    }
                    readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,1) ;
                }
                if (!currentProcess.completed) {
                    currentProcess.currentPhase++;
                    if (currentProcess.currentPhase > 3)
                        currentProcess.currentPhase = 1;
                }
                currentProcess.usedQuantum = usedQuantum;
                readyProcesses = updateReadyQueue(currentTime,readyProcesses ,processes,1) ;
                checkQuantumExhusted(currentProcess);

                System.out.println("Phase 3: " + currentProcess.name + " executed for " + usedQuantum + " units");
            }

            orderExecution.add(currentProcess.name);
            time.add(currentTime - usedQuantum);

            System.out.println("Current time: " + currentTime + ", Completed: " + completed + "/" + processes.size());
            System.out.println();
        }
        System.out.println("Final result ");
        int totalTurnAroundTime = 0 ;
        int totalWaitingTime = 0 ;
        for(Process p :processes){
            System.out.println(p.name+" turnaround time = "+p.turnaroundtime + " and waiting time = " + p.waitingtime);
            totalTurnAroundTime+=p.turnaroundtime ;
            totalWaitingTime+= p.waitingtime ;
        }
        System.out.println("Average turnaround time = " + totalTurnAroundTime/processes.size() +"\n Average waiting time = "+totalWaitingTime/processes.size());


    }

    public Process getHighPriority(Process[] ready) {
        if (ready == null || ready.length == 0) {
            return null;
        }
        int high = 0;
        for (int i = 1; i < ready.length; i++) {
            if (ready[i].priority < ready[high].priority) {
                high = i;
            }
        }
        return ready[high];
    }

    public Process getShortestjob(Process[] ready) {
        if (ready == null || ready.length == 0) {
            return null;
        }
        int s = 0;
        for (int i = 1; i < ready.length; i++) {
            if (ready[i].remaining < ready[s].remaining) {
                s = i;
            }
        }
        return ready[s];
    }
    public void checkQuantumExhusted(Process currentProcess){
        if(currentProcess.usedQuantum >= currentProcess.quantum && currentProcess.remaining > 0){
            currentProcess.usedQuantum = 0 ;
                    currentProcess.quantum+=2 ;
                    currentProcess.isExhausted = true ;
        }
        
    }
    public Deque<Process> updateReadyQueue(int currentTime,Deque<Process> readyProcesses ,List<Process> processes, int phase) {
        int oldReadySz = readyProcesses.size() ;
        Process o = readyProcesses.peek() ;//p2p1
        if(o != null && !o.completed &&(o.isPermitted || o.isExhausted)){
            readyProcesses.remove(o) ;
            readyProcesses.add(o) ;
            o.isExhausted = false ;
            o.isPermitted = false ;
        }
        if(oldReadySz > 0){
            if(phase != 1){
                Process[] prevProcess = new Process[0];
                prevProcess = readyProcesses.toArray(new Process[0]) ;
                Process target ;
                if(phase == 2){
                    target = getHighPriority(prevProcess) ;
                }
                else {
                    target = getShortestjob(prevProcess) ;
                }
                readyProcesses.remove(target) ;
                readyProcesses.addFirst(target);
        }
            for (Process p : processes) {
                if (p != null  &&!readyProcesses.contains(p) &&!p.completed&&p.arrival <= currentTime  ) {
                    readyProcesses.add(p);
                    //System.out.print(p.name + " ");
                }
            }
        }
        else {
            for (Process p : processes) {
                if (p != null &&p.arrival <= currentTime && !p.completed) {
                    readyProcesses.add(p);
                    
                    //System.out.print(p.name + " ");
                }
            }
        }
        
        return readyProcesses;
    }
}
