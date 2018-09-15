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

/**
 * These test cases were derived from a cause-effect graph
 * @author Corey
 *
 */
@ExtendWith(MockitoExtension.class)
class BookingCreditDetailsEnteredTests
{
	@Mock Hotel hotel;
	@Mock Guest guest;
	@Mock Room room;
	@Mock Date arrivalDate;
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
