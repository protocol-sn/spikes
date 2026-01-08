## Trying out integration of Cassandra and Micronaut

Much of the information pulled from:
https://micronaut-projects.github.io/micronaut-cassandra/latest/guide/

### How to run
#### Test run:
1. Ensure Docker is running locally
2. Run the unit tests
3. TableOneRepositoryTest.testInsertRecord_happyPath exercises both methods on the repository class
#### Running live
1. Ensure Docker is running locally
2. From the root directory of this project run `docker compose up` to start the Cassandra instance
   1. It's done when you see several lines that read something like 
   ```
   cassandra  | INFO  [NonPeriodicTasks:1] 2025-12-08 22:24:29,214 BigFormat.java:231 - Deleting sstable: /opt/cassandra/data/data/system/local-7ad54392bcdd35a684174e047860b377/nb-2-big
    ```
   2. On the first startup the final line will actually be creation of the superuser role
3. Noting autocreation of keyspaces
   1. If you are running this exactly as you pulled it, flyway will create the SPIKE_KEYSPACE keyspace because this is set as the `default-schema` value.
      1. This will be created with a replication factor of 1, which is not necessarily what we want.
      2. The first flyway migration will alter this, in this case to 4, to demonstrate that this is possible.
   2. A different `default-schema` field is commented out in application.yaml and a CREATE KEYSPACE command is commented out in the first migration file.
      1. If you swap these out, flyway will create a SPIKE_DEFAULT keyspace tha will exist only to allow it to connect and hold the flyway migration table.
      2. It will then run the migrations, which create our desired schema.
4. In the root directory for this project run `./gradlew run` and start up the server.
   1. The endpoint to insert a record is a POST `/`. Request body is below and  the response will include the created object including the generated ID
   ```
   {
    "textField": "some text", //single string
    "arrField": [1, 2, 3], //array of ints
    "tupleFieldKey": 43, //single int
    "tupleFieldValue": 29, //single int
    "tupleList": {"5": "kv"} //Map of numeric strings to strings
    }
   ```
   2. The endpoint to retrieve a record is `/{id}`, with the ID being in the response from the POST

### Observations
The micronaut guide is pretty barebones, but guides for Spring Boot indicate that Hibernate and JPA *can* support Cassandra, but I opted to avoid that her because I was interested in the collection data types. Hibernate *can* sometimes support these, but I felt like I was fighting Hibernate to get it to work. I opted to use PreparedStatements here, although the Cassandra Java driver also comes with a query builder.

Cassandra overall has some pretty interesting differences from, and limitations compared to, an RDBMS. Obviously a lack of joins is expected, but I was shocked to find that there is no OR clause in Cassandra. I had intended for this spike to include a search endpoint with optional parameters, but found that I couldn't with the PreparedStatements because I could not use the old `OR true = true` trick to ignore nulled out parameters.