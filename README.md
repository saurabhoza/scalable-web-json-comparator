**Scalable Web Challenge** [![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)
===
# **Problem Statement**

 - Provide 2 http endpoints that accepts JSON base64 encoded binary data on both endpoints
   - <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
 - The provided data needs to be diff-ed and the results shall be available on a third end point
   - <host>/v1/diff/<ID>
  - The results shall provide the following info in JSON format
    - If equal return that
     - If not of equal size just return that
     - If of same size provide insight in where the diffs are, actual diffs are not needed. So mainly offsets + length in the data

# **Assumptions**

### **1. How our Request and Response Body would look like ?**

The input will be a JSON format which will contain a Base64 encoded string. Below are sample Request and Response body :

##### Request Body:
````
{
      "data" : "dGVzdGluZyB0aGUgYmFzZTY0"
}
````
##### Response Body:
````
{
       "message" : "Json Data on the Left side saved successfully"
}
````
### **2. What are we going to do with the ID?**

We will save the ID and its corresponding left and right side into database. When client will call the <host>/v1/diff/<ID>, We will use the same id to retrieve the left and right side of data from the database and return the result.

# **Technologies used**

- Java 8
- Spring Boot
- Spring Data
- Spring JPA
- H2 Database
- Maven
- Swagger 2
- Junit &amp; Mockito

# **Build Requirements**
Before being able to build he code you need:
- Maven 3.3+
- Java 1.8

# **How to run Unit Test and Integration Test?**

After you have installed Building requirements tool:
````sh
cd scalable-web-json-comparator/scalable-web
mvn test
````

# **How to generate Java Docs?**

````sh
cd scalable-web-json-comparator/scalable-web
mvn javadoc:javadoc
````
This will create java docs at below location : 
````sh
cd scalable-web-json-comparator/scalable-web/target/site/apidocs/
````

# **How to run code locally?**
````
git clone https://github.com/saurabhoza/scalable-web-json-comparator.git

cd scalable-web-json-comparator/scalable-web

mvn package

java -jar target/scalable-web-1.0.jar
````
# **How to test manually?**

### **Endpoints :**
- POST /v1/diff/{id}/left
- POST /v1/diff/{id}/right
- GET /v1/diff/{id}

### **Save left side json :**
**Request Body**
```sh
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "data": "dGVzdGluZyB0aGUgYmFzZTY0" \ 
 }' 'http://localhost:8080/v1/diff/1/left'
 ````
**Response Body**
````sh
{
  "message": "Json Data on the Left side saved successfully"
}
````
### **Save right side json :**
**Request Body**
```sh
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "data": "dGVzdGluZyB0aGUgYmFzZTY0" \ 
 }' 'http://localhost:8080/v1/diff/1/right'
 ````
**Response Body**
````sh
{
  "message": "Json Data on the Right side saved successfully"
} 
````
### **Get Difference from bothe the JSON :**
**Request Body**
```sh
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/v1/diff/1'
 ````
**Response Body**
````sh
{
  "message": "Left and Right side of Json data is equal."
}
````

# **Rest Documentation with Swagger**

- Default host: [http://localhost:8080](http://localhost:8080/)
- This project is using Swagger2 documentation:

http://localhost:8080/v2/api-docs - JSON format of rest documentation
http://localhost:8080/swagger-ui.html - UI representation of rest documentation

# **Achieves**

1. Solution was created;
2. Unit tests and Integration tests were created with 97% of code coverage. Code coverage path : [https://github.com/saurabhoza/scalable-web-json-comparator/blob/master/Documents/code-coverage-reports/index.html](https://github.com/saurabhoza/scalable-web-json-comparator/blob/master/Documents/code-coverage-reports/index.html)


3. Project is built with no Sonar issues.

# **Suggestions for improvement**

- **Dockerizing**  would be nice, it allows us to test our application across to multiple instances.
- **Distributed Cache(Redis)** would need if we run application across to multiple instances.
- **Authentication** will restrict unauthorizes access to this API.
