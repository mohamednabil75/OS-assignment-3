import java.util.ArrayList;
import java.util.List;

class priortySchedule {
     int Aging;
     int contextswitch;
     List<Process> processes ;
     List<String> order ;

    public priortySchedule(int Aging,int contextswitch , List<Process> processes) {
        this.Aging=Aging;
        this.contextswitch=contextswitch;
        this.processes = processes ;
        this.order = new ArrayList<>() ;
    }
    
        public Process highestPriority(List<Process> ready) {
            List<Integer> waiting = new ArrayList<>();
            List<Integer> prio = new ArrayList<>();


        if (ready == null || ready.size() == 0) {
            return null;
        }
        int s = 0;
        waiting.add(ready.get(0).waitingtime);
        prio.add(ready.get(0).priority);

        for (int i = 1; i < ready.size(); i++) {
        waiting.add(ready.get(i).waitingtime);
        prio.add(ready.get(i).priority);


            if (ready.get(i).priority < ready.get(s).priority) {
                s = i;
            }
        }
    //  for (int x : waiting) {
    // System.out.print(x+" ");
    //     }
    //     System.out.println();
    //       for (int x : prio) {
    // System.out.print(x+" ");
    //     }
    //     System.out.println();

        ready.get(s).waitingtime=0;
        return ready.get(s);
    }
    public void Aging(List<Process> ready){
        for (int i = 0; i < ready.size(); i++) {
            ready.get(i).waitingtime++;
            if(ready.get(i).waitingtime==Aging) 
            {
                if(ready.get(i).priority>1){
                ready.get(i).priority--;
                }
                ready.get(i).waitingtime=0;
            }
        }
    }
    void updateReadyQueue(int currentTime[],List<Process> readyProcesses,List<Process> processes){
    for(int i=0 ;i<processes.size();i++){
        if(processes.get(i).arrival<=currentTime[0]){
            readyProcesses.add(processes.get(i));
            processes.remove(i);
            i--;
        }
    }
    }

    public void process(){
        int currenttime[]={0};
        List<Process> ready=new ArrayList<>();
        List<Process> copied=new ArrayList<>();
        Process pre=null;
        Process p=null;
        while(processes.size()!=0||ready.size()!=0){
            updateReadyQueue(currenttime, ready, processes);
            // System.out.println(ready.size());
            p=highestPriority(ready);

            //for context switch
            if(pre!=null && pre!=p){
            order.add(pre.name);
            // System.out.println(currenttime[0]);
            currenttime[0]+=contextswitch;
            // System.out.println("CS");
            p.waitingtime=0;
            int z=contextswitch;
            while(z>0){
                z--;
                Aging(ready);
            }
            pre=p;
            continue;
            }
            pre=p;
            // System.out.println(currenttime[0]);
            // System.out.println(p.name+"   "+p.priority); //if u want trace code second by second 
            p.remaining--;
            currenttime[0]+=1;
            if(p.remaining==0){
                //  System.out.println("ended");
                p.turnaroundtime=currenttime[0]-p.arrival; //calcuate around to add to answer
                ready.remove(p);
                copied.add(p);
            }
            p.waitingtime=0;
            Aging(ready);
            p.waitingtime=0;



            }
            order.add(p.name);



    //output
     double avgwaiting=0;
     double avground=0;
     for (Process pro : copied) {  
            pro.waitingtime=pro.turnaroundtime-pro.burst;
            System.out.print("the waiting time of "+pro.name+" is "+pro.waitingtime);
            System.out.println(" and turn around time is "+(pro.turnaroundtime));
            avgwaiting+=pro.waitingtime;
            avground+=pro.turnaroundtime;
        }       
        for(String s: order){
            System.out.print(s+"-");
        }
        System.out.println();
        System.out.println("avg waiting = "+avgwaiting/(double)copied.size());
        System.out.println("avg turnaround time = "+avground/(double)copied.size());
        processes=copied;

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
};

