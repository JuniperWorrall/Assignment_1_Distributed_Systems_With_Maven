package com.example;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.rmi.RemoteException;

public class CalculatorSingleClientTest {
    private Calculator calculator;

    @Test
    public void testPushAndPop() throws RemoteException {
        calculator.pushValue(42);
        assertEquals(42, calculator.pop());
        assertTrue(calculator.isEmpty());
    }

    @Test
    public void testMinOperation() throws RemoteException {
        calculator.pushValue(10);
        calculator.pushValue(5);
        calculator.pushValue(15);
        calculator.pushOperation("min");
        assertEquals(5, calculator.pop());
        assertTrue(calculator.isEmpty());
    }

    @Test
    public void testMaxOperation() throws RemoteException {
        calculator.pushValue(10);
        calculator.pushValue(5);
        calculator.pushValue(15);
        calculator.pushOperation("max");
        assertEquals(15, calculator.pop());
        assertTrue(calculator.isEmpty());
    }

    @Test
    public void testGCDOperation() throws RemoteException {
        calculator.pushValue(24);
        calculator.pushValue(36);
        calculator.pushOperation("gcd");
        assertEquals(12, calculator.pop());
        assertTrue(calculator.isEmpty());
    }

    @Test
    public void testLCMOperation() throws RemoteException {
        calculator.pushValue(12);
        calculator.pushValue(18);
        calculator.pushOperation("lcm");
        assertEquals(36, calculator.pop());
        assertTrue(calculator.isEmpty());
    }

    @Test
    public void testDelayPop() throws Exception {
        calculator.pushValue(99);
        long start = System.currentTimeMillis();
        int result = calculator.delayPop(1000);
        long duration = System.currentTimeMillis() - start;

        assertEquals(99, result);
        assertTrue(duration >= 1000);
        assertTrue(calculator.isEmpty());
    }
}