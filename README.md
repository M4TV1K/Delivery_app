# Delivery_app
Simple Delivery app which allows to create and track delivery processes of packages

## UML
There is MAS_12c_Matviichuk_Roman_s17347.pdf file which contains UML Diagrams such as Use Case Diagram, Analytical Class Diagram, Design Class Diagram, Activity Diagram, State Diagram and Sequence Diagram. Diagrams describe the whole structure of the project including workflow and the way models (entities) related to each other, because there is a lot of inheritance implemented not in a direct manner.

## Implementation

### Backend
It is written on Java Spring. Java Spring is being used in order to generate and communicate with the database.  
As the Hibernate allows to use CodeFirst approach for the database, the generated tables are being stored and managed in MySQL server (I am accessing it via MySQL Workbench personally)

### Frontend
ThymeLeaf templates are being used here with a bit of CSS
