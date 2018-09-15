package hotel.booking;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;

@ExtendWith(MockitoExtension.class)
class BookingCreditDetailsEnteredTests
{
	@Mock Hotel hotel;
	@Mock Guest guest;
	@Mock Room room;
	@Mock Date arrivalDate;
	@Mock BookingUI bookingUI;

	@Spy @InjectMocks BookingCTL bookingCTL = new BookingCTL(hotel);

	/**
	 * These test cases were derived from a cause-effect graph
	 * @author Corey
	 *
	 */
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

	//END Cause-effect tests
	//Some tests aren't captured inside a cause and effect graph
	//The following tests are Ad Hoc

	@Test
	void testCreditDetailsInvalidStartingState()
	{
		//Arrange
		CreditCardType cct = CreditCardType.MASTERCARD;
		int cardNumber = 4;
		int ccv = 1;
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.COMPLETED);

		//Act
		Executable e = () -> bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert
		assertThrows(RuntimeException.class, e);
	}

	//And the following tests are boundary tests
	//We know the boundary for number is 5
	//One of the boundaries (6) was covered in an above cause-effect test

	@Test
	void testCreditDetailsCardBoundary()
	{
		//Arrange
		CreditCardType cct = CreditCardType.MASTERCARD;
		int cardNumber = 5;
		int ccv = 1;
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Act
		bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert
		verify(bookingUI).setState(BookingUI.State.COMPLETED);
	}

}
