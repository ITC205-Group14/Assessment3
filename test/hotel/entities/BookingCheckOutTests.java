package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;
import hotel.entities.Booking;
import hotel.entities.Booking.State;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;

@ExtendWith(MockitoExtension.class)
class BookingCheckOutTests
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
	void bookingCheckOutWithValidState()
	{
		//Arrange
		booking.setState(State.CHECKED_IN);
		assertTrue(booking.isCheckedIn());

		//Act
		booking.checkOut();

		//Assert
		Mockito.verify(room).checkout(booking);
		assertTrue(booking.isCheckedOut());
	}

	@Test
	void bookingCheckOutWithInvalidState()
	{
		//Arrange
		booking.setState(State.PENDING);
		assertFalse(booking.isCheckedIn());

		//Act
		Executable e = () -> booking.checkOut();

		//Assert
		assertThrows(RuntimeException.class, e);
	}

}
