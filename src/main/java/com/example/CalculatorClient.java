package com.example;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient { //The client that connects to the server and calls methods that will be run on the server
    public static void main(String[] args)
    {
        try{ //Gets the registry that the server set up and connects to it with a stub
            Registry registry = LocateRegistry.getRegistry("localhost",1900);
            Calculator calculator = (Calculator)registry.lookup("CalculatorService");
            System.out.println("Client Connected...");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}