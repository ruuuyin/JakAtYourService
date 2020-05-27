package jays.data;

import jays.utils.Directory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseHandler {
    private final String CONNECTION_PROPS="Connection.properties";
    private String url;
    private String usr;
    private String pass;

    private Statement statement = null;
    private ResultSet resultSet = null;

    private Connection conn;
    public DatabaseHandler(){
        try {
            initializeDatabaseInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabaseInfo() throws IOException {
        InputStream is = getClass().getResource(Directory.MISC+CONNECTION_PROPS).openStream();
        Properties properties = new Properties();
        if (is!=null) properties.load(is);
        else throw new FileNotFoundException("Cannot find Connection.properties from classpath");

        url = properties.getProperty("dbURL");
        usr = properties.getProperty("dbUsername");
        pass = properties.getProperty("dbPass").equals("N/A")?"":properties.getProperty("dbPass");

        is.close();
    }

    public void startConnection(){
        try {
            conn = DriverManager.getConnection(url,usr,pass);
        }  catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String sql){
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int execUpdate(String sql){
        int returnedRow = 0;
        try {
            statement = conn.createStatement();
            returnedRow = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnedRow;

    }


    public void closeConnection(){
        try {
            if (!conn.isClosed())
                conn.close();
            if (!statement.isClosed())
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
