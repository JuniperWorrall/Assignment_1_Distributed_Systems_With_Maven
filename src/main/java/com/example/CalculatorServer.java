package com.example;
import java.rmi.*;
import java.rmi.registry.*;
public class CalculatorServer { //The server that clients will connect to, it sets up the registry and handles all their calls
    public static void main(String[] args) throws RemoteException {
        Registry registry;

        try { //If registry isn't available try to get it, if that doesn't work then create it
            System.out.println("Calculator server Start...");
            registry = LocateRegistry.getRegistry(1900);
            registry.list();
        } catch(Exception e) {
            registry = LocateRegistry.createRegistry(1900);
        }

        try { //Create skeleton then bind it to the registry
            Calculator calculator = new CalculatorImplementation();
            registry.rebind("CalculatorService", calculator);
        } catch(Exception e){
            System.err.println("Calculator stub exception:");
        }
    }
}