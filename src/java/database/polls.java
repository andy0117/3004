/*
 * Handles all queries relating to the 'polls' table
 */
package database;

import java.sql.*;
/**
 *
 * @author Darren
 */
public class polls {
    Connection conn;
    private int pollID;
    private String pollName;
    private String location;
    
    public polls() {
        pollID = -1;
        pollName = "";
        location = "";
    }
    
    /**
     * Establishes a Oracle connection.
     */
    private Connection getOracleConnection() {
        conn=null;
        try {
            /* Load the Oracle JDBC Driver and register it. */
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            /* Open a new connection */
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "s4203040", "064460");
        } catch(Exception ex){
            System.out.println(ex.toString());
        }
        return conn;
    }
    
    /**
     * Runs the specified query.
     * 
     * @param query The query string to run.
     * @return ResultSet returned from running query.
     */
    private ResultSet runQuery(String query) {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    /**
     * Closes any open Oracle connection.
     */
    private void closeOracleConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
    
    /**
     * Attempts to locate all questIDs for questions this poll in the database. 
     * Will not check for success.
     * 
     * Pre-condition: The pollID must be set to an existing poll
     * 
     * @return  ResultSet   for attempt made.
     *          null        for error.
     */
    public ResultSet getQuestions() {
        try {
            if (getPollID() == -1) {
                return null;
            }
            
            getOracleConnection();
            String query= "SELECT questID FROM Questions WHERE pollID=" 
                    + getPollID();  
            ResultSet resultSet = runQuery(query);
            closeOracleConnection();
            return resultSet;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    /**
     * Attempts to add this poll to the database. 
     * Database will not be checked for success.
     * Will not update existing poll.
     * 
     * Pre-condition: The pollID must be set to a non-existing pollID
     * 
     * @return  0    for attempt made.
     *          -1   for invalid poll properties.
     *          -2   for undefined error.
     */
    public int addPoll() {
        try {
            if (getPollID() == -1) {
                return -1;
            } else if (getPollName().equals("")) {
                return -1;
            } else if (getLocation().equals("")) {
                return -1;
            }
            
            getOracleConnection();
            String query= "INSERT INTO Polls(pollID, pollName, location) VALUES"
                    + "(" + getPollID() + ", '" + getPollName() + "', '" + getLocation() 
                    + "')";  
            runQuery(query);
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }
    
    /**
     * Attempts to edit this poll in the database. 
     * Database will not be checked for success.
     * Will not add a new poll.
     * 
     * Pre-condition: The pollID must be set to an existing poll
     * 
     * @return  0    for attempt made.
     *          -1   for invalid poll properties.
     *          -2   for undefined error.
     */
    public int editPoll() {
        try {
            if (getPollID() == -1) {
                return -1;
            } else if (getPollName().equals("")) {
                return -1;
            } else if (getLocation().equals("")) {
                return -1;
            }
            
            getOracleConnection();
            String query= "UPDATE Polls SET pollName='" + getPollName() 
                    + "', location='" + getLocation() + "', WHERE pollID=" 
                    + getPollID();  
            runQuery(query);
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }

    /**
     * Attempts to delete this poll from the database. 
     * Database will not be checked for success.
     * 
     * Pre-condition: The pollID must be set to an existing poll
     * 
     * @return  0    for attempt made
     *          -1   for unset poll ID.
     *          -2   for undefined error.
     */
    public int deletePoll() {
        try {
            if (getPollID() == -1) {
                return -1;
            } 
            getOracleConnection();
            
            /* Delete questions under poll */
            String query= "SELECT questID FROM Questions WHERE pollID=" + getPollID();
            ResultSet resultSet = runQuery(query);
            
            /* Calls each question to delete itself and its children */
            while (resultSet.next()) {
                questions temp = new questions();
                temp.setQuestID(resultSet.getInt("questID"));
                temp.deleteQuestion();
            }
            
            /* Delete poll */
            query= "DELETE FROM Polls WHERE pollID=" + getPollID();
            runQuery(query);
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }
    
    /**
     * Attempts to find poll by pollID. 
     * Updates properties of this instance of polls to result found.
     * 
     * Pre-condition: The pollID must be set to an existing poll
     * 
     * @return  0    for attempt made
     *          -1   for unset poll ID.
     *          -2   for undefined error.
     */
    public int getPoll() {
        try {
            if (getPollID() == -1) {
                return -1;
            } 
            getOracleConnection();
            String query= "SELECT FROM Polls WHERE pollID=" + getPollID();
            ResultSet resultset = runQuery(query);
            resultset.next();
            setPollID(resultset.getInt("pollID"));
            setPollName(resultset.getString("pollName"));
            setLocation(resultset.getString("location"));
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }

    /**
     * @param pollID the pollID to set, use -1 to automatically set next
     * available ID.
     */
    public void setPollID(int pollID) {
        if (pollID != -1) {
            this.pollID = pollID;
        } else {
            try {
                getOracleConnection();
                String query= "SELECT MAX(pollID) FROM Polls";  
                ResultSet resultset = runQuery(query);
                resultset.next();
                this.pollID = resultset.getInt(1) + 1;
                closeOracleConnection();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * @return the pollID
     */
    public int getPollID() {
        return pollID;
    }

    /**
     * @return the pollName
     */
    public String getPollName() {
        return pollName;
    }

    /**
     * @param pollName the pollName to set
     */
    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

}