import java.util.*;



public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System.out.print("Enter number of processes: ");
        // int n = sc.nextInt();


        List<Process> processes = new ArrayList<>();

        
            processes.add(new Process("p1", 0, 17, 4,7));//  arrival burst priorty timequantum
            processes.add(new Process("p2", 2, 6, 7,9));
            processes.add(new Process("p3", 5, 11, 3,4));
            processes.add(new Process("p4", 15, 4, 6,6));
            //processes.add(new Process("p5", 0, 9, 5,4));



        
        // شوف انت شغال علي ايه وفك كومنت بتاعه ورجع كومنت تاني لما ترفع علي git
        ProcessRunner process=new ProcessRunner( new AGScheduler() );
        //ProcessRunner process=new ProcessRunner( new RoundRobin(2,1) );
        // ProcessRunner process=new ProcessRunner( new ShortestJob() );
        // ProcessRunner process=new ProcessRunner( new priortySchedule() );
        process.processing(processes);


    }
}
