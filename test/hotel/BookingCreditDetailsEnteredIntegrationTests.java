package hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.booking.BookingCTL;
import hotel.booking.BookingUI;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardHelper;
import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;

@ExtendWith(MockitoExtension.class)
class BookingCreditDetailsEnteredIntegrationTests
{
	@Mock Hotel hotel;
	@Mock Guest guest;
	@Mock Room room;

	@Mock (name = "arrivalDate")
	Date arrivalDate = new Date();

	@Mock BookingUI bookingUI;
	@Mock CreditCardHelper creditCardHelper;

	CreditCardType cct = CreditCardType.VISA;
	int cardNumber = 1;
	int ccv = 1;

	@Mock CreditCard creditCard;

	@Mock CreditAuthorizer creditAuthorizer;

	@Spy @InjectMocks BookingCTL bookingCTL = new BookingCTL(hotel);


	/**
	 * @author Corey
	 *
	 */
	@Test
	void testCreditDetailsEnteredInvalidCardRealCard()
	{
		//Arrange
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);
		when(creditCardHelper.loadCreditCard(cct, cardNumber, ccv)).thenReturn(new CreditCard(cct, 5, 1));

		//Act
		bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert
		verify(bookingUI).displayMessage("Credit Card could not be authorized");
		verifyNoMoreInteractions(bookingUI);
	}

	//Equivalence partioning says this is the positive test case
	//verify all good outcomes in as little tests as possible
	//So this is huge
	@Test
	void testCreditDetailsEnteredValidCard()
	{
		//Arrange
		int cardNumber = 4;
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);
		when(creditCardHelper.loadCreditCard(cct, cardNumber, ccv))
		.thenReturn(creditCard);
		when(creditAuthorizer.authorize(any(CreditCard.class), anyDouble())).thenReturn(true);

		ArgumentCaptor<String> roomDescriptionCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Date> arrivalDateCaptor = ArgumentCaptor.forClass(Date.class);
		ArgumentCaptor<Integer> stayLengthCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> guestNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> ccVendorCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> creditCardNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Double> costNumberCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Long> confirmationNumberCaptor = ArgumentCaptor.forClass(Long.class);

		when(room.getDescription()).thenReturn("D");
		when(room.getId()).thenReturn(1);
		when(guest.getName()).thenReturn("N");
		when(creditCard.getVendor()).thenReturn(cct.getVendor());

		//Act
		bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert

		verify(bookingUI).displayConfirmedBooking(
				roomDescriptionCaptor.capture(),
				roomNumberCaptor.capture(),
				arrivalDateCaptor.capture(),
				stayLengthCaptor.capture(),
				guestNameCaptor.capture(),
				ccVendorCaptor.capture(),
				creditCardNumberCaptor.capture(),
				costNumberCaptor.capture(),
				confirmationNumberCaptor.capture());

		//We know in Java that numbers are ALWAYS initalized to 0
		//So we can take advantage of that
		//Verify all the captors
		assertEquals("D", roomDescriptionCaptor.getValue());
		assertTrue(1 == roomNumberCaptor.getValue());
		assertEquals(arrivalDate, arrivalDateCaptor.getValue());
		assertTrue(0 == stayLengthCaptor.getValue());
		assertEquals("N", guestNameCaptor.getValue());
		assertEquals("Visa", ccVendorCaptor.getValue());
		assertTrue(cardNumber == creditCardNumberCaptor.getValue());
		assertTrue(0 == costNumberCaptor.getValue());
		assertTrue(0 == confirmationNumberCaptor.getValue());

		verify(hotel).book(room, guest, arrivalDate, 0, 0, creditCard);
		verify(bookingUI).setState(BookingUI.State.COMPLETED);

		when(bookingCTL.getState()).thenCallRealMethod();
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());

	}

	@Test
	void testCreditDetailsInvalidStartingState()
	{
		//Arrange
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.COMPLETED);

		//Act
		Executable e = () -> bookingCTL.creditDetailsEntered(cct, cardNumber, ccv);

		//Assert
		assertThrows(RuntimeException.class, e);
	}

}
