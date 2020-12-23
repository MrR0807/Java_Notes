# Table of Content

- [JDBC API](#jdbc-api)
  * [Connecting to a Database](#connecting-to-a-database)
    + [Constructing a Connection URL](#constructing-a-connection-url)
    + [Establishing the Database Connection](#establishing-the-database-connection)
  * [Setting the Auto-Commit Mode](#setting-the-auto-commit-mode)
  * [Transaction Isolation Level](#transaction-isolation-level)
    + [Dirty Read](#dirty-read)
    + [Non-Repeatable Read](#non-repeatable-read)
    + [Phantom Read](#phantom-read)
  * [Knowing About the Database](#knowing-about-the-database)
- [Executing SQL Statements](#executing-sql-statements)
  * [Results of Executing a SQL Statement](#results-of-executing-a-sql-statement)
  * [Using the Statement Interface](#using-the-statement-interface)
  * [Using the PreparedStatement Interface](#using-the-preparedstatement-interface)
  * [CallableStatement Interface](#callablestatement-interface)
    + [Executing a CallableStatement](#executing-a-callablestatement)
- [Processing Result Sets](#processing-result-sets)
  * [What Is a ResultSet?](#what-is-a-resultset)
  * [Getting a ResultSet](#getting-a-resultset)
  * [Bidirectional Scrollable ResultSets](#bidirectional-scrollable-resultsets)
  * [Scrolling Through Rows of a ResultSet](#scrolling-through-rows-of-a-resultset)
  * [Closing a ResultSet](#closing-a-resultset)
  * [Making Changes to a ResultSet](#making-changes-to-a-resultset)
    + [Inserting a Row Using a ResultSet](#inserting-a-row-using-a-resultset)
    + [Updating a Row Using a ResultSet](#updating-a-row-using-a-resultset)
    + [Deleting a Row Using a ResultSet](#deleting-a-row-using-a-resultset)
  * [ResultSetMetaData](#resultsetmetadata)
- [Using RowSets](#using-rowsets)
  * [Creating a RowSet](#creating-a-rowset)
  * [Setting RowSet Connection Properties](#setting-rowset-connection-properties)
  * [Setting a Command for a RowSet](#setting-a-command-for-a-rowset)
  * [Populating a RowSet with Data](#populating-a-rowset-with-data)
  * [Scrolling Through Rows of a RowSet](#scrolling-through-rows-of-a-rowset)
  * [Updating Data in a RowSet](#updating-data-in-a-rowset)
  * [JdbcRowSet](#jdbcrowset)
  * [CachedRowSet](#cachedrowset)
- [Working with a Large Object (LOB)](#working-with-a-large-object-lob)
  * [Retrieving LOB Data](#retrieving-lob-data)
  * [Creating a LOB Data](#creating-a-lob-data)
  * [Batch Updates](#batch-updates)
  * [Savepoints in a Transaction](#savepoints-in-a-transaction)
  * [Using a DataSource](#using-a-datasource)
  * [Enabling JDBC Trace](#enabling-jdbc-trace)
  * [Summary](#summary)

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

Suppose you have the following ResultSet of a query:

    select person_id as "Person ID", first_name, last_name from person


In the ResultSet, the person_id column has a column index of 1, the first_name column has a column index of 2, and the last_name column has a column index of 3. You have specified Person ID as the column label for the person_id column. You have not specified the column labels for the first_name and last_name columns. To get the value of the person_id column, you need to use either getInt(1) or getInt("PERSON ID"). To get the value of the first_name column, you need to use either getString(2) or getString("first_name").

You can get the names of columns in a ResultSet object using the ResultSetMetaData object.

In a ResultSet, when a column has a null value, the getXxx() method returns the default value for the Xxx data type:
* For numeric data types (int, double, byte, etc.), the getXxx() method returns zero when the column has a null value;
* For boolean returns false when the column has a null value;
* For reference returns null.

**If you want to know whether the column value, which you read using a getXxx() method, is null, you need to call the wasNull() method immediately after calling the getXxx() method.**

## Bidirectional Scrollable ResultSets

You can request a JDBC driver for a bidirectional scrollable ResultSet by specifying the scrollability property when you create a Statement, prepare a PreparedStatement, or prepare a CallableStatement. **Not all JDBC drivers support all three types of scrollability properties for a result set.**

Example:

    // Request a bi-directional change insensitive ResultSet
    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    
## Scrolling Through Rows of a ResultSet

**The number of rows a ResultSet will retrieve from the database is JDBC driver-dependent. It may choose to retrieve one row at a time from a database. You can give a hint to the JDBC driver using the setFetchSize(int fetchSize) method.**

For example, calling the next() method of a ResultSet may trigger a fetch from the database. Suppose a ResultSet fetches 10 records at a time. If you call the next() method the first time, it will fetch and cache 10 records and, for nine subsequent calls to its next() method, it will give you rows from its cache. Fetching and caching rows for a ResultSet is dependent on a JDBC driver and the underlying DBMS.

## Closing a ResultSet

You can close a ResultSet object by calling its close() method. A ResultSet object can also be closed implicitly in the following situations:
* When the Statement object that produces the ResultSet object is closed, it automatically closes the ResultSet object.
* When a Statement object is re-executed, its previously opened ResultSet object is closed.
* If a Statement object produces multiple result sets, retrieving the next result set closes the previously retrieved ResultSet.
* If it is a forward-only scrollable ResultSet, a JDBC driver may choose to close it when its next() method returns false as the part of optimization.

## Making Changes to a ResultSet

You can use a ResultSet to perform insert, update, and delete operations on database tables. The concurrency for the ResultSet object must be ResultSet.CONCUR_UPDATABLE in order to perform updates on the ResultSet.

### Inserting a Row Using a ResultSet

So far, you are aware of only two imaginary rows in a result set. They were rows before the first row and after the last row. However, there is one more imaginary row that exists in a ResultSet and that is called an **insert row.** You can think of this row as an empty new row, which acts as a staging area for a new row that you want to insert. You can position the cursor to the insert row using the ResultSet object’s **moveToInsertRow()** method. When the cursor moves to the insert row, it remembers its previous position. You can call the **moveToCurrentRow()** method to move the cursor from the insert row back to the previously current row. So, the first step in inserting a new row is to move the cursor to the insert row.

    // Move the cursor to an insert row to add a new row
    rs.moveToInsertRow();

At this point, a new row has been inserted in the staging area and all columns have undefined values. Define values:

    rs.updateInt("person_id", 501);
    rs.updateString("first_name", "Richard");
    rs.updateString("last_name", "Castillo");
    rs.updateString("gender", "M");

You are not done yet with the new row. You must send the changes to the database before your new row becomes part of the ResultSet. You can send the newly inserted row to the database by calling the insertRow() method of the ResultSet interface as shown:
    
    // Send changes to the database
    rs.insertRow();

**Moving to another row before calling the insertRow() method after calling the moveToInsertRow() method discards the new row.**


    public static void addRow(Connection conn) throws SQLException {
            String SQL = "select person_id, first_name, "
                    + "last_name, gender, dob, income "
                    + "from person";
            Statement stmt = null;
            try {
                stmt = conn.createStatement(TYPE_FORWARD_ONLY, CONCUR_UPDATABLE);
                // Get the result set
                ResultSet rs = stmt.executeQuery(SQL);
                // Make sure your resultset is updatable
                int concurrency = rs.getConcurrency();
                if (concurrency != ResultSet.CONCUR_UPDATABLE) {
                    System.out.println("The JDBC driver does not support updatable result sets.");
                    return;
                }
                // First insert a new row to the ResultSet
                rs.moveToInsertRow();
                rs.updateInt("person_id", 501);
                rs.updateString("first_name", "Richard");
                rs.updateString("last_name", "Castillo");
                rs.updateString("gender", "M");
                // Send the new row to the database
                rs.insertRow();

                // Move back to the current row
                rs.moveToCurrentRow();

                // Print all rows in the result set
                while (rs.next()) {
                    System.out.print("Person ID: " + rs.getInt("person_id") +
                            ", First Name: " + rs.getString("first_name") +
                            ", Last Name: " + rs.getString("last_name"));
                    System.out.println();
                }
            }
            finally {
                JDBCUtil.closeStatement(stmt);
            }
    }

### Updating a Row Using a ResultSet

Here are the steps involved in updating an existing row in a ResultSet object:
* Move the cursor to a valid row in the result set. Note that you can update data only for an existing row;
* Call an updateXxx() method for a column to update the column’s value;
* If you do not want to go ahead with the changes made using updateXxx() method calls, you need to call the cancelRowUpdates() method of the ResultSet to cancel the changes;
* When you are done updating all the column’s values for the current row, call the updateRow() method to send the changes to the database;

**If you move the cursor to a different row before calling the updateRow(), all your changes made using the updateXxx() method calls will be discarded.**

    public static void giveRaise(Connection conn, double raise) throws SQLException {
            String SQL = "select person_id, first_name, last_name, income from person";
            Statement stmt = null;
            try {
                stmt = conn.createStatement(TYPE_FORWARD_ONLY, CONCUR_UPDATABLE);
                // Get the result set
                ResultSet rs = stmt.executeQuery(SQL);
                // Make sure our resultset is updatable
                int concurrency = rs.getConcurrency();
                if (concurrency != CONCUR_UPDATABLE) {
                    System.out.println("The JDBC driver does not support updatable result sets.");
                    return;
                }
                // Give everyone a raise
                while (rs.next()) {
                    double oldIncome = rs.getDouble("income");
                    double newIncome = 0.0;
                    if (rs.wasNull()) {
                        // null income starts at 10000.00
                        oldIncome = 10000.00;
                        newIncome = oldIncome;
                    } else {
                        // Increase the income
                        newIncome = oldIncome + oldIncome * (raise / 100.0);
                    }
                    // Update the income column with the new value
                    rs.updateDouble("income", newIncome);
                    // Send the changes to the database
                    rs.updateRow();
                }
            } finally {
                JDBCUtil.closeStatement(stmt);
            }
        }

### Deleting a Row Using a ResultSet

Here are the steps to delete a row:
* Position the cursor at a valid row;
* Call the deleteRow() method of the ResultSet to delete the current row.

        ResultSet rs = get an updatable result set object;
        // Scroll to the row you want to delete, say the first row
        rs.next();
        // Delete the current row
        rs.delete(); // Row is deleted from the result set and the database
        // Commit or rollback changes depending on your processing logic

## ResultSetMetaData

A ResultSetMetaData contains a lot of information about all columns in a result set. All of the methods, except getColumnCount(), in the ResultSetMetaData interface accept a column index in the result set as an argument. It contains the table name, name, label, database data type, class name in Java, nullability, precision, etc. of a column.

# Using RowSets

An instance of the RowSet interface is a wrapper for a result set. The RowSet interface inherits from the ResultSet interface. In simple terms, a RowSet is a Java object that contains a set of rows from a tabular data source. The tabular data source could be a database, a flat file, a spreadsheet, etc. The RowSet interface is in the javax.sql package. The following are the advantages of the RowSet over the ResultSet:
* When you use a ResultSet object, you must deal with the Connection and Statement objects at the same time. A RowSet hides the complexities. All you have to work with is only one object, which is a RowSet object;
* A ResultSet is not Serializable. A RowSet is Serializable;
* **A ResultSet is always connected to a data source. A RowSet object does not need to be connected to its data source all the time**;
* A RowSet is by default scrollable and updatable;
* A ResultSet uses a database as its data source. You are not restricted to using only a database as a data source with a RowSet;
* A RowSet also supports filtering of data after the data has been retrieved. Filtering of data is not possible in a ResultSet. You must use a WHERE clause in a query to filter data in the database itself if you use a ResultSet.
* A RowSet makes it possible to join two or more data sets based on their column’s values after they have been retrieved from their data sources. One data set can be retrieved from a database and another from a flat file.

Disadvantages of using a RowSet:
* A specific RowSet implementation may cache data in memory. **You need to be careful when using such type of RowSets. You should not fetch large volumes of data**;
* With cached data in a RowSet, there are more possibilities of data inconsistency between the data in the RowSet and data in the data source.

The following interfaces in the javax.sql.rowset package define five types of rowsets:
* JdbcRowSet;
* CachedRowSet;
* WebRowSet;
* FilteredRowSet;
* JoinRowSet.

## Creating a RowSet

An instance of the **RowSetFactory** interface lets you create different types of RowSet objects without caring about the rowset implementation classes. To get a **RowSetFactory**, you need to use the **newFactory()** static method of the **RowSetProvider** class. The RowSetFactory interface has five methods to create five types of rowsets.

        JdbcRowSet jdbcRs = null;
        try {
            // Get the RowSetFactory implementation
            RowSetFactory rsFactory = RowSetProvider.newFactory();
            // Create a JdbcRowSet object
            jdbcRs = rsFactory.createJdbcRowSet();
            // Work with jdbcRs here
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (jdbcRs != null) {
                try {
                    // Close the RowSet
                    jdbcRs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

## Setting RowSet Connection Properties

Typically, a RowSet will need to connect to a data source to retrieve and update data. You can set the database connection properties for a RowSet in terms of a JDBC URL or a data source name.

// Create a RowSet
RowSet rs = create a RowSet;
// Set the conection properties for the RowSet
rs.setUrl("jdbc:derby:beginningJavaDB");
rs.setUsername("root");
rs.setPassword("chanda");

Alternatively, you can set a data source name for the RowSet object.

**You do not need to establish a connection to the database. The RowSet will take care of establishing connection when it is needed.**

## Setting a Command for a RowSet

The command will be in a string in the form of a SQL SELECT statement or a stored procedure call. You can use a question mark as a placeholder for any parameter that you would like to pass to your command at runtime. Working with parameters in a command for a RowSet is the same as working with parameters for a PreparedStatement.

    // Command to select rows from the person table with two parameters that
    // will be the range of the income
    String sqlCommand = "select person_id, first_name, last_name, income " +
    "from person " +
    "where income between ? and ?;
    // Set the command to the RowSet object
    rs.setCommand(sqlCommand);
    // Set the range of income between 20000.0 and 30000.0
    rs.setDouble(1, 20000.0);
    rs.setDouble(2, 30000.0);

## Populating a RowSet with Data

If you want to populate a RowSet with data by executing its command, you need to call its execute() method as shown:
// Execute its command to populate the RowSet
rs.execute();

## Scrolling Through Rows of a RowSet

By default, all RowSet objects are bidirectional scrollable and updateable. 

    RowSet rs = create a RowSet;
    ...
    while(rs.next()) {
        // Read values for person_id and first_name from the current row
        int personID = rs.getInt("person_id");
        String firstName = rs.getString("first_name");
        // Perform other processing here
    }

## Updating Data in a RowSet

Updating data in a RowSet is similar to updating data in a ResultSet. To update a column’s value, you need to move the cursor to a row, use one of the updateXxx() methods to set the new value for a column, and call the updateRow() method of the RowSet to make the changes permanent in the RowSet.

To insert a new row, you need to move the cursor to the insert row by calling the moveToInsertRow() method of the RowSet. You need to set values for columns in the insert row using one of updateXxx() methods. Finally, you call the insertRow() method of the RowSet. To delete a row, you need to move the cursor to the row you want to delete and call the deleteRow() method of the RowSet.

## JdbcRowSet

**A JdbcRowSet is also called a connected rowset because it always maintains a database connection.** You can think of a JdbcRowSet as a thin wrapper for a ResultSet. As a ResultSet always maintains a database connection, so does a JdbcRowSet. It adds some methods that let you configure the connection behaviors. You can use its setAutoCommit() method to enable or disable the auto-commit mode for the connection. You can use its commit() and rollback() methods to commit or rollback changes made to its data.

A JDBC driver or underlying database may not support a bidirectional scrollable and updatable result set. In such cases, a JdbcRowSet implementation may provide such features.

        RowSetFactory factory = RowSetUtil.getRowSetFactory();
        // Use a try-with-resources block
        try (JdbcRowSet jdbcRs = factory.createJdbcRowSet()) {
            // Set the connection parameters
            RowSetUtil.setConnectionParameters(jdbcRs);
            // Set the command and input parameters
            String sqlCommand = "select person_id, first_name, " +
                    "last_name from person " +
                    "where person_id between ? and ?";
            jdbcRs.setCommand(sqlCommand);
            jdbcRs.setInt(1, 101);
            jdbcRs.setInt(2, 301);
            // Retrieve the data
            jdbcRs.execute();
            // Scroll to the last row to get the row count It may throw an
            // exception if the underlying JdbcRowSet implementation
            // does not support a bi-directional scrolling result set.
            try {
                jdbcRs.last();
                System.out.println("Row Count: " + jdbcRs.getRow());
                // Position the cursor before the first row
                jdbcRs.beforeFirst();
            } catch (SQLException e) {
                System.out.println("JdbcRowSet implementation" +
                        " supports forward-only scrolling");
            }
            // Print the records in the rowset
            RowSetUtil.printPersonRecord(jdbcRs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

Updating row:

     RowSetFactory factory = RowSetUtil.getRowSetFactory();
     // Use a try-with-resources block
     try (JdbcRowSet jdbcRs = factory.createJdbcRowSet()) {
          // Set the connection parameters
          RowSetUtil.setConnectionParameters(jdbcRs);
          // Set the auto-commit mode to false
          jdbcRs.setAutoCommit(false);
          // Set the command and input parameters
          String sqlCommand = "select person_id, first_name, " +
                    "last_name, income from person " +
                    "where person_id = ?";
          jdbcRs.setCommand(sqlCommand);
          jdbcRs.setInt(1, 101);
          // Retrieve the data
          jdbcRs.execute();
          // If a row is retrieved, update the first row's income
          // column to 65000.00
          if (jdbcRs.next()) {
              int personId = jdbcRs.getInt("person_id");
              jdbcRs.updateDouble("income", 65000.00);
              jdbcRs.updateRow();
              // Commit the changes
              jdbcRs.commit();
              System.out.println("Income has been set to " +
                        "65000.00 for person_id=" + personId);
          }
          else {
              System.out.println("No person record was found.");
          }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

## CachedRowSet

**A CachedRowSet is also called a disconnected rowset because it is disconnected from a database when it does not need a database connection. It keeps the database connection open only for the duration it needs to interact with the database. Once it is done with the connection, it disconnects. For example, it connects to a database when it needs to retrieve or update data.**

A CachedRowSet is always serializable, scrollable, and updatable.

You can obtain the number of rows in a CachedRowSet using its size() method. Note that the size() method is not available for a JdbcRowSet.

        // Use a try-with-resources block
        try (CachedRowSet cachedRs = factory.createCachedRowSet()) {
            // Set the connection parameters
            RowSetUtil.setConnectionParameters(cachedRs);
            String sqlCommand = "select person_id, first_name, last_name " +
                    "from person " +
                    "where person_id between 101 and 501";
            cachedRs.setCommand(sqlCommand);
            cachedRs.execute();
            // Print the records in cached rowset
            System.out.println("Row Count: " + cachedRs.size());
            RowSetUtil.printPersonRecord(cachedRs);
        } catch (SQLException e) {
            e.printStackTrace();
        }


A CachedRowSet provides an additional feature called **paging** to let you retrieve rows generated by a command in chunks. The CachedRowSet lets you set the page size by calling its **setPageSize(int size)** method. Suppose a command for a CachedRowSet generates 500 rows. By calling its **setPageSize(90)**, it will retrieve a maximum of 90 rows at a time. When you call its **execute()** method, it will retrieve the first 90 rows. To retrieve the next 90 rows, you need to call its **nextPage()** method.


You can update the data in a CachedRowSet and save the changes back to the database. The process of saving changes to the database for a CachedRowSet is different from that of a JdbcRowSet. Two main reasons:
* First, it is disconnected and you do not want to connect to the database often;
* Second, the updated data may have conflicts with the data stored in the database.

The process of inserting and deleting rows in a CachedRowSet is the same as in a JdbcRowSet. After changing the values for the current row, you need to call the updateRow() method.

The process of updating rows is a bit different.

After you make changes to a CachedRowSet, you can send changes to the database by calling its acceptChanges() method that may commit the changes if you have set the commit-on-accept-change value to true. You need to refer to the implementation details of the CachedRowSet on how it lets you set the commit-on-accept-change value. If it is set to false, you need to use the commit() or rollback() method of the CachedRowSet interface to commit or rollback changes.

**A CachedRowSet has to deal with conflicts that may exist between the data in it and the data in the database. When conflicts are detected during the acceptChanges() method call, it throws a SyncProviderException.**

# Working with a Large Object (LOB)

The JDBC API has support for working with large objects stored in a database. The type of a large object could be one of the following.
* Binary Large Object (Blob)
* Character Large Object (Clob)
* National Character Large Object (NClob)

The data for LOB columns is usually not stored in a database table itself. The database stores the data for a LOB at some other location. It stores a reference (or pointer) to the data location in the table. The reference for a LOB stored in the table is also called a locator.

When you retrieve the data for a column of a LOB type, usually a JDBC driver retrieves only the locator for the LOB. When you need the actual data, you need to perform some more operations on the locator to fetch the data.

## Retrieving LOB Data

You can retrieve Blob, Clob and NClob column’s data from a result set using the getBlob(), getClob(), and getNClob() methods of the ResultSet interface, respectively.

    ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            int personId = rs.getInt("person_id");
            Blob pictureBlob = rs.getBlob("picture");
            Clob resumeClob = rs.getClob("resume");
        }

Most of the time, you will not read the Blob’s and Clob’s data in an array or a String object. They may contain big amounts of data. The Blob and Clob interfaces let you read their data in chunks using an InputStream and a Reader, respectively.

    InputStream in = pictureBlob.getBinaryStream();
    Reader reader = resumeClob.getCharacterStream();

## Creating a LOB Data

The Connection interface contains three methods to create a LOB:
* Blob createBlob() throws SQLException
* Clob createClob() throws SQLException
* NClob createNClob() throws SQLException

You can use one of the methods to create an empty LOB of a specific type. For example, to store a picture and resume in a database, you would create a Blob object and a Clob object as follows:

    Connection conn = JDBCUtil.getConnection();
    Blob pictureBlob = conn.createBlob();
    Clob resumeClob = conn.createClob();

Once you get the Blob and Clob objects you can write data to them in two ways:
* int setBytes(long pos, byte[] bytes) throws SQLException
* OutputStream setBinaryStream(long pos) throws SQLException


        // Get the output stream of the Blob object to write the picture data to it.
        int startPosition = 1; // start writing from beginning
        OutputStream out = pictureBlob.setBinaryStream(startPosition);
        // Get ready to read from a file
        String picturePath = "picture.jpg";
        FileInputStream fis = new FileInputStream(picturePath);
        // Read from the file and write to the Blob object
        int b = -1;
        while ((b = fis.read()) != -1) {
            out.write(b);
        }
        fis.close();
        out.close();


For Clob:

    // Get the Character output stream of the Clob object to write the resume data to it.
    int startPosition = 1; // start writing from beginning
    Writer writer = resumeClob.setCharacterStream(startPosition);
    // Get ready to read from a file
    String resumePath = "resume.txt";
    FileReader fr = new FileReader(resumePath);
    // Read from the file and write to the Clob object
    int b = -1;
    while ((b = fr.read()) != -1) {
        writer.write(b);
    }
    fr.close();
    writer.close();


Finally, it is time to write the LOB’s data to a database. You can use the setBlob() and setClob() methods of the PreparedStatement interface to set the Blob and Clob data as shown:

    Connection conn = JDBCUtil.getConnection();
    String SQL = "insert into person_detail (person_detail_id, person_id, picture, resume) values (?, ?, ?, ?)";
    PreparedStatement pstmt = null;
    pstmt = conn.prepareStatement(SQL);
    pstmt.setInt(1, 1); // set person_detail_id
    pstmt.setInt(2, 101); // Set person_id
    Blob pictureBlob = conn.createBlob();
    // Write data to pictureBlob object here
    pstmt.setBlob(3, pictureBlob);
    Clob resumeClob = conn.createClob();
    // Write data to resumeClob object here
    pstmt.setClob(4, resumeClob);
    // Insert the record into the database
    pstmt.executeUpdate();

The ResultSet interface also includes the updateBlob() and updateClob() methods, which you can use to update Blob and Clob objects through a ResultSet object. Blob and Clob objects may require a lot of resources. **Once you are done with them, you need to free the resources held by them by calling their free() method.**

    public static void insertPersonDetail(Connection conn, int personDetailId, int personId, String pictureFilePath, String resumeFilePath) throws SQLException {

        String SQL = "insert into person_detail (person_detail_id, person_id, picture, resume) values (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        try {
        pstmt = conn.prepareStatement(SQL);
        pstmt.setInt(1, personDetailId);
        pstmt.setInt(2, personId);
        // Set the picture data
        if (pictureFilePath != null) {
            // We need to create a Blob object first
            Blob pictureBlob = conn.createBlob();
            readInPictureData(pictureBlob, pictureFilePath);
            pstmt.setBlob(3, pictureBlob);
        }
        // Set the resume data
        if (resumeFilePath != null) {
            // We need to create a Clob object first
            Clob resumeClob = conn.createClob();
            readInResumeData(resumeClob, resumeFilePath);
            pstmt.setClob(4, resumeClob);
        }
        pstmt.executeUpdate();
        }
        catch (IOException | SQLException e) {
            throw new SQLException(e);
        } finally {
            JDBCUtil.closeStatement(pstmt);
        }
    }

    public static void retrievePersonDetails(Connection conn, int personDetailId, String picturePath, String resumePath) throws SQLException {

        String SQL = "select person_id, picture, resume from person_detail where person_detail_id = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, personDetailId);
            ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int personId = rs.getInt("person_id");
            Blob pictureBlob = rs.getBlob("picture");
            if (pictureBlob != null) {
                savePicture(pictureBlob, picturePath);
                pictureBlob.free();
            }
            Clob resumeClob = rs.getClob("resume");
            if (resumeClob != null) {
                saveResume(resumeClob, resumePath);
                resumeClob.free();
            }
        }
        } catch (IOException | SQLException e) {
            throw new SQLException(e);
        } finally {
            JDBCUtil.closeStatement(pstmt);
        }
    }

## Batch Updates

The update commands that you can use in a batch update are SQL INSERT, UPDATE, DELETE, and stored procedures. **A command in a batch should not produce a result set.** Otherwise, the JDBC driver will throw a SQLException. A command should generate an update count that will indicate the number of rows affected in the database by the execution of that command.
If you are using a Statement to execute a batch of commands, you can have heterogeneous commands in the same batch. For example, one command could be a SQL INSERT statement and another could be a SQL UPDATE statement.
If you are using a PreparedStatement or CallableStatement to execute a batch of commands, you will execute one command with multiple set of input parameters.

It is important to understand the behavior of the executeBatch() method of the Statement interface. It returns an array of int if all commands in the batch are executed successfully. The array contains as many elements as the number of commands in the batch. Each element in the array contains the update count that is returned from the command. The order of the element in the array is the same as the order of commands in the batch.

The following snippet of code shows how to use a Statement object to execute a batch update.

    Connection conn = JDBCUtil.getConnection();
    Statement stmt = conn.createStatement();
    // Add batch update commands
    stmt.addBatch("insert into t1...);
    stmt.addBatch("insert into t2...);
    stmt.addBatch("update t3 set...);
    stmt.addBatch("delete from t4...);
    // Execute the batch updates
    int[] updateCount = null;
    try {
        updatedCount = stmt.executeBatch();
        System.out.println("Batch executed successfully.");
    }
    catch (BatchUpdateException e) {
        System.out.println("Batch failed.");
    }

The following snippet of code shows how to use a PreparedStatement object to execute a batch update.

    String sql = "delete from person where person_id = ?";
    Connection conn = JDBCUtil.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql);
    // Add two commands to the batch.
    // Command #1: Set the input parameter and add it to the batch.
    pstmt.setInt(201);
    pstmt.addBatch();
    // Command #1: Set the input parameter and add it to the batch.
    pstmt.setInt(301);
    pstmt.addBatch();
    // Execute the batch update
    int[] updateCount = null;
    try {
        updatedCount = pstmt.executeBatch();
        System.out.println("Batch executed successfully.");
    }
    catch (BatchUpdateException e) {
        System.out.println("Batch failed.");
    }

## Savepoints in a Transaction

A database transaction consists of one or more changes as a unit of work. A savepoint in a transaction is like a marker that marks a point in a transaction so that, if needed, the transaction can be rolled back (or undone) up to that point.

    Connection conn = JDBCUtil.getConnection();
    Statement stmt = conn.createStatement();
    stmt.execute("insert into person..."); // insert 1
    Savepoint sp1 = conn.setSavepoint(); // savepoint 1
    stmt.execute("insert into person..."); // insert 2
    Savepoint sp2 = conn.setSavepoint(); // savepoint 2
    stmt.execute("insert into person..."); // insert 3
    Savepoint sp3 = conn.setSavepoint(); // savepoint 3
    stmt.execute("insert into person..."); // insert 4
    Savepoint sp4 = conn.setSavepoint(); // savepoint 4
    stmt.execute("insert into person..."); // insert 5

At this point, you have finer control on the transaction if you want to undo any of the above five inserts into the person table. Now you can use another version of the rollback() method of the Connection object, which accepts a Savepoint object. If you want to undo all changes that were made after savepoint 4, you can do so as follows:

    // Rolls back insert 5 only
    conn.rollback(sp4);
    If you want to undo all changes that were made after savepoint 2, you can do so as follows:
    // Rolls back inserts 3, 4, and 5
    conn.rollback(sp2);

## Using a DataSource

A factory for connections to the physical data source that this DataSource object represents. An alternative to the DriverManager facility, a DataSource object is the preferred means of getting a connection. An object that implements the DataSource interface will typically be registered with a naming service based on the Java™ Naming and Directory (JNDI) API.

Usually, you configure and deploy a DataSource on a server, which is available using a JNDI service. The following is a sample snippet of code that you can use to configure and deploy a DataSource programmatically. It creates a DataSource provided by MYSQL JDBC driver.

    import com.mySQL.jdbc.jdbc2.optional.MySQLDataSource;
    import javax.naming.InitialContext;
    import javax.naming.Context;
    ...
    // Create a DataSource object
    MySQLDataSource mds = new MySQLDataSource();
    mds.setServerName("localhost");
    mds.setPortNumber(3306);
    mds.setUser("root");
    mds.setPassword("chanda");
    // Get the initial context
    Context ctx = new InitialContext();
    // Bind (or register) the DataSource object under a logical name "jdbc/mydb"
    ctx.bind("jdbc.mydb", mds);


The Java application that needs a connection to a database will perform a lookup using the logical name of the DataSource that was given to it at the time of binding. Here is a typical snippet of Java code that you need to write when you need a Connection object:

    import javax.sql.DataSource;
    import java.sql.Connection;
    import javax.naming.InitialContext;
    import javax.naming.Context;
    ...
    // Get the initial context
    Context ctx = new InitialContext();
    // Perform a lookup for the DataSource using its logical name "jdbc/mydb"
    DataSource ds = (DataSource)ctx.lookup("jdbc/mydb");
    // Get a Connection object from the DataSource object
    Connection conn = ds.getConnection();
    // Perform other database related tasks...
    // Close the connection
    conn.close()

The JDBC API provides two other types of data source interfaces: javax.sql.ConnectionPoolDataSource and javas.sql.XADataSource:
* An implementation of the ConnectionPoolDataSource interface provides the connection pooling feature to improve the application’s performance;
* The implementation of the XADataSource interface provides support for distributed transactions, which involve multiple databases.

## Enabling JDBC Trace

You can enable JDBC tracing that will log JDBC activities to a PrintWriter object. You can use the setLogWriter(PrintWriter out) static method of the DriverManager to set a log writer if you are using the DriverManager to connect to a database. If you are using a DataSource, you can use its setLogWriter(PrintWriter out) method to set a log writer.

## Summary

* The **DriverManager** class facilitates registration of JDBC drivers to connect to different types of databases. When passed in database connection properties such as the server location, protocol, database names, user id, password, etc., the DriverManager uses the registered JDBC drivers to connect to the database and returns an object of the **Connection** interface that represents a connection to the database.
* You can use the **getMetaData()** method for a **Connection** object to get a **DatabaseMetaData** object.
* A **Statement** is used to execute SQL statements in string forms from a Java program. The result set returned by a SQL statement is made available in the Java program as an object of the **ResultSet** interface.
* A **PreparedStatement** is used to execute SQL statement with parameters. The SQL statement is pre-compiled to provide a faster execution on repeated use of the same SQL statement with different parameters. Using input parameters in the SQL statement as placeholders also prevents attacks from hackers that use SQL injections.
* A **CallableStatement** is used to call a SQL stored procedure or a function in a database.
* A **ResultSet** represents tabular data defined in terms of rows and columns. Typically, you get a ResultSet by executing a SQL statement that returns a result set from the database. A ResultSet may scroll only in the forward direction or in both forward and backward directions. All JDBC drivers will support at least a forward-only ResultSet. A ResultSet may also be used to update data in the database.
* A **RowSet** is a wrapper for a **ResultSet**. A RowSet hides the complexities that are involved in working with a ResultSet. A **JdbcRowSet**, which is also known as a connected rowset, maintains a database connection all the time. A **CachedRowSet**, which is also called a disconnected rowset, uses a database connection only for the duration it is needed. A **WebRowSet** is a CachedRowSet that supports importing data from an XML document and exporting its data to an XML document. A FilteredRowSet is a WebRowSet that provides filtering capability at the client side. A JoinRowSet is a WebRowSet that provides the ability to combine (or join) two or more disconnected rowsets into one rowset.
* The JDBC provides support for working with database large objects, typically called, Blob, Clob, and NClob.
* For a better performance, you can send multiple SQL commands to the database in one shot using the batch update feature of the JDBC API. **Batch updates are supported through the Statement, PreparedStatement, and CallableStatement interfaces.** The addBatch() method of the Statement object is used to add a SQL command to the batch. The executeBatch() method sends all SQL commands in the batch to the database for execution.
* A database transaction consists of one or more changes as a unit of work. A savepoint in a transaction is a marker that marks a point in a transaction so that, if needed, the transaction can be rolled back up to the marked point. An instance of the **Savepoint** interface represents a savepoint. You can create a savepoint in a transaction using the **setSavepoint()** method of the Connection object. You can specify a savepoint in the rollback() method of the Connection object to roll back the transaction to the specified savepoint.
