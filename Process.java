class Process {
    String name;
    int arrival;
    int burst;
    int priority;

    public Process(String name, int arrival, int burst, int priority) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
    }
}