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
import hotel.entities.ServiceType;

class HotelAddServiceChargeTests
{
	Hotel hotel = new Hotel();

	@Mock Booking booking;

	@BeforeEach
	void init()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void hotelAddServiceChargeInvalidRoomId()
	{
		//Arrange
		int roomId = 99999;

		//Act
		Executable test = () -> hotel.addServiceCharge(roomId, ServiceType.BAR_FRIDGE, 30);

		//Assert
		assertThrows(RuntimeException.class, test);
	}

	@Test
	void hotelCheckAllServiceTypes()
	{
		//Arrange
		int roomId = 0;
		hotel.activeBookingsByRoomId.put(0, booking);

		//Act
		hotel.addServiceCharge(roomId, ServiceType.BAR_FRIDGE, 30);
		hotel.addServiceCharge(roomId, ServiceType.RESTAURANT, 30);
		hotel.addServiceCharge(roomId, ServiceType.ROOM_SERVICE, 30);

		//Assert
		verify(booking).addServiceCharge(ServiceType.BAR_FRIDGE, 30);
		verify(booking).addServiceCharge(ServiceType.RESTAURANT, 30);
		verify(booking).addServiceCharge(ServiceType.ROOM_SERVICE, 30);
	}

	@Test
	void hotelAddServiceChargeWithValidRoomId()
	{
		//Arrange
		int roomId = 0;
		hotel.activeBookingsByRoomId.put(0, booking);

		//Act
		hotel.addServiceCharge(roomId, ServiceType.BAR_FRIDGE, 30);

		//Assert
		verify(booking).addServiceCharge(ServiceType.BAR_FRIDGE, 30);
	}

}