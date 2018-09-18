package hotel.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hotel.credit.CreditCard;
import hotel.utils.IOUtils;

public class Room {
	
	private enum State {READY, OCCUPIED}
	
	int id;
	RoomType roomType;
	List<Booking> bookings;
	State state;

	
	public Room(int id, RoomType roomType) {
		this.id = id;
		this.roomType = roomType;
		bookings = new ArrayList<>();
		state = State.READY;
	}
	

	public String toString() {
		return String.format("Room : %d, %s", id, roomType);
	}


	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return roomType.getDescription();
	}
	
	
	public RoomType getType() {
		return roomType;
	}
	
	public boolean isAvailable(Date arrivalDate, int stayLength) {
		IOUtils.trace("Room: isAvailable");
		for (Booking b : bookings) {
			if (b.doTimesConflict(arrivalDate, stayLength)) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean isReady() {
		return state == State.READY;
	}


	public Booking book(Guest guest, Date arrivalDate, int stayLength, int numberOfOccupants, CreditCard creditCard) {
		Booking booking = new Booking(guest, null, arrivalDate, numberOfOccupants, numberOfOccupants, creditCard);
		bookings.add(booking);
		return booking;		
	}
	

	public void checkin() {
		if (state != State.READY) {
			throw new RuntimeException("Room is not ready, unable to check-in");
		}				
		state = State.OCCUPIED;		
}


	public void checkout(Booking booking) {
		if (state != State.OCCUPIED) {
			throw new RuntimeException("Room is not occupied, unable to checkout");
		}			
		bookings.remove(booking);
		state = State.READY;
	}

}
