class Process {
    String name;
    int arrival;
    int burst;
    int priority;
    int quantum;

    public Process(String name, int arrival, int burst, int priority,int quantum) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
    }
}