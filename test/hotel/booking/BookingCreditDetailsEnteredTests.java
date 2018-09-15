package hotel.booking;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;

/**
 * These test cases were derived from a cause-effect graph
 * @author Corey
 *
 */
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

	@Mock BookingUI bookingUI;

	@Spy @InjectMocks BookingCTL bookingCTL = new BookingCTL(hotel);

	@Test
	void testCreditDetailsEnteredInvalidCard()
	{
		//Arrange
		CreditCardType cct = CreditCardType.VISA;
		int cardNumber = 6;
		int ccv = 1;
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Act
		bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert
		verify(bookingUI).displayMessage("Credit Card could not be authorized");
		verifyNoMoreInteractions(bookingUI);
	}

	@Test
	void testCreditDetailsEnteredValidVisaCard()
	{
		//Arrange
		CreditCardType cct = CreditCardType.VISA;
		int cardNumber = 4;
		int ccv = 1;
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Act
		bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert
		verify(bookingUI).setState(BookingUI.State.COMPLETED);
	}

	@Test
	void testCreditDetailsEnteredValidMasterCard()
	{
		//Arrange
		CreditCardType cct = CreditCardType.MASTERCARD;
		int cardNumber = 4;
		int ccv = 1;
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Act
		bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert
		verify(bookingUI).setState(BookingUI.State.COMPLETED);
	}

}
