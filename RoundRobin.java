import java.util.*;
class RoundRobin implements cpuSceduling {
    
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
            // System.out.println(currenttime+" "+p.arrival);

            while(p.arrival>=currenttime){
                if(proc.isEmpty()){
                    contextswitch=0;
                    if(p.arrival==currenttime)break;
                    currenttime++;
                }
                else{
                    Process current = proc.poll();
                    currenttime+=Math.min(current.remaining,current.quantum);
                    current.remaining-=Math.min(current.remaining,current.quantum);
                    // increase(proc, Math.min(current.remaining,current.quantum));
                    if(current.remaining!=0)proc.add(current);
                    else{
                        p.turnaroundtime=currenttime-p.arrival;
                        // System.out.println("current time is "+currenttime);
                    }
                executionorder+=current.name+" ";

                }
            }
            // p.waitingtime=currenttime-p.arrival;
            // increase(proc, Math.min(p.remaining,p.quantum));
            currenttime+=Math.min(p.remaining,p.quantum);
            p.remaining-=Math.min(p.remaining,p.quantum);
            contextswitch|=1;
            if(p.remaining!=0)proc.add(p);
            else{

                p.turnaroundtime=currenttime-p.arrival;
                }
                executionorder+=p.name+" ";

            
            
        }
         while(!proc.isEmpty()){
                    currenttime+=contextswitch;
                    Process current = proc.poll();
                    // increase(proc, Math.min(current.remaining,current.quantum));
                    currenttime+=Math.min(current.remaining,current.quantum);
                    current.remaining-=Math.min(current.remaining,current.quantum);
                    if(current.remaining!=0)proc.add(current);
                    else{
                        current.turnaroundtime=currenttime-current.arrival;
                    }
                executionorder+=current.name+" ";

            }
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