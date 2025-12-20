package com.scheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AGSchedulerTest {
    
    private static final Gson gson = new Gson();
    
    static Stream<TestCase> testCases() {
        return Stream.of(
            loadTestCase("AG_test1.json"),
            loadTestCase("AG_test2.json"),
            loadTestCase("AG_test3.json"),
            loadTestCase("AG_test4.json"),
            loadTestCase("AG_test5.json"),
            loadTestCase("AG_test6.json")
        );
    }
    
    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Test AG Scheduler")
    void testAGScheduler(TestCase testCase) {
        System.out.println("\n=== Running Test: " + testCase.name + " ===");
        
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
        assertThat(actualResult.executionOrder)
            .as("Execution order mismatch for " + testCase.name)
            .isEqualTo(testCase.expectedOutput.executionOrder);
        
        // Verify average times (with tolerance for floating point)
        assertThat(actualResult.averageWaitingTime)
            .as("Average waiting time mismatch for " + testCase.name)
            .isEqualTo(testCase.expectedOutput.averageWaitingTime, within(0.01));
        
        assertThat(actualResult.averageTurnaroundTime)
            .as("Average turnaround time mismatch for " + testCase.name)
            .isEqualTo(testCase.expectedOutput.averageTurnaroundTime, within(0.01));
        
        // Verify individual process results
        verifyProcessResults(actualResult.processResults, testCase.expectedOutput.processResults, testCase.name);
        
        System.out.println("✓ Test passed: " + testCase.name);
    }
    
    private void verifyProcessResults(List<ProcessResult> actual, 
                                     List<ExpectedProcessResult> expected,
                                     String testName) {
        // Sort both lists by name
        actual.sort(Comparator.comparing(p -> p.name));
        expected.sort(Comparator.comparing(p -> p.name));
        
        assertThat(actual).hasSameSizeAs(expected);
        
        for (int i = 0; i < actual.size(); i++) {
            ProcessResult actualResult = actual.get(i);
            ExpectedProcessResult expectedResult = expected.get(i);
            
            String processName = actualResult.name;
            String prefix = "Process " + processName + " in " + testName + ": ";
            
            // Verify name
            assertThat(actualResult.name)
                .as(prefix + "Name mismatch")
                .isEqualTo(expectedResult.name);
            
            // Verify waiting time
            assertThat(actualResult.waitingTime)
                .as(prefix + "Waiting time mismatch")
                .isEqualTo(expectedResult.waitingTime);
            
            // Verify turnaround time
            assertThat(actualResult.turnaroundTime)
                .as(prefix + "Turnaround time mismatch")
                .isEqualTo(expectedResult.turnaroundTime);
            
            // Verify quantum history
            assertThat(actualResult.quantumHistory)
                .as(prefix + "Quantum history mismatch")
                .isEqualTo(expectedResult.quantumHistory);
        }
    }
    
    private static TestCase loadTestCase(String filename) {
        try {
            InputStream inputStream = AGSchedulerTest.class.getClassLoader()
                .getResourceAsStream(filename);
            
            if (inputStream == null) {
                throw new RuntimeException("Test file not found: " + filename);
            }
            
            Type testCaseType = new TypeToken<TestCase>(){}.getType();
            TestCase testCase = gson.fromJson(new InputStreamReader(inputStream), testCaseType);
            testCase.name = filename.replace(".json", "");
            return testCase;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test case: " + filename, e);
        }
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
    
    @Test
    @DisplayName("Edge Case: Empty Process List")
    void testEmptyProcessList() {
        List<Process> processes = new ArrayList<>();
        AGScheduler scheduler = new AGScheduler(processes);
        scheduler.process();
        AGResult result = scheduler.getResults();
        
        assertThat(result.executionOrder).isEmpty();
        assertThat(result.processResults).isEmpty();
        assertThat(result.averageWaitingTime).isEqualTo(0.0);
        assertThat(result.averageTurnaroundTime).isEqualTo(0.0);
    }
    
    @Test
    @DisplayName("Edge Case: Single Process")
    void testSingleProcess() {
        List<Process> processes = List.of(
            new Process("P1", 0, 5, 1, 10)
        );
        
        AGScheduler scheduler = new AGScheduler(processes);
        scheduler.process();
        AGResult result = scheduler.getResults();
        
        assertThat(result.executionOrder).containsExactly("P1");
        assertThat(result.processResults).hasSize(1);
        assertThat(result.processResults.get(0).waitingTime).isEqualTo(0);
        assertThat(result.processResults.get(0).turnaroundTime).isEqualTo(5);
        assertThat(result.averageWaitingTime).isEqualTo(0.0);
        assertThat(result.averageTurnaroundTime).isEqualTo(5.0);
    }
}