package hotel.entities;

import java.util.Date;

import hotel.credit.CreditCard;

public class BookingHelper {

	private static BookingHelper self;
	
	public static synchronized BookingHelper getInstance() {
		if (self == null) {
			self = new BookingHelper();
		}
		return self;
		
	}


	public Booking makeBooking(Guest guest, Room room, 
			Date arrivalDate, int stayLength, 
			int numberOfOccupants, 
			CreditCard creditCard) {
		
		Booking booking = new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);
		return booking;
	}
}
