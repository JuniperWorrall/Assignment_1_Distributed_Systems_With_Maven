package com.example;
import java.rmi.*;
//Interface for the project, sets up the methods
public interface Calculator extends Remote {
    public void pushValue(String ClientID, int val) throws RemoteException; //Push value onto the stack
    public void pushOperation(String ClientID, String operator) throws RemoteException; //Push an operation that affects the numbers on the stack
    public int pop(String ClientID) throws RemoteException; //Pops the highest number on the stack to the client
    public boolean isEmpty(String ClientID) throws RemoteException; //Checks if the stack is empty
    public int delayPop(String ClientID, int millis) throws RemoteException, InterruptedException; //Pops the highest number on the stack with a millisecond delay given by the client
    public String registerClient() throws RemoteException;
    public Object getClientLock(String clientID) throws RemoteException;
}