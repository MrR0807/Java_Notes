# JDBC API

The JDBC API provides a standard database-independent interface to interact with any tabular data source. 

Using the JDBC API to access data in a database hides the implementation differences that exist in different types of databases. It achieves database transparency by defining most of its API using interfaces and letting the database vendors (or any third-party vendors) provide the implementations for those interfaces. **The collection of the implementation classes that is supplied by a vendor to interact with a specific database is called a JDBC driver.**

## Connecting to a Database

Here are the steps that you need to follow to connect to a database:
* Obtain the JDBC driver and add it to the CLASSPATH environment variable on your machine.
* Register the JDBC driver with the DriverManager.
* Construct a connection URL.
* Use the getConnection() static method of DriverManager to establish a connection.

### Constructing a Connection URL

A database connection is established using a connection URL. The format of a connection URL is dependent upon the DMBS and a JDBC driver. There are three parts of a connection URL:

    <protocol>:<sub-protocol>:<data-source-details>
    
The <protocol> part is always set to jdbc. The <sub-protocol> part is vendor-specific. The <data-source-details> part is DBMS specific that is used to locate the database. In some cases, you can also specify some connection properties in this last part of the URL. Examples:
  
    jdbc:derby://192.168.1.3:1527/beginningJavaDB;create=true
    
    jdbc:oracle:thin:@localhost:1521:chanda
    
As always, the protocol part is jdbc. The sub-protocol part is oracle:thin, which identifies the Oracle Corporation as the vendor, and the type of the driver it will use, which is thin. The data source details part is @localhost:1521:chanda. Details in order: IP address of the machine; port number; Oracle's instance name.

