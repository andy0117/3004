/*
 * Handles all queries relating to the 'responses' table
 */
package database;

import java.sql.*;
/**
 *
 * @author Darren
 */
public class responses {
    Connection conn;
    private int responseID;
    private int questID;
    private String keypad;
    private String responseText;
    
    public responses() {
        responseID = -1;
        questID = -1;
        keypad = "";
        responseText = "";
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
     * Attempts to add this response to the database. 
     * Database will not be checked for success.
     * Will not update existing response.
     * 
     * Pre-condition: The responseID must be set to a non-existing responseID
     * 
     * @return  0    for attempt made.
     *          -1   for invalid response properties.
     *          -2   for undefined error.
     */
    public int addResponse() {
        try {
            if (getResponseID() == -1) {
                return -1;
            } else if (getQuestID() == -1) {
                return -1;
            } else if (getResponseText().equals("")) {
                return -1;
            } else if (getKeypad().equals("")) {
                return -1;
            }
            
            getOracleConnection();
            String query= "INSERT INTO Responses(responseID, keypad, response, "
                    + "questID) VALUES (" + getResponseID() + ", '" 
                    + getKeypad() + "', '" + getResponse() + "', " + getQuestID() + ")";  
            runQuery(query);
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }
    
    /**
     * Attempts to edit this response in the database. 
     * Database will not be checked for success.
     * Will not add a new response.
     * 
     * Pre-condition: The responseID must be set to an existing response
     * 
     * @return  0    for attempt made.
     *          -1   for invalid response properties.
     *          -2   for undefined error.
     */
    public int editResponse() {
        try {
            if (getResponseID() == -1) {
                return -1;
            } else if (getQuestID() == -1) {
                return -1;
            } else if (getResponseText().equals("")) {
                return -1;
            } else if (getKeypad().equals("")) {
                return -1;
            }
            
            getOracleConnection();
            String query= "UPDATE Responses SET questID='" + getQuestID() 
                    + "', response='" + getResponseText() 
                    + "', keypad='" + getKeypad() + "', WHERE responseID=" 
                    + getResponseID();  
            runQuery(query);
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }

    /**
     * Attempts to delete this response from the database. 
     * Database will not be checked for success.
     * 
     * Pre-condition: The responseID must be set to an existing response
     * 
     * @return  0    for attempt made
     *          -1   for unset response ID.
     *          -2   for undefined error.
     */
    public int deleteResponse() {
        try {
            if (getResponseID() == -1) {
                return -1;
            } 
            getOracleConnection();
            String query= "DELETE FROM Responses WHERE responseID=" 
                    + getResponseID();
            runQuery(query);
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }
    
    /**
     * Attempts to find response by responseID. 
     * Updates properties of this instance of responses to result found.
     * 
     * Pre-condition: The responseID must be set to an existing response
     * 
     * @return  0    for attempt made
     *          -1   for unset response ID.
     *          -2   for undefined error.
     */
    public int getResponse() {
        try {
            if (getResponseID() == -1) {
                return -1;
            } 
            getOracleConnection();
            String query= "SELECT FROM Responses WHERE responseID=" + getResponseID();
            ResultSet resultset = runQuery(query);
            resultset.next();
            setResponseID(resultset.getInt("responseID"));
            setQuestID(resultset.getInt("questID"));
            setKeypad(resultset.getString("keypad"));
            setResponseText(resultset.getString("response"));
            closeOracleConnection();
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }

    /**
     * @param responseID the responseID to set, use -1 to automatically set next
     * available ID.
     */
    public void setResponseID(int responseID) {
        if (responseID != -1) {
            this.setResponseID(responseID);
        } else {
            try {
                getOracleConnection();
                String query= "SELECT MAX(responseID) FROM Responses";  
                ResultSet resultset = runQuery(query);
                resultset.next();
                this.setResponseID(resultset.getInt(1) + 1);
                closeOracleConnection();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * @return the responseID
     */
    public int getResponseID() {
        return responseID;
    }

    /**
     * @return the questID
     */
    public int getQuestID() {
        return questID;
    }

    /**
     * @param questID the questID to set
     */
    public void setQuestID(int questID) {
        this.questID = questID;
    }

    /**
     * @return the keypad
     */
    public String getKeypad() {
        return keypad;
    }

    /**
     * @param keypad the keypad to set
     */
    public void setKeypad(String keypad) {
        this.keypad = keypad;
    }

    /**
     * @return the response
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * @param response the response to set
     */
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}