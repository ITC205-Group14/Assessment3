package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.entities.Booking;
import hotel.entities.Hotel;

@ExtendWith(MockitoExtension.class)
class HotelCheckInTests
{
	//This doesn't need to be a spy because
	//we can directly put bookings into the
	//maps inside hotel. We can safely assume
	//the Java Map interface is thoroughly tested
	Hotel hotel = new Hotel();

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

	//Boundary tests

	@Test
	void hotelCheckinWithNegativeConfirmationNumber()
	{
		//Arrange
		long confirmationNumber = -1L;
		hotel.bookingsByConfirmationNumber.put(confirmationNumber, booking);
		assertTrue(hotel.activeBookingsByRoomId.size() == 0);

		//Act
		hotel.checkin(confirmationNumber);

		//Assert
		verify(booking).checkIn();
		assertEquals(hotel.activeBookingsByRoomId.size(), 1);
	}

	@Test
	void hotelCheckinWithMaxLongConfirmationNumber()
	{
		//Arrange
		long confirmationNumber = Long.MAX_VALUE;
		hotel.bookingsByConfirmationNumber.put(confirmationNumber, booking);
		assertTrue(hotel.activeBookingsByRoomId.size() == 0);

		//Act
		hotel.checkin(confirmationNumber);

		//Assert
		verify(booking).checkIn();
		assertEquals(hotel.activeBookingsByRoomId.size(), 1);
	}

	/**
	 * Wraps to a negative number
	 * If we are to follow the "Art of Software Testing"
	 * then this test also reasonably proves
	 * this test works with Long.MIN_VALUE, and we can assume
	 * Long.MIN_VALUE - 1 (Wrapping to MAX value) but I am
	 * going to test them anyway.
	 */
	@Test
	void hotelCheckinWithOverflowingMaxLongConfirmationNumber()
	{
		//Arrange
		long confirmationNumber = Long.MAX_VALUE + 1;
		assertEquals(confirmationNumber, Long.MIN_VALUE);
		hotel.bookingsByConfirmationNumber.put(confirmationNumber, booking);
		assertTrue(hotel.activeBookingsByRoomId.size() == 0);

		//Act
		hotel.checkin(confirmationNumber);

		//Assert
		verify(booking).checkIn();
		assertEquals(hotel.activeBookingsByRoomId.size(), 1);
	}

	@Test
	void hotelCheckinWithMinLongConfirmationNumber()
	{
		//Arrange
		long confirmationNumber = Long.MIN_VALUE;
		hotel.bookingsByConfirmationNumber.put(confirmationNumber, booking);
		assertTrue(hotel.activeBookingsByRoomId.size() == 0);

		//Act
		hotel.checkin(confirmationNumber);

		//Assert
		verify(booking).checkIn();
		assertEquals(hotel.activeBookingsByRoomId.size(), 1);
	}

	@Test
	void hotelCheckinWithOverflowingMinLongConfirmationNumber()
	{
		//Arrange
		long confirmationNumber = Long.MIN_VALUE - 1;
		assertEquals(confirmationNumber, Long.MAX_VALUE);
		hotel.bookingsByConfirmationNumber.put(confirmationNumber, booking);
		assertTrue(hotel.activeBookingsByRoomId.size() == 0);

		//Act
		hotel.checkin(confirmationNumber);

		//Assert
		verify(booking).checkIn();
		assertEquals(hotel.activeBookingsByRoomId.size(), 1);
	}

}
