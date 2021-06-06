# Relational Query Language Translator (RQLT)

`RQLT` is a command-line application that translates relational algebra expressions to relational calculus ones and vice-versa using Java and ANTLR.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

- Java SE Development Kit 11 or later
- [Apache Maven](https://maven.apache.org/) 3.6+
- Optional: Git (any reasonably recent version)

### Building

1. Unpack the archive anywhere on your system.

2. `cd` to the folder `relational-query-language-translator`.

3. Build the project with Maven:
```
mvn package
```
This will create the file `s1705270-0.0.1-SNAPSHOT.jar` in the `target/` subfolder.

### Installing

You can move the file `s1705270-0.0.1-SNAPSHOT.jar` to any location on your system. The
JAR contains all the necessary dependencies to be executed.

### Running

You can run the executable JAR with
```
java -jar target/s1705270-0.0.1-SNAPSHOT.jar
```