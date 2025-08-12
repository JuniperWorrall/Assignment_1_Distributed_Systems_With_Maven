package com.example;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorTestServer{
    private static Registry registry;
    private static Calculator calc;

    public static void start() throws Exception{ //Starts the test server
        if(registry == null){ //If registry isn't available try to get it, if that doesn't work then create it
            try{
                registry = LocateRegistry.getRegistry(1099);
                registry.list();
            } catch(RemoteException e){
                registry = LocateRegistry.createRegistry(1099);
            }

            if(calc == null){ //Create skeleton then bind it to the registry
                calc = new CalculatorImplementation();
                registry.rebind("CalculatorTestServer", calc);
            }
        }
    }

    public static void stop() { //Stop the server
        if (registry != null && calc != null) {
            try{
                registry.unbind("CalculatorTestServer"); //unbind the registry
            } catch(Exception ignored) {

            }

            calc = null;
            registry = null;
        }
    }

    public static Calculator getCalculator() throws Exception { //Get registry and look up the server
        Registry reg = LocateRegistry.getRegistry(1099);
        return (Calculator) reg.lookup("CalculatorTestServer");
    }
}
