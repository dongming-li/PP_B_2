package servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.sql.Statement;

public class WSDatabase {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void StartConnection() throws Exception { //Starts the database connection for this object.
    	 try {
             // This will load the MySQL driver, each DB has its own driver
             Class.forName("com.mysql.jdbc.Driver");
             // Setup the connection with the DB
             connect = DriverManager.getConnection("jdbc:mysql://mysql.cs.iastate.edu/db309ppb2?user=dbu309ppb2&password=tA3abwg!&autoReconnect=true&useSSL=false");


         } catch (Exception e) {
             throw e;
         	}
    }
    public void CutConnection(){ //Clears the database connection for this object.
    	try {
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }
    public void InsertUser(String userName, String password) throws Exception { //Creates a new row for the table, inserts a new UserID, and sets GameID to null.
        try {         
            preparedStatement = connect.prepareStatement("insert into  db309ppb2.Users values (default, ?, ?, ?)");
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, null);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }
    public void SetGameID(String userName, String gameID) throws Exception { //takes a UserID to find an existing table entry for that UserID. Then sets the GameID to the specified GameID. GameID can be set to null.
        try {
            preparedStatement = connect.prepareStatement("UPDATE Users SET GameID = ? WHERE UserID = ?");
            preparedStatement.setString(1, gameID);
            preparedStatement.setString(2, userName);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public void DeleteUser(String userName) throws Exception { //takes a UserID to find an existing table entry for that UserID. Then deletes that database entry.
        try {
            preparedStatement = connect.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public void NullAllGameID(String gameID) throws Exception { //takes a GameID, and sets any mentions of that GameID to null for the table.
        try {
            preparedStatement = connect.prepareStatement("UPDATE Users SET GameID = ? WHERE GameID = ?");
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, gameID);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public String GetGameID(String userID) throws Exception { //Takes a userID, and finds the game ID associated with it.
        try {
            preparedStatement = connect.prepareStatement("select GameID FROM Users WHERE UserID = ?");
            preparedStatement.setString(1, userID);
            resultSet = preparedStatement.executeQuery();
            String gameID = null;
            while (resultSet.next()) {
            gameID = resultSet.getString("GameID");
            }
            return gameID;           
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public String ConfirmUserID(String userID) throws Exception { //Takes a userID, and return true if user exists.
        try {
            preparedStatement = connect.prepareStatement("select Password FROM Users WHERE UserID = ?");
            preparedStatement.setString(1, userID);
            resultSet = preparedStatement.executeQuery();
            String usID = null;
            while (resultSet.next()) {
            usID = resultSet.getString("Password");
            }
            return usID;
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public boolean ConfirmGameID(String gameID) throws Exception { //takes a GameID to find an existing table entry for that GameID. Then deletes that database entry, and clears it from any players that may have it.
        try {
            preparedStatement = connect.prepareStatement("select GameID FROM GameDatav1 WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            resultSet = preparedStatement.executeQuery();
            String gmID = null;
            while (resultSet.next()) {
            gmID = resultSet.getString("GameID");
            }
            if(gmID != null)
            {
            	return true;
            }
            else{
            	return false;
            }          
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public String[] GetUserID(String gameID) throws Exception { //Takes a GameID, and returns an array with the two UserIDs associated with it.
        try {
            preparedStatement = connect.prepareStatement("select UserID FROM Users WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            resultSet = preparedStatement.executeQuery();
            String userID[] = new String[2];
            int i = 0;
            while (resultSet.next()) {
            userID[i] = resultSet.getString("UserID");
            i++;
            }
            return userID;           
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public String GetUser1(String gameID) throws Exception { //Takes a GameID, and returns an array with the two UserIDs associated with it.
        try {
            preparedStatement = connect.prepareStatement("select User1 FROM db309ppb2.GameDatav1 WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String userID = resultSet.getString("User1");
                return userID;
            }
            else{
            	return null;
            }
            

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public String GetUser2(String gameID) throws Exception { //Takes a GameID, and returns an array with the two UserIDs associated with it.
        try {
            preparedStatement = connect.prepareStatement("select User2 FROM db309ppb2.GameDatav1 WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String userID = resultSet.getString("User2");
                return userID;
            }
            else{
            	return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public void InsertNewGame(String gameID, String user1, String user2) throws Exception { //Creates a new row for the table "GameDatav1", and automatically updates the GameID of the added players.
        try {
        	this.SetGameID(user1, gameID);
        	this.SetGameID(user2, gameID);
            preparedStatement = connect.prepareStatement("insert into  db309ppb2.GameDatav1 values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, gameID); //GameID
            preparedStatement.setString(2, user1); //User1
            preparedStatement.setString(3, user2); //User2
            preparedStatement.setString(4, null); //User2
            preparedStatement.setString(5, null); //User2
            preparedStatement.setString(6, null); //User2
            preparedStatement.setInt(7, 0); //Income1, Income of user1
            preparedStatement.setInt(8, 0); //Income2, Income of user2
            preparedStatement.setInt(9, 0); //TCash1, Total cash of user1
            preparedStatement.setInt(10, 0); //TCash2, Total cash of user2
            preparedStatement.setInt(11, 0); //UnitTotal1, Total units controlled by user1
            preparedStatement.setInt(12, 0); //UnitTotal2, Total units controlled by user2
            preparedStatement.setInt(13, 0); //ResourceA1, Total amount of Resource(A) held by user1
            preparedStatement.setInt(14, 0); //ResourceA2, Total amount of Resource(A) held by user2
            
            preparedStatement.setInt(15, 0); //Hex0U1, Number of unit 1 on tile
            preparedStatement.setInt(16, 0); //Hex0U2, Number of unit 2 on tile
            preparedStatement.setInt(17, 0); //Hex0U3, Number of unit 3 on tile
            preparedStatement.setInt(18, 0); //Hex0Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(19, 0); //Hex0Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(20, 0); //Hex0Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(21, 0); //Hex0Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.setInt(22, 0); //Hex1U1, Number of unit 1 on tile
            preparedStatement.setInt(23, 0); //Hex1U2, Number of unit 2 on tile
            preparedStatement.setInt(24, 0); //Hex1U3, Number of unit 3 on tile
            preparedStatement.setInt(25, 0); //Hex1Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(26, 0); //Hex1Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(27, 0); //Hex1Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(28, 0); //Hex1Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.setInt(29, 0); //Hex2U1, Number of unit 1 on tile
            preparedStatement.setInt(30, 0); //Hex2U2, Number of unit 2 on tile
            preparedStatement.setInt(31, 0); //Hex2U3, Number of unit 3 on tile
            preparedStatement.setInt(32, 0); //Hex2Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(33, 0); //Hex2Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(34, 0); //Hex2Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(35, 0); //Hex2Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.setInt(36, 0); //Hex3U1, Number of unit 1 on tile
            preparedStatement.setInt(37, 0); //Hex3U2, Number of unit 2 on tile
            preparedStatement.setInt(38, 0); //Hex3U3, Number of unit 3 on tile
            preparedStatement.setInt(39, 0); //Hex3Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(40, 0); //Hex3Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(41, 0); //Hex3Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(42, 0); //Hex3Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.setInt(43, 0); //Hex4U1, Number of unit 1 on tile
            preparedStatement.setInt(44, 0); //Hex4U2, Number of unit 2 on tile
            preparedStatement.setInt(45, 0); //Hex4U3, Number of unit 3 on tile
            preparedStatement.setInt(46, 0); //Hex4Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(47, 0); //Hex4Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(48, 0); //Hex4Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(49, 0); //Hex4Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.setInt(50, 0); //Hex5U1, Number of unit 1 on tile
            preparedStatement.setInt(51, 0); //Hex5U2, Number of unit 2 on tile
            preparedStatement.setInt(52, 0); //Hex5U3, Number of unit 3 on tile
            preparedStatement.setInt(53, 0); //Hex5Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(54, 0); //Hex5Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(55, 0); //Hex5Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(56, 0); //Hex5Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.setInt(57, 0); //Hex6U1, Number of unit 1 on tile
            preparedStatement.setInt(58, 0); //Hex6U2, Number of unit 2 on tile
            preparedStatement.setInt(59, 0); //Hex6U3, Number of unit 3 on tile
            preparedStatement.setInt(60, 0); //Hex6Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(61, 0); //Hex6Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(62, 0); //Hex6Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(63, 0); //Hex6Resource, tinyInt number indicates a specific Resource
           
            preparedStatement.setInt(64, 0); //Hex7U1, Number of unit 1 on tile
            preparedStatement.setInt(65, 0); //Hex7U2, Number of unit 2 on tile
            preparedStatement.setInt(66, 0); //Hex7U3, Number of unit 3 on tile
            preparedStatement.setInt(67, 0); //Hex7Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(68, 0); //Hex7Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(69, 0); //Hex7Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(70, 0); //Hex7Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.setInt(71, 0); //Hex8U1, Number of unit 1 on tile
            preparedStatement.setInt(72, 0); //Hex8U2, Number of unit 2 on tile
            preparedStatement.setInt(73, 0); //Hex8U3, Number of unit 3 on tile
            preparedStatement.setInt(74, 0); //Hex8Terrain, tinyInt number indicates a specific terrain
            preparedStatement.setInt(75, 0); //Hex8Structure, tinyInt number indicates a specific Structure
            preparedStatement.setInt(76, 0); //Hex8Owner (1 = player1, 2 = player2, 0 = none)
            preparedStatement.setInt(77, 0); //Hex8Resource, tinyInt number indicates a specific Resource
            
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public void DeleteGame(String gameID) throws Exception { //takes a GameID to find an existing table entry for that GameID. Then deletes that database entry, and clears it from any players that may have it.
        try {
        	this.NullAllGameID(gameID);
            preparedStatement = connect.prepareStatement("DELETE FROM GameDatav1 WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public void SetHexData(String gameID, int hexNumber, String dataType, int updatedData) throws Exception { //Takes the GameID, Hex number, HexDatatype, and data to update. Then finds the specified hex and data, and updates it.
        try {
        	//Example hexNumber = 0, DataType = "U1", updatedData = 5
        	//Sets Hex0U1 to 5, meaning 5 of unit one is stationed there.
        	String hex = ("Hex"+hexNumber + dataType);
            preparedStatement = connect.prepareStatement("UPDATE GameDatav1 SET " + hex + " = ? WHERE GameID = ?");
            preparedStatement.setInt(1, updatedData);
            preparedStatement.setString(2, gameID);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public int GetHexData(String gameID, int hexNumber, String dataType) throws Exception { //Takes the GameID, Hex number, and HexDatatype. Then retrieves the requested data, returns -1 if no data found.
        try {
        	//Example hexNumber = 0, DataType = "U1"
        	//returns data from Hex0U1
        	String hex = ("Hex"+hexNumber + dataType);
            preparedStatement = connect.prepareStatement("select " + hex +" FROM GameDatav1 WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            resultSet = preparedStatement.executeQuery();
            int hexData = -1;
            while (resultSet.next()) {
            hexData = resultSet.getInt(hex);
            }
            return hexData;           
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public void SetPlayerData(String gameID, int playerNumber, String dataType, int updatedData) throws Exception { //Takes the GameID, playerNumber, dataType, and data to update. Then finds the specified data, and updates it.
        try {
        	//Example playerNumber = 1, DataType = "UnitTotal", updatedData = 5
        	//Sets UnitTotal1 to 5, meaning 5 user1 has 5 total units.
        	String dataR = (dataType+playerNumber);
            preparedStatement = connect.prepareStatement("UPDATE GameDatav1 SET " + dataR + " = ? WHERE GameID = ?");
            preparedStatement.setInt(1, updatedData);
            preparedStatement.setString(2, gameID);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public void SetChatLog(String gameID, int logNumber, String updatedLog) throws Exception {
        try {
        	String dataR = ("Chatlog"+logNumber);
            preparedStatement = connect.prepareStatement("UPDATE GameDatav1 SET " + dataR + " = ? WHERE GameID = ?");
            preparedStatement.setString(1, updatedLog);
            preparedStatement.setString(2, gameID);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public int GetPlayerData(String gameID, int playerNumber, String dataType) throws Exception { //Takes the GameID, playerNumber, dataType. Then retrieves the requested data, returns -1 if no data found.
        try {
        	//Example playerNumber = 1, DataType = "UnitTotal"
        	//returns data from UnitTotal1
        	String dataR = (dataType+playerNumber);
            preparedStatement = connect.prepareStatement("select " + dataR +" FROM GameDatav1 WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            resultSet = preparedStatement.executeQuery();
            int finData = -1;
            while (resultSet.next()) {
            finData = resultSet.getInt(dataR);
            }
            return finData;           
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    public String GetChatlog(String gameID, int logNumber) throws Exception {
        try {
        	//Example playerNumber = 1, DataType = "UnitTotal"
        	//returns data from UnitTotal1
        	String dataR = ("Chatlog"+ logNumber);
            preparedStatement = connect.prepareStatement("select " + dataR +" FROM GameDatav1 WHERE GameID = ?");
            preparedStatement.setString(1, gameID);
            resultSet = preparedStatement.executeQuery();
            String finData = null;
            while (resultSet.next()) {
            finData = resultSet.getString(dataR);
            }
            return finData;           
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

        } catch (Exception e) {

        }
    }

}
