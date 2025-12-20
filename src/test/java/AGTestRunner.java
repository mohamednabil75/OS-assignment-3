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
        runTest("AG_test1.json");
    }
    
    @Test
    void testAG_test2() {
        runTest("AG_test2.json");
    }
    
    @Test
    void testAG_test3() {
        runTest("AG_test3.json");
    }
    
    @Test
    void testAG_test4() {
        runTest("AG_test4.json");
    }
    
    @Test
    void testAG_test5() {
        runTest("AG_test5.json");
    }
    
    @Test
    void testAG_test6() {
        runTest("AG_test6.json");
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
        
        System.out.println("✓ Test passed: " + filename);
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
    
    static class Input {
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
    
    static class ExpectedProcessResult {
        String name;
        int waitingTime;
        int turnaroundTime;
        List<Integer> quantumHistory;
    }
}