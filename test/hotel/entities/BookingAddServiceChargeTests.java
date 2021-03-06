package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;

@ExtendWith(MockitoExtension.class)
class BookingAddServiceChargeTests
{
	@Mock Hotel hotel;

	@Mock Guest guest;
	@Mock CreditCard creditCard;
	@Mock Room room;

	Date arrivalDate = new Date();
	int stayLength = 1;
	int numberOfOccupants = 1;

	@InjectMocks Booking booking = new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);

	//Unit tests
	@Test
	void bookingAddServiceChargeValidCharges()
	{
		//Arrange
		assertEquals(booking.getCharges().size(), 0);

		//Act
		booking.addServiceCharge(ServiceType.BAR_FRIDGE, 30);
		booking.addServiceCharge(ServiceType.RESTAURANT, 30);
		booking.addServiceCharge(ServiceType.ROOM_SERVICE, 30);

		//Assert
		assertEquals(3, booking.getCharges().size());
	}

	@Test
	void bookingAddServiceChargeNullCharge()
	{
		//Arrange
		assertEquals(0, booking.getCharges().size());

		//Act
		booking.addServiceCharge(null, 30);

		//Assert
		assertEquals(1, booking.getCharges().size());
	}

	@Test
	void bookingAddServiceNegativeCharge()
	{
		//Arrange
		assertEquals(0, booking.getCharges().size());

		//Act
		booking.addServiceCharge(ServiceType.BAR_FRIDGE, -1);

		//Assert
		assertEquals(1, booking.getCharges().size());
	}

	@Test
	void bookingAddServiceHugeCharge()
	{
		//Arrange
		assertEquals(0, booking.getCharges().size());

		//Act
		booking.addServiceCharge(ServiceType.BAR_FRIDGE, Integer.MAX_VALUE);

		//Assert
		assertEquals(1, booking.getCharges().size());
	}
}
