import java.util.*;
class AGResult {
    List<String> executionOrder;
    List<ProcessResult> processResults;
    double averageWaitingTime;
    double averageTurnaroundTime;
}

class ProcessResult {
    String name;
    int waitingTime;
    int turnaroundTime;
    List<Integer> quantumHistory;

    ProcessResult(Process p) {
        this.name = p.name;
        this.waitingTime = p.waitingtime;
        this.turnaroundTime = p.turnaroundtime;
        this.quantumHistory = new ArrayList<>(p.quantumHistory);
    }
}

class AGScheduler {
    public List<Process> processes;
    public LinkedList<Process> readyProcesses;
    public List<Process> completedProcesses;
    public List<String> orderExecution; 
    public List<Integer> orderTime;
    public Process currentProcess;
    public int currentTime;
    public int timeTaken;
    public int completed;
    
    public AGScheduler(List<Process> processes){
        this.processes = new ArrayList<>(processes);
        currentTime = 0;
        timeTaken = 0;
        completed = 0;
        currentProcess = null;
        readyProcesses = new LinkedList<>();
        orderExecution = new ArrayList<>();
        completedProcesses = new ArrayList<>();
        orderTime = new ArrayList<>();
    }
    
    public void process() {
        
        while (!processes.isEmpty() || !readyProcesses.isEmpty() || currentProcess != null) {
            updateReadyQueue();
            
            if(currentProcess == null && !readyProcesses.isEmpty()){
                currentProcess = getNextProcess();
            }
            
            if (currentProcess == null) {
                currentTime++;
                continue;
            }
            executeCurrentProcess();
            currentTime++;
            PreemptionCase();
        }
        
        printResults();
    }
    
    public void executeCurrentProcess() {

        if(currentProcess == null){
            return ;
        }
        currentProcess.currentPhase = currentProcess.getCurrentPhase(currentProcess.usedQuantum);
        currentProcess.remaining--;
        currentProcess.usedQuantum++;
        timeTaken++;
        // System.out.println("Now "+currentProcess.name + " executing remaining at phase "+currentProcess.currentPhase +"and remaining =" + currentProcess.remaining );
        // currentProcess.currentPhase = currentProcess.getCurrentPhase(currentProcess.usedQuantum);
        // System.out.println("now in phase "+currentProcess.currentPhase);
        
        orderExecution.add(currentProcess.name);
        orderTime.add(currentTime);
        
        if (currentProcess.remaining == 0) {
            completeCurrentProcess();
            return;
        }
        // System.out.println("Now the current quantum of  " + currentProcess.name + " = " + currentProcess.quantum );
        // System.out.println("Now the used quantum of  " + currentProcess.name + " = " + currentProcess.usedQuantum );

        if (currentProcess.usedQuantum >= currentProcess.quantum) {
            //System.out.println("Quantum exhausted occured");
            checkQuantumExhausted();
            return;
        }
    }
    
public void PreemptionCase(){
    updateReadyQueue();
    if(currentProcess == null) return;
    
    int currentPhase = currentProcess.getCurrentPhase(currentProcess.usedQuantum);
    currentProcess.currentPhase = currentPhase;
    
    if(currentPhase == 1) {
        return;
    }
    
    else if(currentPhase == 2){

        int phase1End = (int) Math.ceil(0.25 * currentProcess.quantum);
        if(currentProcess.usedQuantum == phase1End) {
            Process bestPriority = getHighPriority();
            if(bestPriority != null &&!readyProcesses.isEmpty() &&bestPriority.priority < currentProcess.priority){    
                //System.out.println("At enter of phase 2 " +currentProcess.name +" preempted by "+ bestPriority.name);            
                int remainingQuantum = currentProcess.quantum - currentProcess.usedQuantum;
                int quantumIncrease = (int)Math.ceil(remainingQuantum / 2.0);
                currentProcess.quantum += quantumIncrease;
                currentProcess.quantumHistory.add(currentProcess.quantum) ;
                currentProcess.currentPhase = 1;
                currentProcess.usedQuantum = 0 ;
                readyProcesses.remove(bestPriority);
                readyProcesses.addFirst(bestPriority);
                readyProcesses.add(currentProcess);
                timeTaken = 0 ;
                currentProcess = null;
            }
        }
    }
    
    else if(currentPhase == 3){
        //System.out.println("i reach here");
        Process bestSJ = getShortestjob();
        // if(bestSJ == null){
        //     System.out.println("null bestJ");
        //     return ;
        // }
        //System.out.println("best SJ = " +bestSJ.name);
        if(bestSJ != null && bestSJ.remaining < currentProcess.remaining){
            //System.out.println("At phase 3 "+currentProcess.name + " preempted by "+bestSJ.name);
            int remainingQuantum = currentProcess.quantum - currentProcess.usedQuantum;
            currentProcess.quantum += remainingQuantum;
            currentProcess.quantumHistory.add(currentProcess.quantum) ;
            currentProcess.currentPhase = 1 ;
            currentProcess.usedQuantum = 0 ;
            readyProcesses.remove(bestSJ);
            readyProcesses.addFirst(bestSJ);
            readyProcesses.add(currentProcess);
            timeTaken = 0 ;
            currentProcess = null;
        }
    }
}    public Process getHighPriority() {
        if(readyProcesses.isEmpty()) {
            return null;
        }
        
        Process best = readyProcesses.get(0);
        for(Process p : readyProcesses){
            if(p.priority < best.priority){
                best = p;
            }
        }
        return best;
    }
    
    public Process getShortestjob() {
        if(readyProcesses.isEmpty()) {
            return null;
        }
        
        Process best = readyProcesses.get(0);
        for(Process p : readyProcesses){
            if(p.remaining < best.remaining){
                best = p;
            }
        }
        return best;
    }
    
    public Process getNextProcess(){
        if(readyProcesses.isEmpty()) {
            return null;
        }
        
        Process p = readyProcesses.remove(0);
        timeTaken = 0;
        p.currentPhase = 1 ;
        return p;
    }
    
    public void completeCurrentProcess(){
        currentProcess.completed = true;
        currentProcess.finishTime = currentTime + 1;
        currentProcess.quantum = 0;
        currentProcess.quantumHistory.add(currentProcess.quantum) ;

        currentProcess.turnaroundtime = currentProcess.finishTime - currentProcess.arrival;
        currentProcess.waitingtime = currentProcess.turnaroundtime - currentProcess.burst;
        
        //System.out.println("Time " + (currentTime + 1) + ": " + currentProcess.name + " COMPLETED");
        
        completedProcesses.add(currentProcess);
        if(!readyProcesses.isEmpty()){
            readyProcesses.get(0).currentPhase = 1 ;
        } 
        currentProcess = null;
    }
    
    public void checkQuantumExhausted(){
        if(currentProcess != null && 
           currentProcess.usedQuantum >= currentProcess.quantum && 
           currentProcess.remaining > 0){
            //System.out.println(currentProcess.name + " exhausted");
            currentProcess.quantum += 2;
            currentProcess.currentPhase = 1 ;
            currentProcess.usedQuantum = 0 ;
            timeTaken = 0 ;
            currentProcess.quantumHistory.add(currentProcess.quantum) ;
            readyProcesses.add(currentProcess);  
            currentProcess = null;
        }
    }
    
    public void updateReadyQueue() {
        Iterator<Process> iterator = processes.iterator();
        while (iterator.hasNext()) {
            Process p = iterator.next();
            if (p.arrival <= currentTime) {
                readyProcesses.add(p);
                //System.out.println("Time " + currentTime + ": " + p.name + " arrived");
                iterator.remove();
            }
        }
        // System.out.println("At time "+currentTime);
        // for(Process p :readyProcesses){
        //     System.out.print(p.name +" ( " + p.currentPhase +" )  ");
        // }
        // System.out.println();
    }
public AGResult getResults() {
    AGResult result = new AGResult();

    if (orderExecution == null || orderExecution.isEmpty()) {
        result.executionOrder = new ArrayList<>();
        result.processResults = new ArrayList<>();
        result.averageWaitingTime = 0.0;
        result.averageTurnaroundTime = 0.0;
        return result;
    }

    List<String> exec = new ArrayList<>();
    exec.add(orderExecution.get(0));
    for (int i = 1; i < orderExecution.size(); i++) {
        if (!orderExecution.get(i).equals(orderExecution.get(i - 1))) {
            exec.add(orderExecution.get(i));
        }
    }
    result.executionOrder = exec;

    if (completedProcesses != null && !completedProcesses.isEmpty()) {
        completedProcesses.sort((a, b) -> a.name.compareTo(b.name));
        List<ProcessResult> pr = new ArrayList<>();

        int totalWaiting = 0, totalTurnaround = 0;

        for (Process p : completedProcesses) {
            pr.add(new ProcessResult(p));
            totalWaiting += p.waitingtime;
            totalTurnaround += p.turnaroundtime;
        }

        result.processResults = pr;
        result.averageWaitingTime = (double) totalWaiting / completedProcesses.size();
        result.averageTurnaroundTime = (double) totalTurnaround / completedProcesses.size();
    } else {
        result.processResults = new ArrayList<>();
        result.averageWaitingTime = 0.0;
        result.averageTurnaroundTime = 0.0;
    }

    return result;
}    
    public void printResults() {

        if(orderExecution.isEmpty()){
            return ;
        }
        completedProcesses.sort((p1, p2) -> p1.name.compareTo(p2.name));
        System.out.println("\nExecution Order:");
        System.out.print(orderExecution.get(0) + " ");
        for(int i = 1; i < orderExecution.size(); i++){
            if(orderExecution.get(i) == orderExecution.get(i-1)){
                continue ;
            }
            System.out.print(orderExecution.get(i) + " ");
        }
        System.out.println();
                
        int totalTurnaround = 0;
        int totalWaiting = 0;
        
        for(Process p : completedProcesses){
            System.out.print(p.name + " waiting time = " + p.waitingtime +  " and turnaround time = " + p.turnaroundtime + " Quantum history -> " );
            p.displayQuantumHistory();
            totalTurnaround += p.turnaroundtime;
            totalWaiting += p.waitingtime;
        }
        System.out.println();
        
        if(!completedProcesses.isEmpty()){
            System.out.println("Average waiting time = " + 
            (double)totalWaiting/completedProcesses.size());
            System.out.println("Average turnaround time = " + 
            (double)totalTurnaround/completedProcesses.size());
        }
    }
}