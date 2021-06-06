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

### EX) Relational Algebra to Relational Calculus
With no custom environment:

<img width="648" alt="RAtoRC1" src="https://user-images.githubusercontent.com/32841130/120931399-3bed0200-c6e9-11eb-978e-22eb6c0ad83e.png">

With a custom environment:

<img width="659" alt="RAtoRC2" src="https://user-images.githubusercontent.com/32841130/120931405-3f808900-c6e9-11eb-8a93-49793d9a06f6.png">

### EX) Relational Calculus to Relational Algebra
With no custom environment:

<img width="656" alt="RCtoRA1" src="https://user-images.githubusercontent.com/32841130/120931465-8c645f80-c6e9-11eb-995c-6d5a61fd00b1.png">

With a custom environment:

<img width="663" alt="RCtoRA2" src="https://user-images.githubusercontent.com/32841130/120931469-95edc780-c6e9-11eb-8e15-a78cd4d0b9e5.png">


### EX) Showcasing with a real question from a past paper exam in Database Systems course at The University of Edinburgh
Relational Algebra to Relational Calculus:

<img width="662" alt="complex1" src="https://user-images.githubusercontent.com/32841130/120931512-cf263780-c6e9-11eb-9702-7553acc9a7b1.png">

Relational Calculus to Relational Algebra:

<img width="665" alt="complex2" src="https://user-images.githubusercontent.com/32841130/120931517-d3525500-c6e9-11eb-88cc-2fb8d6e2ad3b.png">
