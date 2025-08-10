package com.example;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {
    public static void main(String[] args)
    {
        try{
            Registry registry = LocateRegistry.getRegistry("localhost",1900);
            Calculator calculator = (Calculator)registry.lookup("CalculatorService");
            System.out.println("Client Connected...");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}