package com.example;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorTestServer{
    private static Registry registry;
    private static Calculator calc;
    private static final int PORT = 1099;

    public static void start() throws Exception{
        if(registry == null){
            try{
                registry = LocateRegistry.getRegistry(PORT);
                registry.list();
            } catch(RemoteException e){
                registry = LocateRegistry.createRegistry(PORT);
            }

            if(calc == null){
                calc = new CalculatorImplementation();
                registry.rebind("CalculatorTestServer", calc);
            }
        }
    }

    public static void stop() throws Exception {
        if (registry != null && calc != null) {
            try{
                registry.unbind("CalculatorTestServer");
            } catch(Exception ignored) {

            }

            try{
                UnicastRemoteObject.unexportObject(calc, true);
            } catch (Exception ignored) {

            }

            calc = null;
            registry = null;
        }
    }

    public static Calculator getCalculator() throws Exception {
        Registry reg = LocateRegistry.getRegistry(1099);
        return (Calculator) reg.lookup("CalculatorTestServer");
    }
}
