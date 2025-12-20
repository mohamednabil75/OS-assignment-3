import java.util.*;

class Process {
    String name;
    int arrival;
    int burst;
    int priority;
    int quantum;
    int waitingtime=0;
    int turnaroundtime=0;
    int remaining;
    int currentPhase = 1 ;
    int usedQuantum = 0 ;
    boolean completed = false ;
    int finishTime ;
    int phase1End;
    int phase2End;
    List<Integer> quantumHistory ;


    public Process(String name, int arrival, int burst, int priority , int quantum) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.quantum=quantum;
        remaining=burst;
        calculatePhaseBoundaries();
        this.quantumHistory = new ArrayList<>();
        this.quantumHistory.add(quantum) ;
    }
    public void calculatePhaseBoundaries() {
        phase1End = (int) Math.ceil(0.25 * quantum);
        phase2End = phase1End + (int) Math.ceil(0.25 * quantum);
    }
    
    public int getCurrentPhase(int timeTaken) {
        phase1End = (int) Math.ceil(0.25 * quantum);
        phase2End = phase1End + (int) Math.ceil(0.25 * quantum);
        if (timeTaken < phase1End) {
            return 1;
        } else if (timeTaken < phase2End) {
            return 2;
        } else {
            return 3;
        }
    }
    public void displayQuantumHistory(){
        for(int i : quantumHistory){
            System.out.print(i + " ");
        }
        System.out.println();
    }
}