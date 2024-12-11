# Event Booking System

## A Real-Time Event Ticketing System with Advanced Producer-Consumer Implementation using Concurrent Programming

This project features a Spring Boot backend, an Angular frontend, and MySQL as the database. The project aims to:
1. Solve the producer-consumer problem
2. Implement multi-threading and concurrency
3. Utilize core Object-Oriented Programming and Design principles
4. Consist of a command-line interface component and a web application that run separately

## Tech Stack
Backend: Spring Boot (Java)
Frontend: Angular (TypeScript)
Database: MySQL Workbench

## IDE:
Backend: IntelliJ IDEA Community Edition
Frontend: Visual Studio Code

## Prerequisites:
Make sure you have an internet connection and the following installed:

Java (JDK 17 or later)

Node.js (LTS version)

Angular CLI

bash

npm install -g @angular/cli

MySQL Server and MySQL Workbench

Maven (comes with IntelliJ IDEA for Spring Boot projects)

IntelliJ IDEA (version 2023.3.1 or later) 

Visual Studio Code

## Setup Instructions

### 1. Backend (Spring Boot)

Clone the Repository or download zip file and extract it

Steps to clone repository:
1. Create a folder and open the folder in Command Prompt

2. Type the command:  git clone [<repository_url>](https://github.com/coding-genius01/RealTimeEventTicketingSystem.git)

Open the folder you created or extracted and select RealTimeEventTicketingSystem folder

Open it as an IntelliJ IDEA project by right clicking on it or by starting up IntelliJ IDEA and selecting Open Folder

Go to the file on the path RealTimeEventTicketingSystem/src/main/resources/application.properties

Set the value for "spring.jpa.hibernate.ddl-auto" property from "update" to "create" since you're running it for the first time

### 2. Frontend (Angular)

Install Dependencies

Open the folder you created or extracted and select RealTimeEventTicketingSystemUI folder

Open it as a VS Code project by right clicking on it or by starting up Visual Studio Code and selecting Open Folder

Open a new terminal window in VS Code and run the command "npm install"

### 3. Database (MySQL Workbench)

Setup Local Database

Launch MySQL Workbench and create a local instance.

Make sure the MySQL instance is running and accessible via the credentials (url,username and password)

Create a new database by clicking the Create New Schema option.

Go to IntelliJ IDEA and update the values for "spring.datasource.url" with "jdbc:mysql://{your_local_instance_url}/{name_of_schema}", "spring.datasource.username" and "spring.datasource.password" properties according to the credentials for your local instance in MySQL Workbench.

## Start up the system

Start the backend (Spring Boot) by going to the class RealTimeEventTicketingSystem/src/main/java/com/thilina_jayasinghe/w2052199/RealTimeEventTicketingSystem/RealTimeEventTicketingSystemApplication.java

Run the application for the first time and check if the tables are created for configuration, vendors, customers and tickets in MySQL Workbench

After the tables are created, stop the application class.

Set the value for "spring.jpa.hibernate.ddl-auto" property from "create" to "update" in application.properties

Rerun the system

Start frontend (Angular) by entering the command "ng serve" in the terminal.

Open http://localhost:4200 to access the application.

## Usage

Perform operations such as:

1. Initialize configuring the system settings.
2. Register the vendors.
3. Register the customers.
4. Go to the Logs section.
5. Start the system.
6. View real-time logs or visit the TicketPool section for more info.
7. Go to Tickets to view purchased tickets catalogue.

The backend API runs at http://localhost:9090.

## Project Structure

### Backend

RealTimeEventTicketingSystem/src/main/java/com/thilina_jayasinghe/w2052199/RealTimeEventTicketingSystem: Source code.

RealTimeEventTicketingSystem/src/main/resources: File to configure application -> application.properties

### Frontend

realTimeEventTicketingUI/src/app: Angular components, services, and modules.

## API Endpoints

|  Endpoint                       |  Method   |          Description                      |
|---------------------------------|-----------|-------------------------------------------|
|api/save/config                  |   POST    |  Save configuration details               |
|api/save/vendor                  |   POST    | Save new vendor                           |
|api/get/vendor                   |   GET     |  Get list of saved vendors in database    |
|api/delete/vendor/{vendorId}     |   DELETE  |  Delete a specific vendor from database   |
|api/save/customer                |   POST    |  Save new customer                        |
|api/get/customer                 |   GET     |  Get list of saved customers in database  |
|api/delete/customer/{customerId} |   DELETE  |  Delete a specific customer from database |
|api/save/ticket                  |   POST    |  Save a ticket in database                |
|api/get/tickets                  |   GET     |  Get list of saved tickets in database    |
|api/start                        |   POST    |  Start threads                            |
|api/stop                         |   POST    |  Stop running threads                     |
|api/reset                        |   POST    |  Clear logs                               |


## WebSocket Connection

WebSocket URL: ws://localhost:9090/ws-native
Purpose: Real time updates on TicketPool status and logs

### Supported Messages

| MessageType   | Direction | Description                     |
|---------------|-----------|---------------------------------|
| status        | Receive   | Updates on ticketpool status    |
| logs          | Receive   | Updates log messages            |
|               | Send      | Messages sent to backend server |


## Troubleshooting

Backend not starting?

Verify the database credentials in application.properties.

Ensure MySQL is running locally on the specified port.

Frontend issues?

Check if node_modules is installed by running npm install.

Ensure the Angular CLI is installed globally.
