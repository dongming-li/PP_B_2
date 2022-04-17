package connection;
import connection.WSDatabase;

public class Main {

	public static void main(String[] args) throws Exception{
		WSDatabase test = new WSDatabase();
		test.StartConnection();
		//test.SetChatLog("game1", 1, "Hello");
		//System.out.println(test.GetChatlog("game1", 1));
		System.out.println(test.ConfirmUserID("userguess"));
		//test.SetGameID("userUguess", "51");
		//test.InsertUser("userUguess", "five");
		//String game[] = test.GetUserID("51");
		//System.out.println(game);
		//System.out.println(game[0]);
		//System.out.println(game[1]);
		//test.InsertUser("NumberOne");
		//test.InsertUser("NumberTwo");
		//test.DeleteGame("game1");
		//test.InsertNewGame("game1", "userUguess", "userIguess");
		//test.SetPlayerData("game1", 1, "Income", 5);
		//System.out.println(test.GetPlayerData("game1", 1, "Income"));
		//SetHexData(String gameID, int hexNumber, String dataType, int updatedData)
		//GetHexData(String gameID, int hexNumber, String dataType)
		//SetPlayerData(String gameID, int playerNumber, String dataType, int updatedData)
		//GetPlayerData(String gameID, int playerNumber, String dataType)
		
		
	}
}
