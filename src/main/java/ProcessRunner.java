import java.util.*;
class  ProcessRunner{
    public CpuScheduling cpuSc;

    public ProcessRunner(CpuScheduling cpuSC) {
        this.cpuSc=cpuSC;
    }
    
    public void processing(){
        cpuSc.process();
    };
}