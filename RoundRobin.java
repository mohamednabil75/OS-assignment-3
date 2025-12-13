import java.util.*;
class RoundRobin implements cpuSceduling {
    private int RoundRobin=0;
    private int CS=0;


    public RoundRobin(int rb,int contextswitch) {
        RoundRobin=rb;
        CS=contextswitch;
    }
    
    
    // public void increase(Queue<Process> proc, int time) {
    //     for (Process p : proc) {  
    //         p.waitingtime += time;    
    //     }
    // }
    public void process(List<Process> processes) {
        String executionorder="";
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
                executionorder+=current.name+" ";
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
                executionorder+=p.name+" ";            
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
                executionorder+=current.name+" ";


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
        System.out.println(executionorder);
        System.out.println("avg waiting = "+avgwaiting/(double)processes.size());
        System.out.println("avg turnaround time = "+avground/(double)processes.size());

    }
}