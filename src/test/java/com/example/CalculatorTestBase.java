package com.example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class CalculatorTestBase {
    protected Calculator calculator;

    @BeforeEach
    public void setUpBefore() throws Exception{
        CalculatorTestServer.start();
        calculator = CalculatorTestServer.getCalculator();
    }

    @AfterEach
    public void tearDownAfter() throws Exception{
        CalculatorTestServer.stop();
    }
}
