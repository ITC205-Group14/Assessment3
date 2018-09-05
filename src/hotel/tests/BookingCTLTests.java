package hotel.tests;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hotel.booking.BookingCTL;
import hotel.credit.CreditCardType;
import hotel.entities.Hotel;
import hotel.entities.RoomType;

class BookingCTLTests
{
	Hotel hotel;
	BookingCTL bookingCTL;

	@BeforeEach
	void setUp()
	{
		hotel = new Hotel();
		bookingCTL = new BookingCTL(hotel);
		hotel.addRoom(RoomType.SINGLE, 0);
		bookingCTL.phoneNumberEntered(1);
		bookingCTL.guestDetailsEntered("Corey", "123 Fake Street");
		bookingCTL.roomTypeAndOccupantsEntered(RoomType.SINGLE, 1);
		bookingCTL.bookingTimesEntered(new Date(), 3);
	}

	@Test
	void testCreditDetailsEnteredInvalidCard()
	{
		CreditCardType cct = CreditCardType.VISA;
		bookingCTL.creditDetailsEntered(cct, 6, 1);
	}

	@Test
	void testCreditDetailsEnteredInvalidState()
	{

	}


}
