import java.util.*;



public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        System.out.print("Enter Round Robin Time Quantum: ");
        int quantum = sc.nextInt();

        System.out.print("Enter Context Switching time: ");
        int contextSwitch = sc.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1) + " data:");

            System.out.print("Process Name: ");
            String name = sc.next();

            System.out.print("Arrival Time: ");
            int arrival = sc.nextInt();

            System.out.print("Burst Time: ");
            int burst = sc.nextInt();

            System.out.print("Priority: ");
            int priority = sc.nextInt();

            processes.add(new Process(name, arrival, burst, priority));
        }
        // شوف انت شغال علي ايه وفك كومنت بتاعه ورجع كومنت تاني لما ترفع علي git
        // ProcessRunner process=new ProcessRunner( new AGSecduler() );
        // ProcessRunner process=new ProcessRunner( new RoundRobin() );
        // ProcessRunner process=new ProcessRunner( new ShortestJob() );
        // ProcessRunner process=new ProcessRunner( new priortySchedule() );


    }
}
