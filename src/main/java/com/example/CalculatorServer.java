package com.example;
import java.rmi.*;
import java.rmi.registry.*;
public class CalculatorServer
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Calculator server Start...");
            Calculator calculator = new CalculatorImplementation();
            Registry registry = LocateRegistry.createRegistry(1900);
            registry.rebind("CalculatorService", calculator);
            System.out.println("Calculator server readyu.u");
        }
        catch(Exception e) {
            System.err.println("Calculator server exception:");
            System.out.println(e);
        }
    }
}