JUNIT_JAR = lib/junit-platform-console-standalone-1.11.3.jar
SRC = $(shell find src/main/java -name "*.java")
TEST_SRC = $(shell find src/test/java -name "*.java")
OUT_DIR = out

SERVER_JAR = Server-jar-with-dependencies.jar
CLIENT_JAR = Client-jar-with-dependencies.jar

# Default target
.PHONY: all
all: compile

# Compile all source files
.PHONY: compile
compile:
	@mkdir -p $(OUT_DIR)
	javac -d $(OUT_DIR) -cp $(JUNIT_JAR) $(SRC) $(TEST_SRC)

# Run the server
.PHONY: server
server:
	java -jar $(SERVER_JAR)

# Run the client
.PHONY: client
client:
	java -jar $(CLIENT_JAR)

# Run tests
.PHONY: test
test: compile
	java -cp "$(OUT_DIR):$(JUNIT_JAR)" org.junit.platform.console.ConsoleLauncher --scan-class-path

# Clean compiled classes
.PHONY: clean
clean:
	rm -rf $(OUT_DIR)

# Help
.PHONY: help
help:
	@echo "Makefile commands:"
	@echo "  make compile   - Compile all Java sources"
	@echo "  make server    - Run the server jar"
	@echo "  make client    - Run the client jar"
	@echo "  make test      - Compile and run all tests"
	@echo "  make clean     - Remove compiled classes"