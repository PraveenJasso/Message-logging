# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Requirements
* Java 1.8
* Maven

### Steps

1. Clone the project from Git
2. Import as maven project in your IDE
5. Run the Spring application

Note : Once we started the sprint boot application automatically batch will trigger to clean the data

### APIs

1. POST API:
	http://localhost:8080/
	Body Content : {"name":"Test","logId":1,"message":"test"}
2. Read API:
	http://localhost:8080/
3. Patch API:
	http://localhost:8080/set_max_age/30
4. Read particular log with logid
    http://localhost:8080/
    
please use the below command to run the jar file
**************************************************
java -jar Message-logging-0.0.1-SNAPSHOT.jar --cron.expression="0 */1 * * * ?" 