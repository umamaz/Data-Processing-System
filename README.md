# Data Processing System - Java and Go

## Overview

This project implements a concurrent **Data Processing System** in both **Java** and **Go**. The system simulates multiple worker threads processing tasks in parallel from a shared queue while safely managing shared resources.

The project demonstrates:

* Multithreading and concurrency
* Shared resource management
* Synchronization techniques
* Exception and error handling
* Logging
* File output generation
* Safe thread and goroutine termination

---

## Project Description

The application creates a collection of tasks and places them into a shared queue. Multiple worker threads (Java) or goroutines (Go) retrieve tasks from the queue, simulate processing through a time delay, and store results in a shared resource.

The system ensures:

* Safe concurrent access to shared resources
* No duplicate task processing
* No missed tasks
* Proper synchronization
* Graceful handling of errors
* Safe program termination

---

## Technologies Used

### Java

* Java 17+
* ExecutorService
* Runnable
* synchronized methods
* FileWriter
* Exception Handling

### Go

* Go 1.20+
* Goroutines
* Channels
* WaitGroup
* Mutex
* File Handling
* Error Checking

---

## Features

### Shared Task Queue

Both implementations provide a shared task queue with:

* addTask() / AddTask()
* getTask() / GetTask()

These methods allow workers to safely retrieve tasks while preventing race conditions.

### Worker Processing

Each worker:

1. Retrieves a task from the queue.
2. Simulates computational work using a delay.
3. Processes the task.
4. Stores results in a shared result collection.
5. Logs execution details.

### Concurrency Management

#### Java

* ExecutorService thread pool
* synchronized queue methods
* Thread-safe result storage

#### Go

* Goroutines
* Channels for task distribution
* WaitGroup for synchronization
* Mutex for shared result access

### Error Handling

#### Java

* try-catch blocks
* InterruptedException handling
* IOException handling
* Runtime exception simulation

#### Go

* Explicit error checking
* defer statements for cleanup
* File I/O error handling
* Simulated processing errors

### Logging

Both implementations log:

* Worker startup
* Task processing
* Task completion
* Error events
* Program completion

### Output File

Results are written to:

results.txt

after all tasks have been processed.

---

## Running the Java Program

### Compile

```bash
javac DataProcessingSystem.java
```

### Run

```bash
java DataProcessingSystem
```

---

## Running the Go Program

### Run

```bash
go run main.go
```

---

## Sample Output

```text
Worker-1 started
Worker-2 started
Worker-3 started
Worker-4 started

Task 1 processed by Worker-1
Task 2 processed by Worker-2
Task 3 processed by Worker-3

...

Worker-1 ERROR while processing Task 10

Results saved to results.txt
Program Completed
```

---

## Concurrency Comparison

### Java

Java uses a shared-memory concurrency model. Multiple threads access shared objects directly, requiring synchronization mechanisms such as synchronized methods and thread-safe collections. ExecutorService is used to manage worker threads efficiently.

### Go

Go uses a message-passing concurrency model. Goroutines communicate through channels, reducing the amount of explicit synchronization required. WaitGroups and Mutexes are used to coordinate execution and protect shared resources.

---

## Error Simulation

Task 10 intentionally generates a processing error to demonstrate exception and error handling capabilities.

This demonstrates:

* Error detection
* Error logging
* Fault tolerance
* Continued execution after failure

As a result, 19 tasks complete successfully while the application continues running normally.

---

## Requirements Satisfied

* Shared Resource Queue
* Worker Threads / Goroutines
* Task Processing Delay
* Synchronization
* Concurrency Management
* Deadlock Prevention
* Safe Thread Termination
* Exception Handling
* Error Logging
* File Output
* Shared Resource Protection

---

## Author

Umama Azhar

Data Processing System Assignment
Java and Go Concurrent Programming
