# Recipes

I implemented it with Spring Boot, Spring Data, Lombook, PostgreSQL Driver.

I used a PostgreSQL database to persist the information.

The firsts configuration is the database - Postgresql
To run this app, create the database in Postgresql with the name: recipes_abn_amro

Add configuration application-local.properties file like following below.

# Database
```
#spring.jpa.database=POSTGRESQL
spring.datasource.url=jdbc:postgresql://localhost:5432/recipes_abn_amro
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.generate-ddl=true
spring.jpa.properties.hibernate.jdbc.lob.non_contexual_creation=true
spring.jpa.properties.jakarta.persistence.sharedCache.mode=NONE
```
UserName and Password can be used as according to your configuration of the PostgreSQL server. 
For my configuration I used the default username and password.
