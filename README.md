1. Download and install 5.6.23 server on Windows. Change the connection user/password in
   src\main\resources\application.properties
   
2. Download Spring Suite Tool and unzip.
   http://spring.io/tools/sts

3. Install JAVA SDK 7.
   http://www.oracle.com/technetwork/java/javase/downloads/index.html
   
4. Clone the project dutyApp and import into STS.

5. 'CREATE DATABSE DUTYAPP' is a must;
   'CREATE TABLE' is not must; if table does not exist, Spring & Hibernate will help to create them.
   
6. Run Maven->Update Project, then Run As -> Spring Boot App.

API:
POST /api/escalationPolicys  -> add a new escalationPolicy
GET  /api/escalationPolicys  -> get all escalationPolicys
GET  /api/escalationPolicys/:id  -> get a escalationPolicy
PUT  /api/escalationPolicys/:id  -> update a escalationPolicy
DELETE /api/escalationPolicys/:id  -> delete a escalationPolicy

POST /api/escalationPolicys/:policyId/policyRules  -> append a new policy rule
GET  /api/escalationPolicys/:policyId/policyRules  -> get all policy rules
GET  /api/escalationPolicys/:policyId/policyRules/:id -> get one policy rule
PUT  /api/escalationPolicys/:policyId/policyRules/  -> update to new policy rules
PUT  /api/escalationPolicys/:policyId/policyRules/:id -> update one policy rule
DELETE  /api/escalationPolicys/:policyId/policyRules/:id -> delete a policy rule

-------

POST /api/incidents -> Create a new incident
PUT  /api/incidents/:id -> Updates an existing incident
GET  /api/incidents -> get all the incidents
GET  /api/incidents/:id -> get the "id" incident
PUT  /api/incidents/:id/resolve -> Resolve an existing incident
PUT  /api/incidents/:id/acknowledge -> acknowledge an existing incident
DELETE  /api/incidents/:id -> delete the "id" incident

------
POST  /api/services -> Create a new service
PUT  /api/services -> Updates an existing service.
GET  /api/services -> get all the services.
GET  /api/services/:id -> get the "id" service.
DELETE  /api/services/:id -> delete the "id" service.

------

POST  /api/alerts -> Create a new alert.
PUT  /api/alerts/:id -> Updates an existing alert.
GET  /api/alerts -> get all the alerts.
GET  /api/alerts/:id -> get the "id" alert.
DELETE  /api/alerts/:id -> delete the "id" alert.
