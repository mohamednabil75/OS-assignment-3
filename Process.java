class Process {
    String name;
    int arrival;
    int burst;
    int priority;
    int quantum;
    int waitingtime=0;
    int turnaroundtime=0;
    int remaining;

    public Process(String name, int arrival, int burst, int priority,int quantum) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.quantum=quantum;
        remaining=burst;
    }
}