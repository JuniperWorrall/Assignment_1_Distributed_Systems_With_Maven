package com.example;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorTestBase {
    protected static Calculator calculator;

    @BeforeAll
    public static void setUpBefore() throws Exception{
        int attempts = 0;
        while (attempts < 3) {
            try {
                CalculatorTestServer.start();
                calculator = CalculatorTestServer.getCalculator();
                calculator.registerClient();
                return;
            } catch (Exception e) {
                CalculatorTestServer.stop();
                Thread.sleep(500);
                attempts++;
                if(attempts >= 3) {
                    throw e;
                }
            }
        }
    }

    @AfterAll
    public static void tearDownAfter() throws Exception{
        CalculatorTestServer.stop();
        Thread.sleep(100);
    }
}
