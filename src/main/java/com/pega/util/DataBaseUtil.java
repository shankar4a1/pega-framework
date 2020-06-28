

package com.pega.util;

import com.pega.*;
import org.testng.*;

import java.io.*;
import java.sql.*;

public class DataBaseUtil {
    static ConfigurationImpl config;
    String driver;
    static Statement statement;
    static Connection connection;
    static ResultSet rst;
    private static String JDBCurl;
    private static String DBUserName;
    private static String DBPassword;

    static {
        DataBaseUtil.config = null;
        DataBaseUtil.statement = null;
        DataBaseUtil.connection = null;
        DataBaseUtil.rst = null;
        DataBaseUtil.JDBCurl = null;
        DataBaseUtil.DBUserName = null;
        DataBaseUtil.DBPassword = null;
    }

    public DataBaseUtil() {
        this.driver = null;
        final File f = DataUtil.getGlobalSettingsFile();
        DataBaseUtil.config = new ConfigurationImpl(f);
        DataBaseUtil.JDBCurl = DataBaseUtil.config.getDataBaseConfig().getDataBaseURL();
        DataBaseUtil.DBUserName = DataBaseUtil.config.getDataBaseConfig().getDataBasUserName();
        DataBaseUtil.DBPassword = DataBaseUtil.config.getDataBaseConfig().getDataBasePassword();
    }

    private static void loadClass() {
        if (DataBaseUtil.JDBCurl.contains("oracle")) {
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                Reporter.log("Oracle JDBC Driver is Registered", true);
            } catch (ClassNotFoundException e) {
                Reporter.log("Where is your Oracle JDBC Driver? Include in your library path!", true);
                e.printStackTrace();
            }
        } else if (DataBaseUtil.JDBCurl.contains("postgresql")) {
            try {
                Class.forName("org.postgresql.Driver");
                Reporter.log("postgresql JDBC Driver is Registered");
            } catch (ClassNotFoundException e) {
                Reporter.log("Where is your PostgreSQL JDBC Driver? Include in your library path!", true);
                e.printStackTrace();
            }
        } else {
            if (!DataBaseUtil.JDBCurl.contains("db2")) {
                if (!DataBaseUtil.JDBCurl.contains("zos")) {
                    if (DataBaseUtil.JDBCurl.contains("mssql")) {
                        try {
                            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                            Reporter.log("mssql JDBC Driver is Registered");
                        } catch (ClassNotFoundException e) {
                            Reporter.log("Where is your MYSQL JDBC Driver? Include in your library path!", true);
                            e.printStackTrace();
                        }
                        return;
                    }
                    Reporter.log("Please enter the valid driver name", true);
                    return;
                }
            }
            try {
                Class.forName("com.ibm.db2.jcc.DB2Driver");
                Reporter.log("db2 JDBC Driver is Registered");
            } catch (ClassNotFoundException e) {
                Reporter.log("Where is your DB2 JDBC Driver? Include in your library path!", true);
                e.printStackTrace();
            }
        }
    }

    private static Statement getConnectionAndReturnStatement() {
        try {
            DataBaseUtil.connection = DriverManager.getConnection(DataBaseUtil.JDBCurl, DataBaseUtil.DBUserName, DataBaseUtil.DBPassword);
            if (DataBaseUtil.connection != null) {
                DataBaseUtil.statement = DataBaseUtil.connection.createStatement();
            } else {
                Reporter.log("Failed at getting connection to the database,please check the data details like url,username,password", true);
            }
        } catch (SQLException e) {
            Reporter.log("Failed at Creation of Statement,please check the details of Database/connection with the with Data base", true);
            e.printStackTrace();
            return null;
        }
        return DataBaseUtil.statement;
    }

    public static ResultSet selectRows(final String query) {
        Statement stmt = null;
        loadClass();
        stmt = getConnectionAndReturnStatement();
        try {
            if (stmt != null) {
                DataBaseUtil.rst = stmt.executeQuery(query);
            } else {
                Reporter.log("Failed at Creation of Statement,please check the details of Database/connection with the with Data base", true);
            }
        } catch (SQLException e) {
            Reporter.log("Failed at execution of the query select query,please check the syntax of the query", true);
            e.printStackTrace();
            return null;
        }
        return DataBaseUtil.rst;
    }

    public static int updateOrInsertRows(final String query) {
        int rowsupdated = 0;
        Statement stmt = null;
        loadClass();
        stmt = getConnectionAndReturnStatement();
        try {
            if (stmt != null) {
                rowsupdated = stmt.executeUpdate(query);
            } else {
                Reporter.log("Failed at Creation of Statement,please check the details of Database/connection with the with Data base", true);
            }
        } catch (SQLException e) {
            Reporter.log("Failed at execution of the query update/insert query,please check the syntax of the query", true);
            e.printStackTrace();
            return 0;
        }
        return rowsupdated;
    }

    public static void closeConnections() {
        try {
            if (DataBaseUtil.rst != null && !DataBaseUtil.rst.isClosed()) {
                DataBaseUtil.rst.close();
                Reporter.log("rst" + DataBaseUtil.rst + "Closed successfully", true);
            }
        } catch (SQLException e) {
            Reporter.log("unable to find the resultset /the resultset is already closed", true);
            e.printStackTrace();
        }
        try {
            if (DataBaseUtil.statement != null && !DataBaseUtil.statement.isClosed()) {
                DataBaseUtil.statement.close();
                Reporter.log("statement" + DataBaseUtil.statement + "Closed successfully", true);
            }
        } catch (SQLException e) {
            Reporter.log("unable to find the statement /the statement is already closed", true);
            e.printStackTrace();
        }
        try {
            if (DataBaseUtil.connection != null && !DataBaseUtil.connection.isClosed()) {
                DataBaseUtil.connection.close();
                Reporter.log("connection" + DataBaseUtil.connection + "Closed successfully", true);
            }
        } catch (SQLException e) {
            Reporter.log("unable to find the connection /the connection is already closed", true);
            e.printStackTrace();
        }
    }
}
