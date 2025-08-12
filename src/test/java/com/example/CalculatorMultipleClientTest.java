package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CalculatorMultipleClientTest extends CalculatorTestBase{ //Tests that multiple clients can use java rmi on multiple methods
    final static int clientAmount = 5; //Sets the number of concurrent clients
    static String[] clientIDArray; //Stores all client IDs

    @BeforeAll
    public static void registerAllClients() throws RemoteException{ //Registers a separate clientID and stack for each client
        clientIDArray = new String[clientAmount];
        for(int i = 0; i < clientAmount; i++){
            clientIDArray[i] = calculator.registerClient();
        }
    }

    @Test
    public void testMultiClientPushAndPop() throws RemoteException, InterruptedException { //Ensures multiple clients can use pop and push
        final int callsPerClient = 10;
        ExecutorService executor = Executors.newFixedThreadPool(clientAmount); //Allows multiple clients to execute at once
        CountDownLatch countDownLatch = new CountDownLatch(clientAmount); //Ensure threads wait until all other threads are completed

        for(int i = 0; i < clientAmount; i++){
            final int clientNumber = i;
            executor.execute(() -> {
                try{
                    for(int j = 0; j < callsPerClient; j++){
                        int value = clientNumber * callsPerClient + j;
                        calculator.pushValue(clientIDArray[clientNumber], value); //Pushes a unique value for that specific client and call number
                        assertEquals(value, calculator.pop(clientIDArray[clientNumber])); //Makes sure the value popped is the same
                    }
                } catch(RemoteException e){
                    fail("Remote exception: " + e.getMessage());
                } finally{
                    countDownLatch.countDown();
                }
            });
        }

        assertTrue(countDownLatch.await(10, TimeUnit.SECONDS), "Threads took too long");
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Threads didn't shut down");
        for(int i = 0; i < clientAmount; i++){
            assertTrue(calculator.isEmpty(clientIDArray[i])); //Ensure all stacks are empty
        }
    }

    @Test
    public void testMultiClientMinOperation() throws RemoteException, InterruptedException { //Ensures multiple clients can use min push operation
        final int callsPerClient = 10;
        ExecutorService executor = Executors.newFixedThreadPool(clientAmount); //Allows multiple clients to execute at once
        CountDownLatch countDownLatch = new CountDownLatch(clientAmount); //Ensure threads wait until all other threads are completed

        for(int i = 0; i < clientAmount; i++){
            final int baseNum = (i + 1) * 100;
            final int clientNumber = i;
            executor.execute(() -> {
                try{
                    for(int j = 0; j < callsPerClient; j++){ //Pushes three values onto the stack and pushes the smallest back onto the stack after popping all
                        calculator.pushValue(clientIDArray[clientNumber], baseNum + 10);
                        calculator.pushValue(clientIDArray[clientNumber],baseNum + 5);
                        calculator.pushValue(clientIDArray[clientNumber],baseNum + 15);
                        calculator.pushOperation(clientIDArray[clientNumber],"min");
                        assertEquals(baseNum + 5, calculator.pop(clientIDArray[clientNumber])); //Makes sure the value popped is the same
                    }
                } catch(RemoteException e){
                    fail("Remote exception: " + e.getMessage());
                } finally{
                    countDownLatch.countDown();
                }
            });
        }

        assertTrue(countDownLatch.await(10, TimeUnit.SECONDS), "Threads took too long");
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Threads didn't shut down");
        for(int i = 0; i < clientAmount; i++){
            assertTrue(calculator.isEmpty(clientIDArray[i])); //Ensure all stacks are empty
        }
    }

    @Test
    public void testMultiClientMaxOperation() throws Exception { //Ensures multiple clients can use max push operation
        final int callsPerClient = 10;
        ExecutorService executor = Executors.newFixedThreadPool(clientAmount); //Allows multiple clients to execute at once
        CountDownLatch countDownLatch = new CountDownLatch(clientAmount); //Ensure threads wait until all other threads are completed

        for(int i = 0; i < clientAmount; i++){
            final int clientNumber = i;
            executor.execute(() -> {
                try{
                    for(int j = 0; j < callsPerClient; j++){
                        int baseNum = (clientNumber * 100) + j;
                        calculator.pushValue(clientIDArray[clientNumber],baseNum + 10);
                        calculator.pushValue(clientIDArray[clientNumber],baseNum + 5);
                        calculator.pushValue(clientIDArray[clientNumber],baseNum + 15);
                        calculator.pushOperation(clientIDArray[clientNumber],"max");
                        assertEquals(baseNum + 15, calculator.pop(clientIDArray[clientNumber])); //Makes sure the value popped is the same
                    }
                } catch(RemoteException e){
                    fail("Remote exception: " + e.getMessage());
                } finally{
                    countDownLatch.countDown();
                }
            });
        }

        assertTrue(countDownLatch.await(10, TimeUnit.SECONDS), "Threads took too long");
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Threads didn't shut down");
        for(int i = 0; i < clientAmount; i++){
            assertTrue(calculator.isEmpty(clientIDArray[i])); //Ensure all stacks are empty
        }
    }

    @Test
    public void testMultiClientStressTest() throws RemoteException, InterruptedException { //Ensures multiple clients can use the code with great volume
        final int callsPerClient = 100;
        ExecutorService executor = Executors.newFixedThreadPool(clientAmount); //Allows multiple clients to execute at once
        CountDownLatch countDownLatch = new CountDownLatch(clientAmount); //Ensure threads wait until all other threads are completed

        for(int i = 0; i < clientAmount; i++){
            final int clientNumber = i;
            executor.execute(() -> {
                try{
                    for(int j = 0; j < callsPerClient; j++){ //Switches between popping pushed values and min/maxxing them to simulate high volume call numbers
                        if(j % 2 == 0){
                            int value = clientNumber * callsPerClient + j;
                            calculator.pushValue(clientIDArray[clientNumber], value);
                            assertEquals(value, calculator.pop(clientIDArray[clientNumber])); //Makes sure the value popped is the same
                        } else{
                            calculator.pushValue(clientIDArray[clientNumber],clientNumber + 10);
                            calculator.pushValue(clientIDArray[clientNumber],clientNumber + 20);
                            calculator.pushOperation(clientIDArray[clientNumber], j % 4 == 1 ? "min" : "max");
                            int result = calculator.pop(clientIDArray[clientNumber]);
                            assertTrue(result == clientNumber + 10 || result == clientNumber + 20); //Makes sure the value popped is the same
                        }
                    }
                } catch(RemoteException e){
                    fail("Remote exception: " + e.getMessage());
                } finally{
                    countDownLatch.countDown();
                }
            });
        }

        assertTrue(countDownLatch.await(10, TimeUnit.SECONDS), "Threads took too long");
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Threads didn't shut down");
        for(int i = 0; i < clientAmount; i++){
            assertTrue(calculator.isEmpty(clientIDArray[i])); //Ensure all stacks are empty
        }
    }
}