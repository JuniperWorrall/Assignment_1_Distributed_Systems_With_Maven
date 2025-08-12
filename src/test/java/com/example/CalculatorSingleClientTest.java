package com.example;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.rmi.RemoteException;

public class CalculatorSingleClientTest extends CalculatorTestBase{
    static String ClientID; //Stores the client's ID

    @BeforeAll //Registers the client before doing anything else
    public static void registerAllClients() throws RemoteException{
        ClientID = calculator.registerClient();
    }

    @Test
    public void testPushAndPop() throws RemoteException { //Tests that pushing and popping works
        calculator.pushValue(ClientID, 42);
        assertEquals(42, calculator.pop(ClientID));
        assertTrue(calculator.isEmpty(ClientID));
    }

    @Test
    public void testMinOperation() throws RemoteException { //Tests that the min pushOperation works
        calculator.pushValue(ClientID, 10);
        calculator.pushValue(ClientID, 5);
        calculator.pushValue(ClientID, 15);
        calculator.pushOperation(ClientID, "min");
        assertEquals(5, calculator.pop(ClientID));
        assertTrue(calculator.isEmpty(ClientID));
    }

    @Test
    public void testMaxOperation() throws RemoteException { //Tests that the max pushOperation works
        calculator.pushValue(ClientID, 10);
        calculator.pushValue(ClientID, 5);
        calculator.pushValue(ClientID, 15);
        calculator.pushOperation(ClientID, "max");
        assertEquals(15, calculator.pop(ClientID));
        assertTrue(calculator.isEmpty(ClientID));
    }

    @Test
    public void testGCDOperation() throws RemoteException { //Tests that the gcd pushOperation works
        calculator.pushValue(ClientID, 24);
        calculator.pushValue(ClientID, 36);
        calculator.pushOperation(ClientID, "gcd");
        assertEquals(12, calculator.pop(ClientID));
        assertTrue(calculator.isEmpty(ClientID));
    }

    @Test
    public void testLCMOperation() throws RemoteException { //Tests that the lcm pushOperation works
        calculator.pushValue(ClientID, 12);
        calculator.pushValue(ClientID, 18);
        calculator.pushOperation(ClientID, "lcm");
        assertEquals(36, calculator.pop(ClientID));
        assertTrue(calculator.isEmpty(ClientID));
    }

    @Test
    public void testDelayPop() throws Exception { //Tests that delayPop works
        calculator.pushValue(ClientID, 99);
        long start = System.currentTimeMillis();
        int result = calculator.delayPop(ClientID, 1000);
        long duration = System.currentTimeMillis() - start;

        assertEquals(99, result);
        assertTrue(duration >= 1000);
        assertTrue(calculator.isEmpty(ClientID));
    }

    @Test
    public void testIsEmpty() throws Exception { //Tests that delayPop works
        boolean result = calculator.isEmpty(ClientID);
        assertTrue(result);
        calculator.pushValue(ClientID, 12);
        result = calculator.isEmpty(ClientID);
        assertFalse(result);
        calculator.pop(ClientID);
        assertTrue(calculator.isEmpty(ClientID));
    }
}