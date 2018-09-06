package hotel.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;

@ExtendWith(MockitoExtension.class)
class HotelTest
{
	Hotel hotel = new Hotel();

	int roomId = 123;
	RoomType roomType = RoomType.SINGLE;
	@InjectMocks Room room = new Room(roomId, roomType);

	@Mock Guest guest;
	@Mock CreditCard creditCard;

	@Test
	void bookConfirmationNumberFormat()
	{
		//Arrange
		Date arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		int stayLength = 3;
		int occupantNumber = 1;
		long expected = 1012018123;

		//Act
		long confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard);

		//Assert
		assertEquals(expected, confirmationNumber);
	}

}