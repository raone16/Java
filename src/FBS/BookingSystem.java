package FBS;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import FBS.FlightDetails;
import FBS.FlightDetails.Booking;
public class BookingSystem {
	static Scanner input = new Scanner(System.in);
        static List<FlightDetails> l = new ArrayList<FlightDetails>();
        static void addflight() {
        	FlightDetails f =new FlightDetails();
        	l.add(f);
        }
        static FlightDetails flightNo(int flightNo) {
        	FlightDetails f;
        
	 		try {
	 			f=l.get(flightNo-101);
	 		}
	 		catch(Exception e) {
	 			return null;
	 		}
	 		
	 		return f;
        	
        }
        static FlightDetails flightNo() {
        	FlightDetails f;
        
        	int flightNo;
        	System.out.println("Enter the flight no");
	 		flightNo=input.nextInt();
	 		try {
	 			f=l.get(flightNo-101);
	 		}
	 		catch(Exception e) {
	 			return null;
	 		}
	 		
	 		return f;
        	
        }
        static List<Integer> availableFlight(){
        	List<Integer> availflight = new ArrayList<Integer>();
        	FlightDetails f;
        	for(int i=0;i<l.size();i++) {
        		f=l.get(i);
        		availflight.add(f.getFlightNo());
        		
        	}
        	return availflight;
            
        } 
        static void bookingDetail(int bId) {
        	boolean flag;
        	for(FlightDetails f: l) {
        		flag=f.displayBookingSummary(bId);
        		if(flag)
        			return;
        	}
        	System.out.println("invalid booking id");
        	
        	
        }
        static void addToFile() throws IOException {
        	
        	FileOutputStream f = new FileOutputStream("myObject.txt");
			//ObjectOutputStream o = new ObjectOutputStream(f);
			Writer bw = new OutputStreamWriter(f);
			for(FlightDetails fly:l) {
				try {
				Booking br = fly.getBookingRecord();
				while(br!=null) {
					System.out.println("adding to file");
					bw.write(Integer.toString(fly.getFlightNo())+"\t");
					bw.write(Integer.toString(br.getBookId())+"\t");
					bw.write(br.getSeatsBooked().toString()+"\t");
					bw.write(Double.toString(br.getCost())+"\t");
					bw.write((br.isMealPreference()?"Yes":"No")+"\t");
					bw.write((br.getStatus()? "Confirm":"Cancel")+"\t");
					
					bw.write("\n");
					br=br.getNext();
				}
        	}
				
		    catch(Exception e)
				{
		    	  System.out.println(e.getMessage());
				}
        	}bw.close();
        
        	
        }
        public static void readFromFile() {
        	Path path = Paths.get("myObject.txt");
        	int tempId;
        	FlightDetails f;
        	try {
        		BufferedReader buf = Files.newBufferedReader(path,StandardCharsets.US_ASCII);
        		String newRecord = buf.readLine();
        		while(newRecord !=null) {
        			String[] items = newRecord.split("\t");
        			tempId=Integer.parseInt(items[0])-101;
        			try{f=l.get(tempId);}
        			catch(Exception e) {
        				addflight();
        				f=l.get(tempId);
        			}
        			String s1= items[2];
        			  s1 = s1.replace("[","");
        			 s1 = s1.replace("]","");
        			 s1 = s1.replaceAll(" ","");
        			 List<String> myList = new ArrayList<String>(Arrays.asList(s1.split(",")));
        			List<Integer> seats = new ArrayList<Integer>(Arrays.asList());
        			for(String s : myList) {
        				seats.add(Integer.parseInt(s));
        			}
        			
        		    f.makeBooking(seats, Double.parseDouble(items[3]),(items[4].equals("Yes")),(seats.get(0)<6),(items[5].equals("Confirm")));
        		    newRecord = buf.readLine();
        		}
        	
        	}
        	catch(Exception e) {
        		System.out.println(e.getMessage());
        	}
        }
        
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int bookingid,ch=1;
		int noOfSeats;
		int Pref;
		boolean mealPref,classPref;
		FlightDetails f;
		
		
		readFromFile();
			
	while(ch!=0) {
		System.out.print("1.addFilght 2.checkAvailablity \n"
				+ "3.BookTicket 4.cancelTicket 5.mealPref\n 6.BookingSummary"
				+ " 7.Avialble FlightNo 8.Booking Details or enter 0 to end");
		 ch = input.nextInt();
		 switch(ch) {
		 	case 0:
			try {
				addToFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 		break;
		 	case 1:
		 		addflight();
		 		break;
		 	case 2:
		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println(f.availability());
		 		break;
		 		
		 	case 3:
		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println("Enter no of seats");
		 		noOfSeats = input.nextInt();
		 		System.out.println("Enter 1 for Business 0 for Economy");
		 		System.out.println("seat no 0-5 Business class 6-14 EconomyClass");
		 		Pref = input.nextInt();
		 		classPref = (Pref==1)? true:false;
		 		System.out.println("Enter 1 for meal 0 for not");
		 		Pref = input.nextInt();
		 		mealPref = (Pref==1)? true:false;
		 		f.bookTicket(noOfSeats, classPref, mealPref);
		 		break;
		 	case 4:

		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println("Enter the booking id");
		 		bookingid = input.nextInt();
		 		f.cancelBooking(bookingid);
		 		break;
		 	case 5:
		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println("Meal Prefernce"+f.mealPref());
		 		break;
		 	case 6:
		 		f= flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		f.displayBookingSummary();
		 		break;
		 	case 7:
		 		System.out.println(availableFlight());
		 		break;
		 	case 8:
		 		 System.out.println("Enter the booking ID");
		 		 bookingid = input.nextInt();
		 		 bookingDetail(bookingid);
		 		 
		 		 break;
		 	default:
		 		System.out.println("invalid choice");
		 		
		 }
		 
	}
	input.close();
	}

}
