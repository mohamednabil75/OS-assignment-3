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
    boolean isPermitted = false ;
    boolean isExhausted = false ;

    public Process(String name, int arrival, int burst, int priority , int quantum) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.quantum=quantum;
        remaining=burst;
    }
    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }
    public void setusedQuantum(int q){
        this.usedQuantum = q ;
    }
}