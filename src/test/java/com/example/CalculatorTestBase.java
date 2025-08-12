package com.example;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class CalculatorTestBase { //Sets up clients for the testing architecture
    protected static Calculator calculator;

    @BeforeAll
    public static void setUpBefore() throws Exception {
        try {
            CalculatorTestServer.start(); //Starts the server
            calculator = CalculatorTestServer.getCalculator(); //Gets the stub
            calculator.registerClient(); //Registers the client
        } catch (Exception e) {
            CalculatorTestServer.stop();
            Thread.sleep(500);
        }
    }

    @AfterAll
    public static void tearDownAfter() throws Exception {
        CalculatorTestServer.stop(); //Stop the server
        Thread.sleep(100);
    }
}
