import java.util.scanner;

public class java_pract
{
	string *seats_on_plane [30][5];
	
	public class ticket(){
		int aisle;
		char seat_let;
		int boarding;
		int cost;
		}
	
	public class make_ticket(String args);
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(in != 'q'){
			System.out.println("Press 'q' to exit or 'c' to make new ticket");
			char in = sc.nextChar();
			if(in != 'q'){
				make_ticket(args);	
			}
		}	
		
	}
	
	public class make_ticket(String args){
		Scanner scan = new Scanner(System.in);
		char inp;
		int inp2;
		ticket() tick = new ticket();
		System.out.println("Enter Aisle number:");
		inp2 = scan.nextInt();
		tick.aisle = inp2;
		System.out.println("Enter Seat letter):
		inp = scan.nextChar();
		tick.seat_let = inp;
		System.out.println("Enter boarding number:");
		inp2 = scan.nextInt();
		tick.boarding = inp2;
		System.out.println("Enter cost:");
		inp2 = scan.nextInt();
		tick.cost = inp2;
		System.out.println("End ticket");
		
	}
}