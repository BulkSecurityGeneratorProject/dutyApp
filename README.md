1. Download and install 5.6.23 server on Windows. Change the connection user/password in
   src\main\resources\application.properties
   
2. Download Spring Suite Tool and unzip.
   http://spring.io/tools/sts

3. Install JAVA SDK 7.
   http://www.oracle.com/technetwork/java/javase/downloads/index.html
   
4. Clone the project dutyApp and import into STS.

5. 'CREATE DATABSE DUTYAPP' is a must;
   'CREATE TABLE' is not must; if table does not exist, Spring & Hibernate will help to create them.
   MySQL setting is in src\main\resources\config\application-dev.yml
   
6. Run Maven->Update Project, then Run As -> Spring Boot App.
