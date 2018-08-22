package FBS;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FlightDetails implements Serializable {
	private static int count = 100;
	private static int noOfBook = 0;
	
	private int FlightNo;
	private boolean[] seat = new boolean[15];
	private static final int MEAL_COST = 200;
	private static int BASE_FARE_BIS = 2000;
	private static int BASE_FARE_ECO = 1000;
	private Booking bookingRecord;
	FlightDetails(){
		bookingRecord=null;
		count++;
		this.FlightNo = count;
	}
	public int getFlightNo() {
		return FlightNo;
	}
  public boolean[] getSeat() {
		return seat;
	}
    private static int getBaseFareEco() {
    	return BASE_FARE_ECO;
    }
    private static int getBaseFareBis() {
    	return BASE_FARE_BIS;
    }
	public Booking getBookingRecord() {
		return bookingRecord;
	}

	private void setBookingRecord(Booking bookingRecord) {
		this.bookingRecord = bookingRecord;
	}

public class Booking{
	  private int BookId;
	  private List<Integer> seatsBooked = new LinkedList<Integer>();
	  private double cost;
	  private boolean mealPreference = false;
	  private boolean status = true;
	  private Booking next = null;
	 public  Booking(List<Integer> seats,double cost,boolean meal){
		  this.seatsBooked = seats;
		  this.cost = cost;
		  this.mealPreference = meal;
		  
		 noOfBook++;
		 BookId = noOfBook;
	  }
	 public void setStatus(boolean state) {
		 this.status = state;
	 }
	 public int getBookId() {
		 return BookId;
	 }
	public List<Integer> getSeatsBooked() {
		return seatsBooked;
	}
	public void setSeatsBooked(List<Integer> seatsBooked) {
		this.seatsBooked = seatsBooked;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public boolean isMealPreference() {
		return mealPreference;
	}
	public void setMealPreference(boolean mealPreference) {
		this.mealPreference = mealPreference;
	}
	public Booking getNext() {
		return next;
	}
	public void setNext(Booking next) {
		this.next = next;
	}
	public boolean getStatus() {
		return status; 
	}
}
 public boolean displayBookingSummary(int bookId) {
	 Booking br = getBooking(bookId);
	 if(br!=null) {
			 	 System.out.println("booking summary");
				 System.out.println("Booking Id: "+br.BookId+" Cost:"+br.getCost()+
					 " MealPref: "+ br.isMealPreference()+ "Seats:"+br.getSeatsBooked()+"Status :"+(br.getStatus()? "Confirm":"Canceled"));
				 return true;
	 
	 		}
	return false;
 }

	public void displayBookingSummary() {
	 Booking br = getBookingRecord();
	 if(br==null) {
		 System.out.println("no booking made");
	 }
	 else {
		 System.out.println("booking summary");
		 double total=0.0;
		do  {
			 
				 System.out.println("Booking Id: "+br.BookId+" Cost:"+br.getCost()+
					 " MealPref: "+ br.isMealPreference()+ "Seats:"+br.getSeatsBooked()+"Status :"+(br.getStatus()? "Confirm":"Canceled"));
				 if(br.getStatus())
					 total += br.getCost();
				 br=br.getNext();
			 
		 }while(br!=null);
		System.out.println("Total income :" +total);
	 }
 }
	static public void updateBaseFare(boolean classPref) {
    	if(classPref)
    		BASE_FARE_BIS+=200;
    	else
    		BASE_FARE_ECO+=100;
    }
	public Booking getBooking(int id) {
		Booking br = getBookingRecord();
		 if(br==null) {
			 System.out.println("no booking made");
		 }
		 else {
			 while(br!=null) {
				 
					 if(br.BookId==id)
						 return br;
					 br=br.getNext();
				 
			 }
		 }
		 return null;
	}
	public void cancelBooking(int id) {
		Booking br = this.getBooking(id);
		if(br.status) {
			br.status=false;
			for(int i:br.seatsBooked)
				this.seat[i]=false;
		}
	}
	
	public void makeBooking(List<Integer> seats,double cost,boolean mealPref,boolean classPref,boolean status) {
		Booking br = this.getBookingRecord();
		Booking newBook=new Booking(seats,cost,mealPref); 
		newBook.setStatus(status);
		for(int i:seats) {
			this.seat[i]=true;
		}
		if(br!=null) {
			while(br.next!=null) {
				br=br.next;
			}
			br.next = newBook;
		}
		else {
			this.setBookingRecord(newBook);
			System.out.println("new entry");
		}
		
		updateBaseFare(classPref);
	}
 public void bookTicket(int n,boolean classPref,boolean mealPref) {
	 List<Integer> avail = seatsAvailablity(classPref);
	 List<Integer> temp = new ArrayList<Integer>();
	 double tcost=0;
	 if(avail.size()<n) {
		   System.out.println("specified no of seats is not available");
		   System.out.println("no of seats available for the prefered class is="+avail.size());
	   }
	   else
	   {
		   Scanner in = new Scanner(System.in);
		   int seat;
		   while(n>0) {
			   System.out.println("enter the prefered seat no");
			   seat = in.nextInt();
			   if(avail.contains(seat)) {
				   temp.add(seat);
				   n--;
			   }
			   else 
				   System.out.println("seat "+seat+" is not available select someother seat"); 
		 }
		   if(classPref) {
			   tcost += getBaseFareBis()*temp.size();
		   }
		   else {
			   tcost += getBaseFareEco()*temp.size();
		   }
		 if(mealPref) {
			 tcost += MEAL_COST*temp.size();
		 }
		 makeBooking(temp,tcost,mealPref,classPref,true);
		 
	   }
	 
 }
   public List<Integer> seatsAvailablity(boolean classPref) {
	  
	  return (classPref?(this.bisAvailability()):(this.ecoAvailability()));
	   
	   
   }
	public List<Integer> mealPref(){
		List<Integer> meal = new ArrayList<Integer>();
		Booking b= this.getBookingRecord();
		while(b!=null) {
			if(b.getStatus() && b.isMealPreference()) {
				for(Integer l:b.seatsBooked) {
					meal.add(l);
				}
			}
			b=b.next;
		}
		return meal;
	}
	
	public List<Integer> ecoAvailability() {
		List<Integer> avail = new ArrayList<Integer>();
			for(int i=6;i<15;i++) {
				if(!this.seat[i])
					avail.add(i);
			}
	 return avail;
	}
	
	
	public List<Integer> bisAvailability() {
	 List<Integer> avail = new ArrayList<Integer>();
	 	for(int i=0;i<5;i++) {
	 		if(!this.seat[i])
	 			avail.add(i);
	 	}
	 	return avail;
	}
	
	public List<Integer> availability() {
		List<Integer> avail = new ArrayList<Integer>();
		for(int i=0;i<15;i++) {
			if(!this.seat[i])
				avail.add(i);
		}
		return avail;
	}
}
