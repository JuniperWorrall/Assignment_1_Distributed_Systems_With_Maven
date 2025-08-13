### README for Juniper Worrall's Assignment 1
Start by opening a bash terminal and running the command "java -jar Server-jar-with-dependencies.jar", this will initiate the jar file that will run the server.
On a seperate terminal, run the command "java -jar Client-jar-with-dependencies.jar", this does the same but for a client. You can start more of these in different terminals to simulate more than 1 client.

The available commands should be shown when run but all possible ones are:
* push x
* pop
* delayPop x
* isEmpty
* pushOperation min
* pushOperation max
* pushOperation lcm
* pushOperation gcd
* quit
where x is a positive integer.

#### Running Tests
If you are using intellij and maven you should be able to use the test button in lifecycle and it will run, if you are just using bash terminal run the command "java -cp "out:lib/junit-platform-console-standalone-1.11.3.jar" \
org.junit.platform.console.ConsoleLauncher --scan-class-path". If that does not run any tests, then first run "javac -d out -cp "lib/junit-platform-console-standalone-1.11.3.jar" \$(find src/main/java src/test/java -name "*.java")". Then you should be able to run the previous command and it will show 11 out of 11 tests successful.
