import java.util.*;



public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System.out.print("Enter number of processes: ");
        // int n = sc.nextInt();


        List<Process> processes = new ArrayList<>();

        
            processes.add(new Process("p1", 0, 8, 3));//  arrival burst priorty timequantum
            processes.add(new Process("p2", 1, 4, 1));
            processes.add(new Process("p3", 2, 2, 4));
            processes.add(new Process("p4", 3, 1, 2));
            processes.add(new Process("p5", 4, 3, 5));



        
        // شوف انت شغال علي ايه وفك كومنت بتاعه ورجع كومنت تاني لما ترفع علي git
        // ProcessRunner process=new ProcessRunner( new AGSecduler() );
        ProcessRunner process=new ProcessRunner( new RoundRobin(2,1) );
        // ProcessRunner process=new ProcessRunner( new ShortestJob() );
        // ProcessRunner process=new ProcessRunner( new priortySchedule() );
        process.processing(processes);


    }
}
