package com.example;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Stack;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    private Stack<Integer> intStack = new Stack<>();
    private Stack<String> strStack = new Stack<>();
    public CalculatorImplementation() throws RemoteException
    {
        super();
    }

    @Override

    public synchronized void pushValue(int val) throws RemoteException {
        intStack.push(val);
    }

    public synchronized void pushOperation(String operator) throws RemoteException {
        strStack.push(operator);
        int result = intStack.pop();
        if(operator.equals("min")){
            while(!intStack.isEmpty()){
                int current = intStack.pop();
                if(result > current) {
                    result = current;
                }
            }
        } else if(operator.equals("max")){
            while(!intStack.isEmpty()){
                int current = intStack.pop();
                if(result < current) {
                    result = current;
                }
            }
        } else if(operator.equals("lcm")){
            while(!intStack.isEmpty()){
                int current = intStack.pop();
                result = (((current * result)) / (gcd(current, result)));
            }
        } else if(operator.equals("gcd")){
            while(!intStack.isEmpty()){
                int current = intStack.pop();
                result = gcd(result, current);
            }
        }

        intStack.push(result);
    }

    public synchronized int pop() throws RemoteException {
        return intStack.pop();
    }

    public synchronized boolean isEmpty() throws RemoteException {
        return intStack.empty();
    }

    public synchronized int delayPop(int millis) throws InterruptedException, RemoteException {
        Thread.sleep(millis);
        return intStack.pop();
    }

    private synchronized int gcd(int a, int b){
        if(b == 0){
            return a;
        } else{
            return gcd(b, a % b);
        }
    }
}