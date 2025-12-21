import java.util.*;
class RoundRobin implements CpuScheduling {
    private int RoundRobin=0;
    private int CS=0;
    List<Process> processes ;
    List<String> order ;


    public RoundRobin(int rb,int contextswitch , List<Process> processes) {
        RoundRobin=rb;
        CS=contextswitch;
        this.processes = processes ;
        order = new ArrayList<>() ;
    }
    
    
    // public void increase(Queue<Process> proc, int time) {
    //     for (Process p : proc) {  
    //         p.waitingtime += time;    
    //     }
    // }
    public void process() {
        int currenttime=0;
        int contextswitch=0;
        Queue<Process> proc=new LinkedList<>();
        for(Process p:processes){
            currenttime+=contextswitch;
            if(!proc.isEmpty())
            while(p.arrival>=currenttime||p.arrival>proc.peek().waitingtime){
                if(proc.isEmpty()){
                    contextswitch=0;
                    if(p.arrival==currenttime)break;
                    currenttime++;
                }
                else{
                    Process current = proc.poll();
                    currenttime+=Math.min(current.remaining,RoundRobin);
                    current.remaining-=Math.min(current.remaining,RoundRobin);
                    current.waitingtime=currenttime;
                    if(current.remaining!=0)proc.add(current);
                    else{
                        current.turnaroundtime=currenttime-current.arrival;
                    }
                order.add(current.name) ;
                currenttime+=contextswitch;

                }
            }
            currenttime+=Math.min(p.remaining,RoundRobin);
            p.remaining-=Math.min(p.remaining,RoundRobin);
            contextswitch=CS;
            p.waitingtime=currenttime;
            if(p.remaining!=0)proc.add(p);
            else{

                p.turnaroundtime=currenttime-p.arrival;

                }
                order.add(p.name) ;        
        }
         while(!proc.isEmpty()){
                    currenttime+=contextswitch;
                    Process current = proc.poll();
                    currenttime+=Math.min(current.remaining,RoundRobin);
                    current.remaining-=Math.min(current.remaining,RoundRobin);
                    if(current.remaining!=0)proc.add(current);
                    else{
                        current.turnaroundtime=currenttime-current.arrival;

                    }
                order.add(current.name) ;


            }


    //output
     double avgwaiting=0;
     double avground=0;
     for (Process p : processes) {  
            p.waitingtime=p.turnaroundtime-p.burst;
            System.out.print("the waiting time of "+p.name+" is "+p.waitingtime);
            System.out.println(" and turn around time is "+(p.turnaroundtime));
            avgwaiting+=p.waitingtime;
            avground+=p.turnaroundtime;
        }       
        for(String s :order){
            System.err.print(s + " ");
        }
        System.out.println();
        System.out.println("avg waiting = "+avgwaiting/(double)processes.size());
        System.out.println("avg turnaround time = "+avground/(double)processes.size());

    }
    public Result getResults() {
    Result result = new Result();

    if (order == null || order.isEmpty()) {
        result.executionOrder = new ArrayList<>();
        result.processResults = new ArrayList<>();
        result.averageWaitingTime = 0.0;
        result.averageTurnaroundTime = 0.0;
        return result;
    }

    List<String> exec = new ArrayList<>();
    exec.add(order.get(0));
    for (int i = 1; i < order.size(); i++) {
        if (!order.get(i).equals(order.get(i - 1))) {
            exec.add(order.get(i));
        }
    }
    result.executionOrder = exec;

    if (processes != null && !processes.isEmpty()) {
        processes.sort((a, b) -> a.name.compareTo(b.name));
        List<processResult2> pr = new ArrayList<>();

        int totalWaiting = 0, totalTurnaround = 0;

        for (Process p : processes) {
            pr.add(new processResult2(p));
            totalWaiting += p.waitingtime;
            totalTurnaround += p.turnaroundtime;
        }

        result.processResults = pr;
        result.averageWaitingTime = (double) totalWaiting / processes.size();
        result.averageTurnaroundTime = (double) totalTurnaround / processes.size();
    } else {
        result.processResults = new ArrayList<>();
        result.averageWaitingTime = 0.0;
        result.averageTurnaroundTime = 0.0;
    }

    return result;
}    

}