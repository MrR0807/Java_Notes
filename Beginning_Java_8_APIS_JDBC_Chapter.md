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

|Isolation Level  |Dirty Read    |Non-Repeatable Read |Phantom Read |
| --------------- | ----------- | ------------------- | ------------ |
| Read Uncommitted| Permitted   | Permitted           | Permitted    |
| Read Committed  | Not Permitted | Permitted         | Permitted    |
| Repeatable Read | Not Permitted | Not Permitted     | Permitted    |
| Serializable    | Not Permitted | Not Permitted     | Not Permitted| 

    // Get a Connection object
    Connection conn = get a connection object...;
    // Set the transaction isolation level to read committed
    conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

You will be using three sets of methods while working with data in JDBC programs: getXxx(), setXxx(), and updateXxx(), where Xxx indicates a data type such as int, String, Date, etc. These methods are found in many interfaces that are used in this chapter such as PreparedStatement, ResultSet, etc.

## Knowing About the Database

The same database feature may be supported differently, or not supported at all, by different DBMSs. An instance of the **DatabaseMetaData interface** gives you detailed information about the features supported by a DBMS through the JDBC driver.

# Executing SQL Statements

Based on the type of work that a SQL statement performs in a DBMS, it can be categorized as follows:

* A Data Definition Language (DDL) Statement: Examples of DDL statements are CREATE TABLE, ALTER TABLE, etc.
* A Data Manipulation Language (DML) Statement: Examples of DML statements are SELECT, INSERT, UPDATE, DELETE, etc.
* A Data Control Language (DCL) Statement: Examples of DCL statements are GRANT and REVOKE.
* A Transaction Control Language (TCL) Statement: Example of TCL statements are COMMIT, ROLLBACK, SAVEPOINT, etc.

Java uses three different interfaces to represent SQL statements in different formats:
* Statement
* PreparedStatement
* CallableStatement

        interface CallableStatement extends PreparedStatement
        interface PreparedStatement extends Statement

Using a **PreparedStatement** object is preferred over using a **Statement**:
* Using a PreparedStatement eliminates the threat of a SQL injection.
* The PreparedStatement improves the performance of your JDBC application by compiling a statement once and executing it multiple times.
* A PreparedStatement lets you use Java data types to supply values in a SQL statement instead of using strings.

## Results of Executing a SQL Statement

When you execute a SQL statement, the DBMS may return zero or more results. The results may include update counts (number of records affected in the database) or result sets (a group of records).
When you execute a SELECT statement, it returns a result set. When you execute an UPDATE or DELETE statement, it returns an update count, which is the number of records affected in the database by the SQL.
**When you execute a stored procedure, it may return multiple update counts as well as multiple result sets.**

## Using the Statement Interface

You can use a Statement to execute any kind of SQL statement:
* boolean execute(String SQL) throws SQLException. Typically, it is used to execute a SQL statement that does not return a result set, such as a DDL statement like CREATE TABLE.
* int executeUpdate(String SQL) throws SQLException. The executeUpdate() method is used to execute a SQL statement that updates the data in the database such as INSERT, UPDATE and DELETE statements. It returns the number of rows affected in the database by the execution of the statement.
* ResultSet executeQuery(String SQL) throws SQLException. The executeQuery() method is especially designed to execute a SQL statement that produces one and only one result set. It is best suited for executing a SELECT statement.

Example:

        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            // Create a SQL string
            String SQL = "create table person ( " +
                "person_id integer not null, " +
                "first_name varchar(20) not null, " +
                "last_name varchar(20) not null, " +
                "gender char(1) not null, " +
                "dob date, " +
                "income double," +
                "primary key(person_id))";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        }
        finally {
            JDBCUtil.closeStatement(stmt);
        }
        
        // Commit the transaction
        JDBCUtil.commit(conn);
        System.out.println("Person table created.");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            JDBCUtil.rollback(conn);
        }
        finally {
            JDBCUtil.closeConnection(conn);
        }

## Using the PreparedStatement Interface

It precompiles the SQL statement provided DBMS supports a SQL statement precompilation. It reuses the precompiled SQL statement if the statement is executed multiple times. It lets you prepare a SQL statement, which is in a string format, using placeholders for input parameters.
A question mark in a SQL string is a placeholder for an input parameter whose value will be supplied before the statement is executed. Suppose you want to use a PreparedStatement to insert a record in the person table. Your SQL statement in a string format would be as follows:

    String sql = "insert into person " +
        "(person_id, first_name, last_name, gender, dob, income) " +
        "values " +
        "(?, ?, ?, ?, ?, ?)";
        
You can create a PreparedStatement using the prepareStatement() method of the Connection object.

    String sql = "your sql statement goes here";
    Connection conn = JDBCUtil.getConnection();
    // Obtain a PreparedStatement for the sql
    PreparedStatement pstmt = conn.prepareStatement(sql);

The next step is to supply the values for the placeholders one-by-one using a setXxx() method of the PreparedStatement interface, where Xxx is the data type of the placeholder.

    pstmt.setInt(1, 301); // person_id
    pstmt.setString(2, "Tom"); // first name
    pstmt.setString(3, "Baker"); // last name
    pstmt.setString(4, "M"); // gender
    java.sql.Date dob = java.sql.Date.valueOf("1970-01-25");
    pstmt.setDate(5, dob); // dob
    pstmt.setDouble(6, 45900); // income

Now it is time to send the SQL statement with the values for the placeholders to the database. You execute a SQL statement in a PreparedStatement using one of its execute(), executeUpdate(), and executeQuery() methods. **These methods take no arguments. Recall that the Statement interface has the same methods, which take SQL strings as their arguments.**

    // Execute the INSERT statement in pstmt
    pstmt.executeUpdate();
    
If you want to clear the values of all placeholders, you can use the clearParameters() method of the PreparedStatement interface. When you are done with executing the statement in a PreparedStatement object, you need to close it using its close() method.


    public class PreparedStatementTest {
        public static void main(String[] args) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = JDBCUtil.getConnection();
                pstmt = getInsertSQL(conn);
                // Need to get dob in java.sql.Date object
                Date dob = Date.valueOf("1970-01-01");
                // Insert two person records
                insertPerson(pstmt, 401, "Sara", "Jain", "F", dob, 0.0);
                insertPerson(pstmt, 501, "Su", "Chi", "F", null, 10000.0);
                // Commit the transaction
                JDBCUtil.commit(conn);
                System.out.println("Updated person records successfully.");
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                JDBCUtil.rollback(conn);
            }
            finally {
                JDBCUtil.closeStatement(pstmt);
                JDBCUtil.closeConnection(conn);
            }
        }
        public static void insertPerson(PreparedStatement pstmt, int personId, String firstName, String lastName, String gender, Date dob, double income) throws SQLException {
            // Set all the input parameters
            pstmt.setInt(1, personId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, gender);
            // Set the dob value properly if it is null
            if (dob == null) {
                pstmt.setNull(5, Types.DATE);
            }
            else {
                pstmt.setDate(5, dob);
            }
            pstmt.setDouble(6, income);
            // Execute the statement
            pstmt.executeUpdate();
        }
        public static PreparedStatement getInsertSQL(Connection conn) throws SQLException {
            String SQL = "insert into person " +
            "(person_id, first_name, last_name, gender, dob, income) " +
            "values " +
            "(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            return pstmt;
        }
    }

## CallableStatement Interface

It is used to call a SQL stored procedure or a function in a database. You can also call a stored procedure or a function using the Statement object. However, using a CallableStatement is the preferred way.

### Executing a CallableStatement

Before you execute a stored procedure, you need to prepare a CallableStatement by calling the prepareCall() method of the Connection object. The prepareCall() method accepts a SQL string as a parameter. The following snippet of code shows how to prepare a CallableStatement:

    Connection conn = JDBCUtil.getConnection();
    String SQL = "{call myProcedure}";
    CallableStatement cstmt = conn.prepareCall(SQL);

# Processing Result Sets

A set of rows obtained by executing a SQL SELECT statement in a database is known as a result set. JDBC lets you execute a SELECT statement in the database and process the returned result set in the Java program using an instance of the ResultSet interface.

## What Is a ResultSet?

When you execute a query (a SELECT statement) in a database, it returns the matching records in the form of a result set. You can consider a result set as a data arranged in rows and columns.

A ResultSet object maintains a cursor, which points to a row in the result set. It works similar to a cursor object in database programs. You can scroll the cursor to a specific row in the result set to access or manipulate the column values for that row. The cursor can point to only one row at a time. The row to which it points at a particular point in time is called the ***current row***. 

The following three properties of a ResultSet object need to be discussed before you can look at an example:
* Scrollability
* Concurrency
* Holdability

**Scrollability determines the ability of the ResultSet to scroll through the rows. By default, a ResultSet is scrollable only in the forward direction.** You can also create a ResultSet that can scroll in the forward as well as the backward direction. I will call this ResultSet a bidirectional scrollable ResultSet. A bidirectional scrollable ResultSet has another property called **update sensitivity.** It determines whether the changes in the underlying database will be reflected in the result set while you are scrolling through its rows. **A scroll sensitive ResultSet shows you changes made in the database, whereas a scroll insensitive one would not show you the changes made in the database after you have opened the ResultSet.**

The following three constants in the ResultSet interface are used to specify the scrollability of a ResultSet:
* **TYPE_FORWARD_ONLY**: Allows a ResultSet object to move only in the forward direction.
* **TYPE_SCROLL_SENSITIVE**: Allows a ResultSet object to move in the forward and backward directions.
* **TYPE_SCROLL_INSENSITIVE**: Allows a ResultSet object to move in the forward and backward directions.

**Concurrency refers to its ability of the ResultSet to update data. By default, a ResultSet is read-only and it does not let you update its data.** If you want to update data in a database through a ResultSet, you need to request an updatable result set from the JDBC driver. The following two constants in the ResultSet interface are used to specify the concurrency of a ResultSet:
* **CONCUR_READ_ONLY**: Makes a result set read-only.
* **CONCUR_UPDATABLE**: Makes a result set updatable.

**Holdability refers to the state of the ResultSet after a transaction that it is associated with has been committed.** A ResultSet may be closed or kept open when the transaction is committed. **The default value of the holdability of a ResultSet is dependent on the JDBC driver.** The holdability of a ResultSet is specified using one of the following two constants defined in the ResultSet interface:
* **HOLD_CURSORS_OVER_COMMIT**: Keeps the ResultSet open after the transaction is committed.
* **CLOSE_CURSORS_AT_COMMIT**: Closes the ResultSet after the transaction is committed.

## Getting a ResultSet

Here is a typical way to get a forward-only scrollable result set:

    Connection conn = JDBCUtil.getConnection();
    Statement stmt = conn.createStatement();
    String sql = "select person_id, first_name, last_name, dob, income " +
    "from person";
    // Execute the query to get the result set
    ResultSet rs = stmt.executeQuery(sql);
    // Process the result set using the rs variable


The returned ResultSet from the executeQuery() method is already open, and it is ready to be looped through to get the associated data. **In the beginning, the cursor points before the first row in the result set. You must move the cursor to a valid row before you can access the column’s values for that row.** The next() method of the ResultSet is used to move the cursor to the next row. When the next() method is called for the first time, it moves the cursor to the first row in the result set.

It is very important to consider the return value of the next() method. It returns a boolean value. It returns true if the cursor is positioned to a valid row. Otherwise, it returns false.

When a cursor is positioned after the last row in a forward-only scrollable ResultSet object, you cannot do anything with it, except close it using its close() method. However, things are different for a bidirectional scrollable ResultSet, which lets you iterate through the rows as many times as you want.

The following four methods of the ResultSet interface let you know if the cursor is before the first row, on the first row, on the last row, or after the last row:
* boolean isBeforeFirst() throws SQLException
* boolean isFirst() throws SQLException
* boolean isLast() throws SQLException
* boolean isAfterLast() throws SQLException














































