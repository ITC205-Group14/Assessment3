package hotel.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.Hotel;
import hotel.entities.RoomType;

@ExtendWith(MockitoExtension.class)
class BookRoomScenarioTests
{
	@Spy Hotel hotel = new Hotel();
	@Mock BookingUI bookingUI;
	BookingCTL bookingCTL;

	String existingName = "Foo";
	String existingAddress = "Bar";
	int existingPhone = 555;
	RoomType rt = RoomType.SINGLE;
	Date arrival = new Date();
	int stayLength = 3;

	CreditCardType ccType = CreditCardType.VISA;
	int ccNumber = 3;
	int ccCcv = 1;

	@BeforeEach
	void setup() throws Exception
	{
		/**
		 * This will load a hotel with 3 rooms
		 * Single 101, Twin 201, Twin share, 301
		 * And an EXISTING GUEST (Fred, Nurke, 2)
		 */
		bookingCTL = new BookingCTL(hotel);
		bookingCTL.bookingUI = bookingUI;
		//Add a guest to test with
		hotel.registerGuest(existingName, existingAddress, existingPhone);
		//Add room to test with
		hotel.addRoom(rt, 101);
	}

	@Test
	void ExistingGuestBooksIntoRoom()
	{
		//Arrange ---
		//Check if guest really does exist
		assertNotNull(hotel.findGuestByPhoneNumber(existingPhone));

		//Act ---
		//Check the state as we go, because why not?
		bookingCTL.phoneNumberEntered(existingPhone);
		assertEquals(BookingCTL.State.ROOM, bookingCTL.getState());
		bookingCTL.roomTypeAndOccupantsEntered(rt, 1);
		assertEquals(BookingCTL.State.TIMES, bookingCTL.getState());
		bookingCTL.bookingTimesEntered(arrival, stayLength);
		assertEquals(BookingCTL.State.CREDIT, bookingCTL.getState());
		bookingCTL.creditDetailsEntered(ccType, ccNumber, ccCcv);
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());

		//Assert ---
		verify(bookingUI, times(4)).setState(any());
		verify(hotel).isRegistered(existingPhone);
		verify(hotel, times(2)).findGuestByPhoneNumber(existingPhone);
		verify(bookingUI).displayGuestDetails(existingName, existingAddress, existingPhone);
		assertEquals(rt, bookingCTL.selectedRoomType);
		assertEquals(1, bookingCTL.occupantNumber);
		assertEquals(arrival, bookingCTL.arrivalDate);
		assertEquals(stayLength, bookingCTL.stayLength);
		verify(bookingUI).displayBookingDetails(rt.getDescription(), arrival, stayLength, rt.calculateCost(arrival, stayLength));
		verify(hotel).book(eq(bookingCTL.room), eq(bookingCTL.guest), eq(arrival), eq(stayLength), eq(1), any(CreditCard.class));
		verify(bookingUI).displayConfirmedBooking(eq(rt.getDescription()), eq(101), eq(arrival), eq(stayLength), eq(existingName), eq(ccType.getVendor()), eq(ccNumber), anyDouble(), anyLong());
	}

}
