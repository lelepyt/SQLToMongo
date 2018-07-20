# SQLToMongo
Simple tool which converts SQL select queries to Mongo results. 

## Set up
* Clone the repository.
* Import the project to IDE as Maven project.
* Update ```../resources/config.properties``` with connection data to MongoDB. 
**host** - the host to your MongoDB, 
**port** - the port to your MongoDB,
**collection** - the  database you will use and which has the collection.

## How to run from IDE
* Run the Main class which has the main method. 
* Enter select SQL and hit enter. For example

![alt text](https://i.imgur.com/JzwlQZi.png)


## How to run CLI (Ubuntu)
* Run maven ```mvn clean install```
* Copy from jar file target/sqltomongo-1.0-jar-with-dependencies.jar to your folder.
* Open terminal in your folder and run ```java -jar sqltomongo-1.0-jar-with-dependencies.jar``` For example

![alt text](https://i.imgur.com/B8RaFly.png)

