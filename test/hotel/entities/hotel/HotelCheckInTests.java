package hotel.entities.hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.entities.Booking;
import hotel.entities.Hotel;

@ExtendWith(MockitoExtension.class)
class HotelCheckInTests
{
	@Spy Hotel hotel = new Hotel();

	@Mock Booking booking;

	@Test
	void hotelCheckinWithInvalidConfirmationNumber()
	{
		//Arrange
		int confirmationNumber = 99999;

		//Act
		Executable test = () -> hotel.checkin(confirmationNumber);

		//Assert
		assertThrows(RuntimeException.class, test);
	}

	@Test
	void hotelCheckinWithValidConfirmationNumber()
	{
		//Arrange
		long confirmationNumber = 5L;
		hotel.bookingsByConfirmationNumber.put(confirmationNumber, booking);
		assertTrue(hotel.activeBookingsByRoomId.size() == 0);

		//Act
		hotel.checkin(confirmationNumber);

		//Assert
		verify(booking).checkIn();
		assertEquals(hotel.activeBookingsByRoomId.size(), 1);
	}

}
