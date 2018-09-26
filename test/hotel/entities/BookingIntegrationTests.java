package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;
import hotel.entities.Booking.State;

@ExtendWith(MockitoExtension.class)
class BookingIntegrationTests
{
	@Mock Hotel hotel;

	@Mock Guest guest;
	Room room = new Room(0, RoomType.DOUBLE);
	@Mock CreditCard creditCard;

	Date arrivalDate = new Date();
	int stayLength = 1;
	int numberOfOccupants = 1;

	@Spy @InjectMocks Booking booking = new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);

	@Test
	void bookingCheckinWithValidStateRealRoom()
	{
		//Arrange
		when(booking.getState()).thenReturn(State.PENDING);
		assertTrue(booking.isPending());

		//Act
		booking.checkIn();

		//Assert
		when(booking.getState()).thenCallRealMethod();
		assertTrue(booking.isCheckedIn());
		assertTrue(room.isOccupied());
	}

	@Test
	void bookingCheckinWithInvalidStateRealRoom()
	{
		//Arrange
		when(booking.getState()).thenReturn(State.CHECKED_IN);
		assertFalse(booking.isPending());

		//Act
		Executable e = () -> booking.checkIn();

		//Assert
		assertThrows(RuntimeException.class, e);
	}

	@Test
	void bookingCheckOutWithValidStateRealRoom()
	{
		//Arrange
		when(booking.getState()).thenReturn(State.CHECKED_IN);
		assertTrue(booking.isCheckedIn());

		//Act
		booking.checkOut();

		//Assert
		when(booking.getState()).thenCallRealMethod();
		assertTrue(booking.isCheckedOut());
		assertTrue(room.isReady());
	}

	@Test
	void bookingCheckOutWithInvalidStateRealRoom()
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
