package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
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
class BookingCheckInTests
{
	@Mock Hotel hotel;


	@Mock Guest guest;
	@Mock Room room;
	@Mock CreditCard creditCard;

	Date arrivalDate = new Date();
	int stayLength = 1;
	int numberOfOccupants = 1;

	@Spy @InjectMocks Booking booking = new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);

	@Test
	void bookingCheckinWithValidState()
	{
		//Arrange
		when(booking.getState()).thenReturn(State.PENDING);
		assertTrue(booking.isPending());

		//Act
		booking.checkIn();

		//Assert
		verify(room).checkin();
		when(booking.getState()).thenCallRealMethod();
		assertTrue(booking.isCheckedIn());
	}

	@Test
	void bookingCheckinWithInvalidState()
	{
		//Arrange
		when(booking.getState()).thenReturn(State.CHECKED_IN);
		assertFalse(booking.isPending());

		//Act
		Executable e = () -> booking.checkIn();

		//Assert
		assertThrows(RuntimeException.class, e);
	}

}
