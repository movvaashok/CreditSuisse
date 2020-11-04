# CreditSuisse
Server Log Analysis
# Building Jar
Maven is used as build tool in this application
1) Clone the project into your computer using (https://github.com/movvaashok/CreditSuisse.git)
2) Navigate to "LogAnalysis" folder
3) Open command prompt and run the command **mvn package** ( Maven has to be installed in system to execute **mvn package** command)
   maven will run unit tests and creates a jar for the project in "target" folder
4) CreditSuisseLogAnalyser.jar will be created.
# Executing Jar
1) The jar file needs some configuration deatils, 
        1) path for server log file
        2) database connection string 
        3) database table name.
    **A sample configuration file has been provided appConfig.json**
2) The Server Log file is assumed to have an **Array of JSON Objects** parsing code is written for the same, however it can be changed as per requirement.
  **A sample input file has been added as serverlogs.txt**
3) Once configuration details are set run the jar using **java -jar CreditSuisseLogAnalyser.jar appConfig.json**

# Results
1. Logs are written to app.log file
2. Any event that took long time will be printed on console and time taken by the event.
