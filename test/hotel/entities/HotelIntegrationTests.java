package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;

@ExtendWith(MockitoExtension.class)
class HotelIntegrationTests
{

	Hotel hotel = new Hotel();

	//Create a REAL booking
	@Mock Guest guest;
	Room room = new Room(0, RoomType.DOUBLE);
	@Mock CreditCard creditCard;
	Date arrivalDate = new Date();
	int stayLength = 1;
	int numberOfOccupants = 1;

	@Spy @InjectMocks Booking booking = new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);

	//Add Service Charge ---------------------------------------------------------------------
	@Test
	void hotelAddServiceChargeInvalidRoomIdRealBooking()
	{
		//Arrange
		int roomId = 99999;

		//Act
		Executable test = () -> hotel.addServiceCharge(roomId, ServiceType.BAR_FRIDGE, 30);

		//Assert
		assertThrows(RuntimeException.class, test);
	}

	@Test
	void hotelCheckAllServiceTypesRealBooking()
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
	void hotelAddServiceChargeWithValidRoomIdRealBooking()
	{
		//Arrange
		int roomId = 0;
		hotel.activeBookingsByRoomId.put(0, booking);

		//Act
		hotel.addServiceCharge(roomId, ServiceType.BAR_FRIDGE, 30);

		//Assert
		verify(booking).addServiceCharge(ServiceType.BAR_FRIDGE, 30);
	}

	@Test
	void hotelAddServiceChargeWithMaxCostRealBooking()
	{
		//Arrange
		int roomId = 0;
		hotel.activeBookingsByRoomId.put(0, booking);
		double cost = Double.MAX_VALUE;

		//Act
		hotel.addServiceCharge(roomId, ServiceType.BAR_FRIDGE, cost);

		//Assert
		verify(booking).addServiceCharge(ServiceType.BAR_FRIDGE, cost);
	}

	@Test
	void hotelAddServiceChargeWithMinDoubleCostRealBooking()
	{
		//Arrange
		int roomId = 0;
		hotel.activeBookingsByRoomId.put(0, booking);
		double cost = Double.MIN_VALUE;

		//Act
		hotel.addServiceCharge(roomId, ServiceType.BAR_FRIDGE, cost);

		//Assert
		verify(booking).addServiceCharge(ServiceType.BAR_FRIDGE, cost);
	}

	@Test
	void hotelAddServiceInvalidServiceTypeRealBooking()
	{
		//Arrange
		int roomId = 0;
		hotel.activeBookingsByRoomId.put(0, booking);
		double cost = 20;

		//Just use a null
		ServiceType FREAKISHLY_SMALL_WINE = null;

		//Act
		hotel.addServiceCharge(roomId, FREAKISHLY_SMALL_WINE, cost);

		//Assert
		verify(booking).addServiceCharge(FREAKISHLY_SMALL_WINE, cost);
	}

	//Hotel Book---------------------------------------------------------------------

	@Test
	void bookConfirmationNumberRealRoom()
	{
		//Arrange
		room = new Room(0, RoomType.SINGLE);
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertEquals(expected, confirmationNumber);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
	}

	@Test
	void bookConfirmationNumberRealRoomRealGuest()
	{
		//Arrange
		room = new Room(0, RoomType.SINGLE);
		guest = new Guest("Barry", "White", 555555555);
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertEquals(expected, confirmationNumber);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
	}

	@Test
	void bookConfirmationNumberRealRoomRealGuestRealCreditCard()
	{
		//Arrange
		room = new Room(0, RoomType.SINGLE);
		guest = new Guest("Barry", "White", 555555555);
		creditCard = new CreditCard(CreditCardType.MASTERCARD, 1, 1);
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertEquals(expected, confirmationNumber);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
	}

	//Hotel Checkin--------------------------------------------------------------------------------

	@Test
	void hotelCheckinWithValidConfirmationNumberRealBooking()
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
	void hotelCheckinWithNegativeConfirmationNumberRealBooking()
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
	void hotelCheckinWithMaxLongConfirmationNumberRealBooking()
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
	void hotelCheckinWithOverflowingMaxLongConfirmationNumberRealBooking()
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
	void hotelCheckinWithMinLongConfirmationNumberRealBooking()
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
	void hotelCheckinWithOverflowingMinLongConfirmationNumberRealBooking()
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

	//Hotel Checkout -----------------------------------------------------------------

	@Test
	void hotelCheckoutWithValidRoomIdRealBooking()
	{
		//Arrange
		int roomId = 0;
		booking.checkIn();
		hotel.activeBookingsByRoomId.put(roomId, booking);

		//Act
		hotel.checkout(roomId);

		//Assert
		verify(booking).checkOut();
	}

	@Test
	void hotelCheckoutWithMaxIntRoomIdRealBooking()
	{
		//Arrange
		int roomId = Integer.MAX_VALUE;
		booking.checkIn();
		hotel.activeBookingsByRoomId.put(roomId, booking);

		//Act
		hotel.checkout(roomId);

		//Assert
		verify(booking).checkOut();
	}

	@Test
	void hotelCheckoutWithMinIntRoomIdRealBooking()
	{
		//Arrange
		int roomId = Integer.MIN_VALUE;
		booking.checkIn();
		hotel.activeBookingsByRoomId.put(roomId, booking);

		//Act
		hotel.checkout(roomId);

		//Assert
		verify(booking).checkOut();
	}


}
