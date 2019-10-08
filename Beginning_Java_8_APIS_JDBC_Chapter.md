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
    
As always, the protocol part is jdbc. The sub-protocol part is **oracle:thin**, which identifies the Oracle Corporation as the vendor, and the type of the driver it will use, which is thin. The data source details part is @localhost:1521:chanda. Details in order: IP address of the machine; port number; Oracle's instance name.

### Establishing the Database Connection

DriverManager::getConnection() method takes a connection URL, a user id, a password, and any number of name-value pairs using a java.util.Properties object. The getConnection() method is overloaded:
* static Connection getConnection(String url) throws SQLException
* static Connection getConnection(String url, Properties info) throws SQLException
* static Connection getConnection(String url, String user, String password) throws SQLException

**A ResultSet object is automatically closed when the Statement object that generated it is closed, re-executed, or used to retrieve the next result from a sequence of multiple results.**

## Setting the Auto-Commit Mode

When you connect to a database, the auto-commit property for the Connection object is set to true by default. **If a connection is in an auto-commit mode, a SQL statement is committed automatically after its successful execution.** If a connection is not in an auto-commit mode, you must call the commit() or rollback() method of the Connection object to commit or rollback a transaction. Typically, you disable the auto-commit mode for a connection in a JDBC application, so your application logic controls the final outcome of the transaction. To disable the auto-commit mode, you need to call the setAutoCommit(false) on the Connection object after connection has been established.

## Transaction Isolation Level

In a multi-user database, you will often come across the following two terms:
* Data concurrency
* Data consistency

Data concurrency refers to the ability of multiple users to use the same data concurrently. Data consistency refers to the accuracy of the data that is maintained when multiple users are manipulating the data concurrently. **A database maintains data consistency using locks and by isolating one transaction from another. How much a transaction is isolated from another transaction depends on the desired level of data consistency.**

### Dirty Read

In a dirty read, a transaction reads uncommitted data from another transaction:
* Transaction A inserts a new row in a table and it has not committed it yet.
* Transaction B reads the uncommitted row inserted by the transaction A.
* Transaction A rollbacks the changes.
* At this point, transaction B is left with data for a row that does not exist.

### Non-Repeatable Read

In a non-repeatable read, when a transaction re-reads the data, it finds that the data has been modified by another transaction that has been already committed:
* Transaction A reads a row.
* Transaction B modifies or deletes the same row and commits the changes.
* Transaction A re-reads the same row and finds that the row has been modified or deleted.

### Phantom Read

In a phantom read, when a transaction re-executes the same query, it finds more data that satisfies the query:
* Transaction A executes a query (say Q) and finds X number of rows matching the query.
* Transaction B inserts some rows that satisfy the query Q criteria and commits.
* Transaction A re-executes the same query (Q) and finds Y number of rows (Y > X) matching the query.

The ANSI SQL-92 standard defines four transaction isolation levels in terms of the above-described three situations for data consistency. The four transaction isolation levels are as follows:

| Isolation Level  | Dirty Read    | Non-Repeatable Read | Phantom Read |
| ---------------- | ------------- | ------------------- | ------------ |
| Read Uncommitted | Permitted     | Permitted           | Permitted    |
| Read Committed   | Not Permitted | Permitted           | Permitted    |
| Repeatable Read  | Not Permitted | Not Permitted       | Permitted    |
| Serializable     | Not Permitted | Not Permitted       | Not Permitted| 



























