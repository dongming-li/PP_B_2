

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String UserWaiting = null;
	
    /**
     * Default constructor. 
     */
    public MyServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h3>Hello World</h3>");
		out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
        String reqType = request.getParameter("reqType");
        
        String responseMessage = "init";
        
        WSDatabase Database = new WSDatabase();
        //Database.StartConnection();
        try {
			Database.StartConnection();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("database offline");
			return;
		}
        
        switch(reqType){
        	case "register":
        		responseMessage = register(request, Database);
        		break;
        	case "login":
                responseMessage = login(request, Database);
        		break;
        	case "newGame":
        		responseMessage = newGame(request, Database);
        		break;
        	case "chatUpdate":
        		responseMessage = getChatUpdate(request, Database);
        		break;
        	case "chatPost":
        		responseMessage = ChatPost(request, Database);
        		break;
        	default:
        		out.println("Invalid request");
        		break;
        }   
        
        Database.CutConnection();
        
        out.println(responseMessage);
        
        return;
	}
 
	public String login(HttpServletRequest request, WSDatabase Database){
		//TODO insert user
        String username = request.getParameter("username");
		try {
			if(!Database.ConfirmUserID(username)){
				return "Invalid Username";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Check UserName Failed";
		}
		
		return "Home";
	}

	public String register(HttpServletRequest request, WSDatabase Database){
        String username = request.getParameter("username");

        //String password = request.getParameter("password");
        try {
        	if(Database.ConfirmUserID(username)){
        		return "Username already taken";
        	}
			Database.InsertUser(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "could not insert user:" + username;
		}
		
		return "Home";
	}

	public String newGame(HttpServletRequest request, WSDatabase Database){
		String username = request.getParameter("username");
		
		if(UserWaiting == null){
			UserWaiting = username;
			return "Match Making";
		}
		
		String user1 = UserWaiting;
		UserWaiting = null;
		String user2 = username;
		int gameID = 1;
		
		try {
			while (!Database.ConfirmGameID(Integer.toString(gameID))){
				gameID ++;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "failed to find gameID";
		}
		
		try {
			Database.InsertNewGame(Integer.toString(gameID), user1, user2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed to insert Game";
		}
		//Database.InsertNewGame(gameID, user1, user2);
		return "Home";
	}
	
	public String addMessage(HttpServletRequest request, WSDatabase Database){
		return "Home";
	}
	
	public String getChatUpdate(HttpServletRequest request, WSDatabase Database){
		
		String gameID;
		Integer logNumber = 3;
		
		try {
			gameID = Database.GetGameID(request.getParameter("username"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "no gameId";
		}
		
		//get the most recent chat log
		try {
			
			while(Database.GetChatlog(gameID, logNumber) == null && logNumber != 0){
				logNumber --;
			}
			return Database.GetChatlog(gameID, logNumber);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Home";
	}
	
	public String ChatPost(HttpServletRequest request, WSDatabase Database){
		
		String gameID;
		Integer logNumber = 1;
		
		try {
			gameID = Database.GetGameID(request.getParameter("username"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "no gameId";
		}
		
		//get the most recent chat log
		try {
			
			while(Database.GetChatlog(gameID, logNumber) != null && logNumber != 4){
				logNumber ++;
			}
			Database.SetChatLog(gameID, logNumber, request.getParameter("chatData"));
			return request.getParameter("cahtData");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "error";	
	}
}