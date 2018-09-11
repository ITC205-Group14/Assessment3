package hotel.entities.booking;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;
import hotel.entities.Booking;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;

@ExtendWith(MockitoExtension.class)
class BookingCheckInTests
{
	@Mock Hotel hotel;


	@Mock Guest guest;
	@Mock Room room = new Room(0, RoomType.DOUBLE);
	@Mock CreditCard creditCard;
	Date arrivalDate = new Date();
	int stayLength = 1;
	int numberOfOccupants = 1;
	@InjectMocks Booking booking = new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);

	@BeforeEach
	void init()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void bookingCheckinWithValidState()
	{
		//Arrange
		assertTrue(booking.isPending());

		//Act
		booking.checkIn();

		//Assert
		Mockito.verify(room).checkin();
		assertTrue(booking.isCheckedIn());
	}

}
