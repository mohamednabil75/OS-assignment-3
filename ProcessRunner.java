import java.util.*;
class  ProcessRunner{
    public cpuSceduling cpuSc;

    public ProcessRunner(cpuSceduling cpuSC) {
        this.cpuSc=cpuSC;
    }
    
    public void processing(List<Process> processes){
        cpuSc.process(processes);
    };
}