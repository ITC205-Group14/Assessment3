package hotel.booking;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCardType;
import hotel.entities.Hotel;
import hotel.entities.RoomType;

@ExtendWith(MockitoExtension.class)
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
