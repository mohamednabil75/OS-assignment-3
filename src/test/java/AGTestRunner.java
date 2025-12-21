import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AGSchedulerTest {
    
    private static final Gson gson = new Gson();
    
    @Test
    void testAG_test1() {
        System.out.println();
        System.out.println("Tesing AG Scheduler");
        runTest("AG_test1.json");
        runTest("AG_test2.json");
        runTest("AG_test3.json");
        runTest("AG_test4.json");
        runTest("AG_test5.json");
        runTest("AG_test6.json");
    }
    
    @Test
    void testSJF(){
        System.out.println();
        System.out.println("Tesing SJF Scheduler");
        runTestSJF("test_1.json");
        runTestSJF("test_2.json");
        runTestSJF("test_3.json");
        runTestSJF("test_4.json");
        runTestSJF("test_5.json");
        runTestSJF("test_6.json");

    }
    @Test
    void testPriority(){
        System.out.println();
        System.out.println("Tesing Priority Scheduler");
        runTestPriority("test_1.json");
        runTestPriority("test_2.json");
        runTestPriority("test_3.json");
        runTestPriority("test_4.json");
        runTestPriority("test_5.json");
        runTestPriority("test_6.json");

    }
    @Test
    void testRR(){
        System.out.println();
        System.out.println("Tesing Round Robin Scheduler");
        runTestRR("test_1.json");
        runTestRR("test_2.json");
        runTestRR("test_3.json");
        runTestRR("test_4.json");
        runTestRR("test_5.json");
        runTestRR("test_6.json");

    }
        private void runTestSJF(String filename) {
        System.out.println("\n=== Running Test: " + filename + " ===");
        
        TestCaseOther testCase = loadTestCaseOther(filename);
        if (testCase == null) {
            fail("Failed to load test case: " + filename);
        }
        // Convert input processes
        List<Process> processes = new ArrayList<>();
        for (InputProcess ip : testCase.input.processes) {
            processes.add(new Process(ip.name, ip.arrival, ip.burst, ip.priority, ip.quantum));
        }

        int contextSwitch = testCase.input.contextSwitch ;
        
        // Run scheduler
        // AGScheduler scheduler = new AGScheduler(processes);
        // scheduler.process();
        // AGResult actualResult = scheduler.getResults();
        ShortestJob scheduler = new ShortestJob(contextSwitch,processes) ;
        scheduler.process();
        Result s = scheduler.getResults();

        //System.out.println(s.executionOrder.toArray(new String[0])[0]);
        
        
        // Verify execution order
        assertArrayEquals(
            testCase.expectedOutput.SJF.executionOrder.toArray(new String[0]),
            s.executionOrder.toArray(new String[0]),
            "Execution order mismatch for " + filename
        );

        
        // Verify average times
        assertEquals(testCase.expectedOutput.SJF.averageWaitingTime, 
                    s.averageWaitingTime, 0.01,
                    "Average waiting time mismatch for " + filename);
        
        assertEquals(testCase.expectedOutput.SJF.averageTurnaroundTime, 
                    s.averageTurnaroundTime, 0.01,
                    "Average turnaround time mismatch for " + filename);
        
        // Verify process results
        assertEquals(testCase.expectedOutput.SJF.processResults.size(), 
                    s.processResults.size(),
                    "Number of processes mismatch in " + filename);
        
        // Sort both lists by name for comparison
        s.processResults.sort(Comparator.comparing(p -> p.name));
        testCase.expectedOutput.SJF.processResults.sort(Comparator.comparing(p -> p.name));
        
        for (int i = 0; i < s.processResults.size(); i++) {
            processResult2 actual = s.processResults.get(i);
            ExpectedProcessResult expected = testCase.expectedOutput.SJF.processResults.get(i);
            
            assertEquals(expected.name, actual.name, "Process name mismatch in " + filename);
            assertEquals(expected.waitingTime, actual.waitingTime, 
                        "Waiting time mismatch for " + expected.name + " in " + filename);
            assertEquals(expected.turnaroundTime, actual.turnaroundTime,
                        "Turnaround time mismatch for " + expected.name + " in " + filename);
        }
        
        System.out.println("Test passed: " + filename);
    }

private void runTestRR(String filename) {
        System.out.println("\n=== Running Test: " + filename + " ===");
        
        TestCaseOther testCase = loadTestCaseOther(filename);
        if (testCase == null) {
            fail("Failed to load test case: " + filename);
        }
        // Convert input processes
        List<Process> processes = new ArrayList<>();
        for (InputProcess ip : testCase.input.processes) {
            processes.add(new Process(ip.name, ip.arrival, ip.burst, ip.priority, ip.quantum));
        }

        int contextSwitch = testCase.input.contextSwitch ;
        int RR = testCase.input.rrQuantum ;
        
        // Run scheduler
        // AGScheduler scheduler = new AGScheduler(processes);
        // scheduler.process();
        // AGResult actualResult = scheduler.getResults();
        RoundRobin scheduler = new RoundRobin(RR,contextSwitch,processes) ;
        scheduler.process();
        Result s = scheduler.getResults();

        //System.out.println(s.executionOrder.toArray(new String[0])[0]);
        
        
        // Verify execution order
        assertArrayEquals(
            testCase.expectedOutput.RR.executionOrder.toArray(new String[0]),
            s.executionOrder.toArray(new String[0]),
            "Execution order mismatch for " + filename
        );

        
        // Verify average times
        assertEquals(testCase.expectedOutput.RR.averageWaitingTime, 
                    s.averageWaitingTime, 0.01,
                    "Average waiting time mismatch for " + filename);
        
        assertEquals(testCase.expectedOutput.RR.averageTurnaroundTime, 
                    s.averageTurnaroundTime, 0.01,
                    "Average turnaround time mismatch for " + filename);
        
        // Verify process results
        assertEquals(testCase.expectedOutput.RR.processResults.size(), 
                    s.processResults.size(),
                    "Number of processes mismatch in " + filename);
        
        // Sort both lists by name for comparison
        s.processResults.sort(Comparator.comparing(p -> p.name));
        testCase.expectedOutput.RR.processResults.sort(Comparator.comparing(p -> p.name));
        
        for (int i = 0; i < s.processResults.size(); i++) {
            processResult2 actual = s.processResults.get(i);
            ExpectedProcessResult expected = testCase.expectedOutput.RR.processResults.get(i);
            
            assertEquals(expected.name, actual.name, "Process name mismatch in " + filename);
            assertEquals(expected.waitingTime, actual.waitingTime, 
                        "Waiting time mismatch for " + expected.name + " in " + filename);
            assertEquals(expected.turnaroundTime, actual.turnaroundTime,
                        "Turnaround time mismatch for " + expected.name + " in " + filename);
        }
        
        System.out.println("Test passed: " + filename);
    }


    
    private void runTest(String filename) {
        System.out.println("\n=== Running Test: " + filename + " ===");
        TestCase testCase = loadTestCase(filename);
        if (testCase == null) {
            fail("Failed to load test case: " + filename);
        }
        
        // Convert input processes
        List<Process> processes = new ArrayList<>();
        for (InputProcess ip : testCase.input.processes) {
            processes.add(new Process(ip.name, ip.arrival, ip.burst, ip.priority, ip.quantum));
        }
        
        // Run scheduler
        AGScheduler scheduler = new AGScheduler(processes);
        scheduler.process();
        AGResult actualResult = scheduler.getResults();
        // System.out.println("----    ---  "+ testCase.expectedOutput.averageTurnaroundTime);
        // System.out.println("----    ---  "+ testCase.expectedOutput.averageTurnaroundTime);
        
        
        // Verify execution order
        assertArrayEquals(
            testCase.expectedOutput.executionOrder.toArray(new String[0]),
            actualResult.executionOrder.toArray(new String[0]),
            "Execution order mismatch for " + filename
        );
        
        // Verify average times
        assertEquals(testCase.expectedOutput.averageWaitingTime, 
                    actualResult.averageWaitingTime, 0.01,
                    "Average waiting time mismatch for " + filename);
        
        assertEquals(testCase.expectedOutput.averageTurnaroundTime, 
                    actualResult.averageTurnaroundTime, 0.01,
                    "Average turnaround time mismatch for " + filename);
        
        // Verify process results
        assertEquals(testCase.expectedOutput.processResults.size(), 
                    actualResult.processResults.size(),
                    "Number of processes mismatch in " + filename);
        
        // Sort both lists by name for comparison
        actualResult.processResults.sort(Comparator.comparing(p -> p.name));
        testCase.expectedOutput.processResults.sort(Comparator.comparing(p -> p.name));
        
        for (int i = 0; i < actualResult.processResults.size(); i++) {
            ProcessResult actual = actualResult.processResults.get(i);
            ExpectedProcessResult expected = testCase.expectedOutput.processResults.get(i);
            
            assertEquals(expected.name, actual.name, "Process name mismatch in " + filename);
            assertEquals(expected.waitingTime, actual.waitingTime, 
                        "Waiting time mismatch for " + expected.name + " in " + filename);
            assertEquals(expected.turnaroundTime, actual.turnaroundTime,
                        "Turnaround time mismatch for " + expected.name + " in " + filename);
            assertEquals(expected.quantumHistory, actual.quantumHistory,
                        "Quantum history mismatch for " + expected.name + " in " + filename);
        }
        
        System.out.println("Test passed: " + filename);
    }
     private void runTestPriority(String filename) {
        System.out.println("\n=== Running Test: " + filename + " ===");
        
        TestCaseOther testCase = loadTestCaseOther(filename);
        if (testCase == null) {
            fail("Failed to load test case: " + filename);
        }
        // Convert input processes
        List<Process> processes = new ArrayList<>();
        for (InputProcess ip : testCase.input.processes) {
            processes.add(new Process(ip.name, ip.arrival, ip.burst, ip.priority, ip.quantum));
        }

        int contextSwitch = testCase.input.contextSwitch ;
        int aging = testCase.input.agingInterval ;
        
        // Run scheduler
        // AGScheduler scheduler = new AGScheduler(processes);
        // scheduler.process();
        // AGResult actualResult = scheduler.getResults();
        priortySchedule scheduler = new priortySchedule(aging,contextSwitch,processes) ;
        scheduler.process();
        Result s = scheduler.getResults();

        //System.out.println(s.executionOrder.toArray(new String[0])[0]);
        
        
        // Verify execution order
        assertArrayEquals(
            testCase.expectedOutput.Priority.executionOrder.toArray(new String[0]),
            s.executionOrder.toArray(new String[0]),
            "Execution order mismatch for " + filename
        );

        
        // Verify average times
        assertEquals(testCase.expectedOutput.Priority.averageWaitingTime, 
                    s.averageWaitingTime, 0.01,
                    "Average waiting time mismatch for " + filename);
        
        assertEquals(testCase.expectedOutput.Priority.averageTurnaroundTime, 
                    s.averageTurnaroundTime, 0.01,
                    "Average turnaround time mismatch for " + filename);
        
        // Verify process results
        assertEquals(testCase.expectedOutput.Priority.processResults.size(), 
                    s.processResults.size(),
                    "Number of processes mismatch in " + filename);
        
        // Sort both lists by name for comparison
        s.processResults.sort(Comparator.comparing(p -> p.name));
        testCase.expectedOutput.Priority.processResults.sort(Comparator.comparing(p -> p.name));
        
        for (int i = 0; i < s.processResults.size(); i++) {
            processResult2 actual = s.processResults.get(i);
            ExpectedProcessResult expected = testCase.expectedOutput.Priority.processResults.get(i);
            
            assertEquals(expected.name, actual.name, "Process name mismatch in " + filename);
            assertEquals(expected.waitingTime, actual.waitingTime, 
                        "Waiting time mismatch for " + expected.name + " in " + filename);
            assertEquals(expected.turnaroundTime, actual.turnaroundTime,
                        "Turnaround time mismatch for " + expected.name + " in " + filename);
        }
        
        System.out.println("Test passed: " + filename);
    }
    
    private TestCase loadTestCase(String filename) {
        try {
            InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(filename);
            
            if (inputStream == null) {
                System.err.println("Warning: Test file not found: " + filename);
                return null;
            }
            
            Type testCaseType = new TypeToken<TestCase>(){}.getType();
            TestCase testCase = gson.fromJson(new InputStreamReader(inputStream), testCaseType);
            testCase.name = filename.replace(".json", "");
            return testCase;
            
        } catch (Exception e) {
            System.err.println("Error loading test case " + filename + ": " + e.getMessage());
            return null;
        }
    }
    private TestCaseOther loadTestCaseOther(String filename) {
        try {
            InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(filename);
            
            if (inputStream == null) {
                System.err.println("Warning: Test file not found: " + filename);
                return null;
            }
            
            Type testCaseType = new TypeToken<TestCaseOther>(){}.getType();
            TestCaseOther testCase = gson.fromJson(new InputStreamReader(inputStream), testCaseType);
            testCase.name = filename.replace(".json", "");
            return testCase;
            
        } catch (Exception e) {
            System.err.println("Error loading test case " + filename + ": " + e.getMessage());
            return null;
        }
    }
    
    @Test
    void testEmptyProcessList() {
        List<Process> processes = new ArrayList<>();
        AGScheduler scheduler = new AGScheduler(processes);
        scheduler.process();
        AGResult result = scheduler.getResults();
        
        assertTrue(result.executionOrder.isEmpty());
        assertTrue(result.processResults.isEmpty());
        assertEquals(0.0, result.averageWaitingTime);
        assertEquals(0.0, result.averageTurnaroundTime);
    }
    
    @Test
    void testSingleProcess() {
        List<Process> processes = Arrays.asList(
            new Process("P1", 0, 5, 1, 10)
        );
        
        AGScheduler scheduler = new AGScheduler(processes);
        scheduler.process();
        AGResult result = scheduler.getResults();
        
        assertEquals(1, result.executionOrder.size());
        assertEquals("P1", result.executionOrder.get(0));
        assertEquals(1, result.processResults.size());
        assertEquals(0, result.processResults.get(0).waitingTime);
        assertEquals(5, result.processResults.get(0).turnaroundTime);
        assertEquals(0.0, result.averageWaitingTime);
        assertEquals(5.0, result.averageTurnaroundTime);
    }
    
    // Test case data classes
    static class TestCase {
        String name;
        Input input;
        ExpectedOutput expectedOutput;
    }
    static class TestCaseOther {
        String name;
        Input input;
        ExpectedOutputOther expectedOutput;
    }
    
    static class Input {
        public int contextSwitch;
        public int agingInterval ;
        public int rrQuantum ;
        List<InputProcess> processes;
    }
    
    static class InputProcess {
        String name;
        int arrival;
        int burst;
        int priority;
        int quantum;
    }
    
    static class ExpectedOutput {
        List<String> executionOrder;
        List<ExpectedProcessResult> processResults;
        double averageWaitingTime;
        double averageTurnaroundTime;
    }
    static class ExpectedOutputOther {
        schedulerType SJF ;
        schedulerType Priority ;
        schedulerType RR ;


    }
    static class schedulerType{
        List<String> executionOrder;
        List<ExpectedProcessResult> processResults;
        double averageWaitingTime;
        double averageTurnaroundTime;

    }

    
    static class ExpectedProcessResult {
        String name;
        int waitingTime;
        int turnaroundTime;
        List<Integer> quantumHistory;
    }


    

}