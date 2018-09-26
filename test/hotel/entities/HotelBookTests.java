package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;

@ExtendWith(MockitoExtension.class)
class HotelBookTests
{
	Hotel hotel = new Hotel();

	@Mock Room room;
	@Mock Guest guest;
	@Mock CreditCard creditCard;

	/**
	 * Tests the booking number confirmation format
	 * with a leading zero is formatted correctly
	 */
	@Test
	void bookConfirmationNumberFormatLeadingZero()
	{
		//Arrange
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
	void bookConfirmationInvalidDate()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(500000000, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		long expected = 105000000000L;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		//We expect an overflow
		assertNotEquals(expected, confirmationNumber);
		//But, because of the code it should still be added
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
	}

	@Test
	void bookConfirmationNullRoom()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		Executable e = () -> hotel.book(null, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertThrows(NullPointerException.class, e);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);
	}

	/**
	 * Doesnt fail because GUEST is only passed
	 * to further commands
	 */
	@Test
	void bookConfirmationNullGuest()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long conf = hotel.book(room, null, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertEquals(expected, conf);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
		assertTrue(hotel.bookingsByConfirmationNumber.containsKey(conf));
	}

	@Test
	void bookConfirmationNullCreditCard()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long conf = hotel.book(room, null, arrivalDate, stayLength, occupantNumber, null);

		//Assert
		assertEquals(expected, conf);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
		assertTrue(hotel.bookingsByConfirmationNumber.containsKey(conf));
	}

	@Test
	void bookConfirmationNegativeOccupants()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = -1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long conf = hotel.book(room, null, arrivalDate, stayLength, occupantNumber, null);

		//Assert
		assertEquals(expected, conf);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
		assertTrue(hotel.bookingsByConfirmationNumber.containsKey(conf));
	}

	@Test
	void bookConfirmationIntMaxOccupants()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = Integer.MAX_VALUE;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long conf = hotel.book(room, null, arrivalDate, stayLength, occupantNumber, null);

		//Assert
		assertEquals(expected, conf);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
		assertTrue(hotel.bookingsByConfirmationNumber.containsKey(conf));
	}

	@Test
	void bookConfirmationIntMinOccupants()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = Integer.MIN_VALUE;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long conf = hotel.book(room, null, arrivalDate, stayLength, occupantNumber, null);

		//Assert
		assertEquals(expected, conf);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
		assertTrue(hotel.bookingsByConfirmationNumber.containsKey(conf));
	}

	@Test
	void bookConfirmationNegativeStayLength()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int occupantNumber = 1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long conf = hotel.book(room, guest, arrivalDate, -1, occupantNumber, creditCard);

		//Assert
		assertEquals(expected, conf);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
		assertTrue(hotel.bookingsByConfirmationNumber.containsKey(conf));
	}

	@Test
	void bookConfirmationIntMaxStayLength()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int occupantNumber = 1;
		long expected = 1020180;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		//Act
		long conf = hotel.book(room, guest, arrivalDate, Integer.MAX_VALUE, occupantNumber, creditCard);

		//Assert
		assertEquals(expected, conf);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 1);
		assertTrue(hotel.bookingsByConfirmationNumber.containsKey(conf));
	}

	@Test
	void bookConfirmationMaxIntRoomNumber()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(Integer.MAX_VALUE, 01, 01).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		int roomId = Integer.MAX_VALUE;
		RoomType roomType = RoomType.SINGLE;
		Room room = new Room(roomId, roomType);

		//Act
		Executable e = () -> hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertThrows(NumberFormatException.class, e);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);
	}

	@Test
	void bookConfirmationMinIntRoomNumber()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(Integer.MAX_VALUE, 01, 01).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);

		int roomId = Integer.MIN_VALUE;
		RoomType roomType = RoomType.SINGLE;
		Room room = new Room(roomId, roomType);

		//Act
		Executable e = () -> hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertThrows(NumberFormatException.class, e);
		assertEquals(hotel.bookingsByConfirmationNumber.size(), 0);
	}

}
