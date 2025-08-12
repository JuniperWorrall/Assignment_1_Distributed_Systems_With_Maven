package com.example;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator { //The logic for the methods that will be run by the server
    private final Map<String, Stack<String>> clientStrStacks = new ConcurrentHashMap<>();
    private final Map<String, Stack<Integer>> clientIntStacks = new ConcurrentHashMap<>();
    private final Map<String, Object> clientLocks = new ConcurrentHashMap<>();

    public CalculatorImplementation() throws RemoteException
    {
        super();
    }

    @Override

    //Pushes value given by client onto the stack
    public void pushValue(String ClientID, int val) throws RemoteException {
        Stack<Integer> stack = clientIntStacks.computeIfAbsent(ClientID, k -> new Stack<>());
        synchronized (getClientLock(ClientID)) {
            stack.push(val);
        }
    }

    //Pushes operator onto the string stack and runs its logic while emptying the stack
    public void pushOperation(String ClientID, String operator) throws RemoteException {
        Stack<Integer> stack = clientIntStacks.computeIfAbsent(ClientID, k -> new Stack<>());
        Stack<String> strStack = clientStrStacks.computeIfAbsent(ClientID, k -> new Stack<>());

        synchronized (getClientLock(ClientID)) {
            strStack.push(operator);
            int result = pop(ClientID);

            switch (operator) {
                case "min": { //Finds the smallest number on the stack and returns it to the stack
                    while (!stack.isEmpty()) {
                        int current = stack.pop();
                        if (result > current) {
                            result = current;
                        }
                    }
                    break;
                }
                case "max": { //Finds the largest number on the stack and returns it to the stack
                    while (!stack.isEmpty()) {
                        int current = stack.pop();
                        if (result < current) {
                            result = current;
                        }
                    }
                    break;
                }
                case "lcm": { //Finds the lowest common multiple between numbers on the stack and returns it
                    while (!stack.isEmpty()) {
                        int current = stack.pop();
                        long multiple = (long) current * result;
                        result = (int)((multiple) / (gcd(current, result)));
                    }
                    break;
                }
                case "gcd": { //Finds the greatest common divisor between numbers on the stack and returns it
                    while (!stack.isEmpty()) {
                        int current = stack.pop();
                        result = gcd(result, current);
                    }
                    break;
                }
            }
            stack.push(result);
        }
    }

    public int pop(String ClientID) throws RemoteException { //Pops the highest number off the stack and returns it to the client
        Stack<Integer> stack = clientIntStacks.computeIfAbsent(ClientID, k -> new Stack<>());
        synchronized (getClientLock(ClientID)){
            if(stack.empty()){ //if stack is empty, instead of popping it returns -1, this should not happen when working properly
                throw new IllegalStateException("Stack is empty, cannot perform operation");
            } else {
                return stack.pop();
            }
        }
    }

    public boolean isEmpty(String ClientID) throws RemoteException { //Checks if stack is empty
        Stack<Integer> stack = clientIntStacks.computeIfAbsent(ClientID, k -> new Stack<>());
        synchronized (getClientLock(ClientID)){
            return stack.empty();
        }
    }

    public int delayPop(String ClientID, int millis) throws InterruptedException, RemoteException { //Pops highest number on the stack with a millisecond delay given by the client
        Stack<Integer> stack = clientIntStacks.computeIfAbsent(ClientID, k -> new Stack<>());
        synchronized (getClientLock(ClientID)){
            try{
                Thread.sleep(millis);
                return stack.pop();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                throw new RemoteException("Interruption while waiting.");
            }
        }
    }

    public synchronized String registerClient() throws RemoteException{
        String clientID = UUID.randomUUID().toString();
        clientIntStacks.put(clientID, new Stack<>());
        clientLocks.put(clientID, new Object());
        return clientID;
    }

    public Object getClientLock(String clientID) throws RemoteException{
        return clientLocks.computeIfAbsent(clientID, k -> new Object());
    }

    private synchronized int gcd(int a, int b){ //calculates the greatest common divisor between two numbers
        if(b == 0){
            return a;
        } else{
            return gcd(b, a % b);
        }
    }
}