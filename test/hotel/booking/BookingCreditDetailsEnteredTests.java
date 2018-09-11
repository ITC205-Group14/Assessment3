package hotel.booking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.booking.BookingCTL.State;
import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;

@ExtendWith(MockitoExtension.class)
class BookingCreditDetailsEnteredTests
{
	@Mock Hotel hotel;

	@Mock (name = "guest")
	Guest guest = new Guest("Name", "Address", 0);

	@Mock (name = "room")
	Room room = new Room(0, RoomType.SINGLE);

	@Mock (name = "arrivalDate")
	Date arrivalDate = new Date();

	@InjectMocks BookingCTL bookingCTL = new BookingCTL(hotel);

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.initMocks(this);
		bookingCTL.setState(State.CREDIT);
	}

	@Test
	void testCreditDetailsEnteredInvalidCard()
	{
		//Arrange
		CreditCardType cct = CreditCardType.VISA;

		//Act
		bookingCTL.creditDetailsEntered(cct, 6, 1);

		//Assert
		assertFalse(bookingCTL.isCompleted());
	}

	@Test
	void testCreditDetailsEnteredValidCard()
	{
		//Arrange
		CreditCardType cct = CreditCardType.VISA;

		//Act
		bookingCTL.creditDetailsEntered(cct, 4, 1);

		//Assert
		assertTrue(bookingCTL.isCompleted());
	}

}
