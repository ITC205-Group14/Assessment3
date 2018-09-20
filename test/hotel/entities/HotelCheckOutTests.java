package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.entities.Booking;
import hotel.entities.Hotel;

@ExtendWith(MockitoExtension.class)
class HotelCheckOutTests
{
	@Spy Hotel hotel = new Hotel();

	@Mock Booking booking;

	@Test
	void hotelCheckoutWithInvalidRoomId()
	{
		//Arrange
		int roomId = 0;
		Mockito.when(hotel.findActiveBookingByRoomId(0)).thenReturn(null);

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
		hotel.activeBookingsByRoomId.put(roomId, booking);

		//Act
		hotel.checkout(roomId);

		//Assert
		verify(booking).checkOut();
	}

	@Test
	void hotelCheckoutWithMaxIntRoomId()
	{
		//Arrange
		int roomId = Integer.MAX_VALUE;
		hotel.activeBookingsByRoomId.put(roomId, booking);

		//Act
		hotel.checkout(roomId);

		//Assert
		verify(booking).checkOut();
	}

	@Test
	void hotelCheckoutWithMinIntRoomId()
	{
		//Arrange
		int roomId = Integer.MIN_VALUE;
		hotel.activeBookingsByRoomId.put(roomId, booking);

		//Act
		hotel.checkout(roomId);

		//Assert
		verify(booking).checkOut();
	}

}
