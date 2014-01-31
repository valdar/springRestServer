Spring REST Server with db/queue XA Transaction
===============================================
This is one of a set of 3 example projects (valdar/springJmsConsumer , valdar/springRestClient) that I made for an interview.  
It is a simple spring REST server that use the String Id posted to write a row on a db and send a message on a queue using a XA transaction.
The distribute XA transaction manager used is Atomikos.

To build the project:
```sh
mvn package
```

To run it:
```sh
mvn exec:exec
```

Whit this command it'll use a copy of the property file in the root of the project.  
Otherwise you can copy the jar file created in the target directory, create you own property config file, put all in the same direcorty and run it with:
```sh
java -jar springRestServer.jar
```