package hotel.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardHelper;
import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;

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

	CreditCard creditCard;
	CreditAuthorizer creditAuthorizer;

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

		//Make a real credit card
		CreditCard realCC = new CreditCard(cct, 6, 1);
		//Force the helper to return this real CC
		when(creditCardHelper.loadCreditCard(any(CreditCardType.class), anyInt(), anyInt())).thenReturn(realCC);

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
	void testCreditDetailsEnteredValidCardRealCard()
	{
		//Arrange
		int cardNumber = 4;
		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Make a real credit card
		CreditCard realCC = new CreditCard(cct, cardNumber, 1);
		//Force the helper to return this real CC
		when(creditCardHelper.loadCreditCard(any(CreditCardType.class), anyInt(), anyInt())).thenReturn(realCC);

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
		assertEquals(realCC.getVendor(), ccVendorCaptor.getValue());
		assertTrue(realCC.getNumber() == creditCardNumberCaptor.getValue());
		assertTrue(0 == costNumberCaptor.getValue());
		assertTrue(0 == confirmationNumberCaptor.getValue());

		verify(hotel).book(room, guest, arrivalDate, 0, 0, realCC);
		verify(bookingUI).setState(BookingUI.State.COMPLETED);

		when(bookingCTL.getState()).thenCallRealMethod();
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());

	}

	@Test
	void testCreditDetailsEnteredValidCardRealCardRealRoom()
	{
		//Arrange
		int cardNumber = 4;
		room = new Room(0, RoomType.SINGLE);
		bookingCTL.room = room;

		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Make a real credit card
		CreditCard realCC = new CreditCard(cct, cardNumber, 1);
		//Force the helper to return this real CC
		when(creditCardHelper.loadCreditCard(any(CreditCardType.class), anyInt(), anyInt())).thenReturn(realCC);

		ArgumentCaptor<String> roomDescriptionCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Date> arrivalDateCaptor = ArgumentCaptor.forClass(Date.class);
		ArgumentCaptor<Integer> stayLengthCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> guestNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> ccVendorCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> creditCardNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Double> costNumberCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Long> confirmationNumberCaptor = ArgumentCaptor.forClass(Long.class);

		when(guest.getName()).thenReturn("N");

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
		assertEquals("Single room", roomDescriptionCaptor.getValue());
		assertTrue(0 == roomNumberCaptor.getValue());
		assertEquals(arrivalDate, arrivalDateCaptor.getValue());
		assertTrue(0 == stayLengthCaptor.getValue());
		assertEquals("N", guestNameCaptor.getValue());
		assertEquals(realCC.getVendor(), ccVendorCaptor.getValue());
		assertTrue(realCC.getNumber() == creditCardNumberCaptor.getValue());
		assertTrue(0 == costNumberCaptor.getValue());
		assertTrue(0 == confirmationNumberCaptor.getValue());

		verify(hotel).book(room, guest, arrivalDate, 0, 0, realCC);
		verify(bookingUI).setState(BookingUI.State.COMPLETED);

		when(bookingCTL.getState()).thenCallRealMethod();
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());
	}

	@Test
	void testCreditDetailsEnteredValidCardRealCardRealRoomRealGuest()
	{
		//Arrange
		int cardNumber = 4;
		room = new Room(0, RoomType.SINGLE);
		bookingCTL.room = room;

		guest = new Guest("Fred", "123 Fake", 555);
		bookingCTL.guest = guest;

		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Make a real credit card
		CreditCard realCC = new CreditCard(cct, cardNumber, 1);
		//Force the helper to return this real CC
		when(creditCardHelper.loadCreditCard(any(CreditCardType.class), anyInt(), anyInt())).thenReturn(realCC);

		ArgumentCaptor<String> roomDescriptionCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Date> arrivalDateCaptor = ArgumentCaptor.forClass(Date.class);
		ArgumentCaptor<Integer> stayLengthCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> guestNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> ccVendorCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> creditCardNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Double> costNumberCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Long> confirmationNumberCaptor = ArgumentCaptor.forClass(Long.class);

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
		assertEquals("Single room", roomDescriptionCaptor.getValue());
		assertTrue(0 == roomNumberCaptor.getValue());
		assertEquals(arrivalDate, arrivalDateCaptor.getValue());
		assertTrue(0 == stayLengthCaptor.getValue());
		assertEquals("Fred", guestNameCaptor.getValue());
		assertEquals(realCC.getVendor(), ccVendorCaptor.getValue());
		assertTrue(realCC.getNumber() == creditCardNumberCaptor.getValue());
		assertTrue(0 == costNumberCaptor.getValue());
		assertTrue(0 == confirmationNumberCaptor.getValue());

		verify(hotel).book(room, guest, arrivalDate, 0, 0, realCC);
		verify(bookingUI).setState(BookingUI.State.COMPLETED);

		when(bookingCTL.getState()).thenCallRealMethod();
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());
	}

	@Test
	void testCreditDetailsEnteredValidCardRealCardRealRoomRealGuestRealHotel()
	{
		//Arrange
		hotel = new Hotel();
		bookingCTL.hotel = hotel;

		arrivalDate = new GregorianCalendar(2018, 0, 1).getTime();
		bookingCTL.arrivalDate = arrivalDate;

		int cardNumber = 4;
		room = new Room(0, RoomType.SINGLE);
		bookingCTL.room = room;

		guest = new Guest("Fred", "123 Fake", 555);
		bookingCTL.guest = guest;

		when(bookingCTL.getState()).thenReturn(BookingCTL.State.CREDIT);

		//Make a real credit card
		CreditCard realCC = new CreditCard(cct, cardNumber, 1);
		//Force the helper to return this real CC
		when(creditCardHelper.loadCreditCard(any(CreditCardType.class), anyInt(), anyInt())).thenReturn(realCC);

		ArgumentCaptor<String> roomDescriptionCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Date> arrivalDateCaptor = ArgumentCaptor.forClass(Date.class);
		ArgumentCaptor<Integer> stayLengthCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> guestNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> ccVendorCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> creditCardNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Double> costNumberCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Long> confirmationNumberCaptor = ArgumentCaptor.forClass(Long.class);

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
		assertEquals("Single room", roomDescriptionCaptor.getValue());
		assertTrue(0 == roomNumberCaptor.getValue());
		assertEquals(arrivalDate, arrivalDateCaptor.getValue());
		assertTrue(0 == stayLengthCaptor.getValue());
		assertEquals("Fred", guestNameCaptor.getValue());
		assertEquals(realCC.getVendor(), ccVendorCaptor.getValue());
		assertTrue(realCC.getNumber() == creditCardNumberCaptor.getValue());
		assertTrue(0 == costNumberCaptor.getValue());
		assertTrue(1020180 == confirmationNumberCaptor.getValue());

		verify(bookingUI).setState(BookingUI.State.COMPLETED);

		//Verify we changed states
		when(bookingCTL.getState()).thenCallRealMethod();
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());
	}

}
