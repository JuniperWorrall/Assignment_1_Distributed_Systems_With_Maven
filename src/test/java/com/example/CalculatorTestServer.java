package com.example;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorTestServer{
    private static Registry registry;

    public static void start() throws Exception{
        if(registry == null){
            registry = LocateRegistry.createRegistry(1900);
            Calculator calc = new CalculatorImplementation();
            Naming.rebind("src.test.CalculatorTestServer", calc);
        }
    }

    public static void stop() throws Exception {
        if (registry != null) {
            Naming.unbind("src.test.CalculatorTestServer");
            registry = null;
        }
    }

    public static Calculator getCalculator() throws Exception {
        return (Calculator) Naming.lookup("src.test.CalculatorTestServer");
    }
}
