import java.util.*;



public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System.out.print("Enter number of processes: ");
        // int n = sc.nextInt();


        List<Process> processes = new ArrayList<>();

        
        processes.add(new Process("P1", 0, 3, 2, 10));
        processes.add(new Process("P2", 2, 4, 3, 12));
        processes.add(new Process("P3", 5, 2, 1, 8));
        processes.add(new Process("P4", 8, 5, 4, 15));
        processes.add(new Process("P5", 12, 3, 5, 9));
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
