package hotel.entities.hotel;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Spy;

import hotel.entities.Booking;
import hotel.entities.Hotel;

class HotelCheckOutTests
{
	@Spy Hotel hotel = new Hotel();

	@Mock Booking booking;

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

}
