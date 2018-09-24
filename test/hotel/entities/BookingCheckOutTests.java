package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;
import hotel.entities.Booking.State;

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
	@Spy @InjectMocks Booking booking = new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);

	@BeforeEach
	void init()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void bookingCheckOutWithValidState()
	{
		//Arrange
		when(booking.getState()).thenReturn(State.CHECKED_IN);
		assertTrue(booking.isCheckedIn());

		//Act
		booking.checkOut();

		//Assert
		verify(room).checkout(booking);
		when(booking.getState()).thenCallRealMethod();
		assertTrue(booking.isCheckedOut());
	}

	@Test
	void bookingCheckOutWithInvalidState()
	{
		//Arrange
		when(booking.getState()).thenReturn(State.PENDING);
		assertFalse(booking.isCheckedIn());

		//Act
		Executable e = () -> booking.checkOut();

		//Assert
		assertThrows(RuntimeException.class, e);
	}

}
