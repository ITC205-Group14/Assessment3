package hotel.entities.hotel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hotel.entities.Booking;
import hotel.entities.Hotel;

class HotelCheckOutTests
{
	Hotel hotel = new Hotel();

	@Mock Booking booking;

	@BeforeEach
	void init()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void hotelCheckoutWithInvalidRoomId()
	{
		//Arrange
		int roomId = 99999;

		//Act
		Executable test = () -> hotel.checkout(roomId);

		//Assert
		assertThrows(RuntimeException.class, test);
	}

	@Test
	void hotelCheckoutWithValidRoomId()
	{
		//Arrange
		int roomId = 0;
		hotel.activeBookingsByRoomId.put(0, booking);

		//Act
		hotel.checkout(roomId);

		//Assert
		verify(booking).checkOut();
	}

}
