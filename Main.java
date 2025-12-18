import java.util.*;



public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System.out.print("Enter number of processes: ");
        // int n = sc.nextInt();


        List<Process> processes = new ArrayList<>();

        
        //processes.add(new Process("P1", 0, 10, 4, 12));  // arrival 0, burst 10, priority 4, quantum 12
        //processes.add(new Process("P2", 1, 4, 2, 8));    // arrival 1, burst 4, priority 2, quantum 8
        //processes.add(new Process("P3", 2, 5, 5, 10));   // arrival 2, burst 5, priority 5, quantum 10
        //processes.add(new Process("P4", 3, 2, 1, 6));    // arrival 3, burst 2, priority 1, quantum 6
        //processes.add(new Process("P5", 4, 8, 6, 15));   // arrival 4, burst 8, priority 6, quantum 15
        //processes.add(new Process("P6", 5, 3, 3, 9));

        processes.add(new Process("P1", 0, 10, 3, 4));  // arrival 0, burst 10, priority 3, quantum 4
        processes.add(new Process("P2", 0, 8, 1, 5));   // arrival 0, burst 8, priority 1, quantum 5
        processes.add(new Process("P3", 0, 12, 2, 6));  // arrival 0, burst 12, priority 2, quantum 6
        processes.add(new Process("P4", 0, 6, 4, 3));   // arrival 0, burst 6, priority 4, quantum 3
        processes.add(new Process("P5", 0, 9, 5, 4));


        //processes.add(new Process("P1", 0, 3, 2, 10));
        //processes.add(new Process("P2", 2, 4, 3, 12));
        //processes.add(new Process("P3", 5, 2, 1, 8));
       //processes.add(new Process("P4", 8, 5, 4, 15));
       //processes.add(new Process("P5", 12, 3, 5, 9));
            //processes.add(new Process("p5", 0, 9, 5,4));



        
        // شوف انت شغال علي ايه وفك كومنت بتاعه ورجع كومنت تاني لما ترفع علي git
        // ProcessRunner process=new ProcessRunner( new AGScheduler() );
        AGScheduler agScheduler = new AGScheduler(processes) ;
        //ProcessRunner process=new ProcessRunner( new RoundRobin(2,1) );
        // ProcessRunner process=new ProcessRunner( new ShortestJob() );
        // ProcessRunner process=new ProcessRunner( new priortySchedule() );
        agScheduler.process();


    }
}
