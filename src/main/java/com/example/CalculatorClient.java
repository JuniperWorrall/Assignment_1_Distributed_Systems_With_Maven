package com.example;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class CalculatorClient { //The client that connects to the server and calls methods that will be run on the server
    static Calculator calculator;
    static String ClientID;
    public static void main(String[] args)
    {
        try{ //Gets the registry that the server set up and connects to it with a stub
            Registry registry = LocateRegistry.getRegistry("localhost",1900); //Starts the server
            calculator = (Calculator)registry.lookup("CalculatorService");//Gets the stub
            ClientID = calculator.registerClient(); //Registers the client
            System.out.println("Client Connected...");
            ClientInterface();
        } catch(Exception e){
            System.out.println("Client Not Connected...");
        }
    }

    public static void ClientInterface() throws RemoteException, InterruptedException {
        Scanner input = new Scanner(System.in);
        String userInput = "";
        System.out.println("Potential Commands:\npush 0, pop, delayPop 0, isEmpty, pushOperation (min/max/lcm/gcd), quit\n");
        System.out.println("Enter Command: ");

        while(!userInput.equalsIgnoreCase("quit")) {
            userInput = input.next();

            switch (userInput) {
                case "push": {
                    int val = input.nextInt();
                    calculator.pushValue(ClientID, val);
                    break;
                }
                case "delayPop": {
                    int milli = input.nextInt();
                    System.out.println(calculator.delayPop(ClientID, milli));
                    break;
                }
                case "pushOperation": {
                    String Operator = input.next();
                    calculator.pushOperation(ClientID, Operator);
                    break;
                }
                case "pop": {
                    System.out.println(calculator.pop(ClientID));
                    break;
                }
                case "isEmpty": {
                    System.out.println(calculator.isEmpty(ClientID));
                    break;
                }
            }
            System.out.println("Enter Command: ");
        }
    }
}