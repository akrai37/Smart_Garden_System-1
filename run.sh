#!/bin/bash

# Smart Garden System Runner
# This script ensures the application runs with Java 21

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

echo "Using Java version:"
java -version
echo ""

echo "Starting Smart Garden System..."
mvn javafx:run
