# READ FILE


## Requirement

- Java Version 8

## Run Locally

1. Before running, Configuration credential in file "application.properties" for username and password email.
2. The email address used to send and receive email is in the file "ReadFileController.java" line 85, 92, 103. you can change the email as you wish
3. Compile and install all modules:
    * Online mode (use it for the first time to download dependencies):  
      `mvn clean install`
    * (Optional) or use the Offline mode, if you already have all dependencies in your Maven local repository:  
      `mvn -o clean install`
4. Run the application:  
   `java -jar .\target\read-file-0.0.1-SNAPSHOT.jar`
5. Access the application using this URL (it will also generate the database structure) with a Postman or other application (already included for the collection API):
    * input : [http://localhost:8080/input](http://localhost:8080/input) (POST)
        * check in Body (file : upload file with extension .txt)

## Authors

- [@ariabdulmajid](https://www.github.com/ariabdulmajid)
 

