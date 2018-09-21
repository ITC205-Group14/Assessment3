package hotel.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import hotel.entities.Guest;
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
	RoomType rtSingle = RoomType.SINGLE;
	RoomType rtDouble = RoomType.DOUBLE;
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
		hotel.addRoom(rtSingle, 101);
		hotel.addRoom(rtDouble, 201);
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
		bookingCTL.roomTypeAndOccupantsEntered(rtSingle, 1);
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
		assertEquals(rtSingle, bookingCTL.selectedRoomType);
		assertEquals(1, bookingCTL.occupantNumber);
		assertEquals(arrival, bookingCTL.arrivalDate);
		assertEquals(stayLength, bookingCTL.stayLength);
		verify(bookingUI).displayBookingDetails(rtSingle.getDescription(), arrival, stayLength, rtSingle.calculateCost(arrival, stayLength));
		verify(hotel).book(eq(bookingCTL.room), eq(bookingCTL.guest), eq(arrival), eq(stayLength), eq(1), any(CreditCard.class));
		verify(bookingUI).displayConfirmedBooking(eq(rtSingle.getDescription()), eq(101), eq(arrival), eq(stayLength), eq(existingName), eq(ccType.getVendor()), eq(ccNumber), anyDouble(), anyLong());
	}

	@Test
	void NewGuestBooksIntoRoom()
	{
		//Arrange ---
		//Check if guest really does NOT exist
		assertNull(hotel.findGuestByPhoneNumber(5));

		//Act ---
		hotel.registerGuest("Newmann", "Also fake", 5);
		//Check he registered correctly
		assertNotNull(hotel.findGuestByPhoneNumber(5));
		//Check the state as we go, because why not?
		bookingCTL.phoneNumberEntered(5);
		assertEquals(BookingCTL.State.ROOM, bookingCTL.getState());
		bookingCTL.roomTypeAndOccupantsEntered(rtSingle, 1);
		assertEquals(BookingCTL.State.TIMES, bookingCTL.getState());
		bookingCTL.bookingTimesEntered(arrival, stayLength);
		assertEquals(BookingCTL.State.CREDIT, bookingCTL.getState());
		bookingCTL.creditDetailsEntered(ccType, ccNumber, ccCcv);
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());

		//Assert ---
		verify(bookingUI, times(4)).setState(any());
		verify(hotel).isRegistered(5);
		verify(hotel, times(3)).findGuestByPhoneNumber(5);
		verify(bookingUI).displayGuestDetails("Newmann", "Also fake", 5);
		assertEquals(rtSingle, bookingCTL.selectedRoomType);
		assertEquals(1, bookingCTL.occupantNumber);
		assertEquals(arrival, bookingCTL.arrivalDate);
		assertEquals(stayLength, bookingCTL.stayLength);
		verify(bookingUI).displayBookingDetails(rtSingle.getDescription(), arrival, stayLength, rtSingle.calculateCost(arrival, stayLength));
		verify(hotel).book(eq(bookingCTL.room), eq(bookingCTL.guest), eq(arrival), eq(stayLength), eq(1), any(CreditCard.class));
		verify(bookingUI).displayConfirmedBooking(eq(rtSingle.getDescription()), eq(101), eq(arrival), eq(stayLength), eq("Newmann"), eq(ccType.getVendor()), eq(ccNumber), anyDouble(), anyLong());
	}

	@Test
	void ExistingGuestBooksIntoUnavaliableRoom()
	{
		//Arrange ---
		//Check if guest really does exist
		assertNotNull(hotel.findGuestByPhoneNumber(existingPhone));
		hotel.roomsByType.get(rtSingle).get(101).book(new Guest("BookedBeforeYou", "S", 34), arrival, stayLength, 1, new CreditCard(ccType, ccNumber, ccCcv));

		//Act ---
		//Check the state as we go, because why not?
		bookingCTL.phoneNumberEntered(existingPhone);
		assertEquals(BookingCTL.State.ROOM, bookingCTL.getState());
		bookingCTL.roomTypeAndOccupantsEntered(rtSingle, 1);
		assertEquals(BookingCTL.State.TIMES, bookingCTL.getState());
		bookingCTL.bookingTimesEntered(arrival, stayLength);
		assertEquals(BookingCTL.State.ROOM, bookingCTL.getState());
		//We go back to ROOM because there is already a guest
		//This will allow us to select a different room type, date etc
		bookingCTL.roomTypeAndOccupantsEntered(rtDouble, 1);
		assertEquals(BookingCTL.State.TIMES, bookingCTL.getState());
		bookingCTL.bookingTimesEntered(arrival, stayLength);
		assertEquals(BookingCTL.State.CREDIT, bookingCTL.getState());
		bookingCTL.creditDetailsEntered(ccType, ccNumber, ccCcv);
		assertEquals(BookingCTL.State.COMPLETED, bookingCTL.getState());

		//Assert ---
		verify(bookingUI, times(6)).setState(any());
		verify(hotel).isRegistered(existingPhone);
		verify(hotel, times(2)).findGuestByPhoneNumber(existingPhone);
		verify(bookingUI).displayGuestDetails(existingName, existingAddress, existingPhone);
		assertEquals(rtDouble, bookingCTL.selectedRoomType);
		assertEquals(1, bookingCTL.occupantNumber);
		assertEquals(arrival, bookingCTL.arrivalDate);
		assertEquals(stayLength, bookingCTL.stayLength);
		verify(bookingUI).displayBookingDetails(rtDouble.getDescription(), arrival, stayLength, rtDouble.calculateCost(arrival, stayLength));
		verify(hotel).book(eq(bookingCTL.room), eq(bookingCTL.guest), eq(arrival), eq(stayLength), eq(1), any(CreditCard.class));
		verify(bookingUI).displayConfirmedBooking(eq(rtDouble.getDescription()), eq(201), eq(arrival), eq(stayLength), eq(existingName), eq(ccType.getVendor()), eq(ccNumber), anyDouble(), anyLong());
	}

	@Test
	void ExistingGuestBooksIntoUnavaliableRoomThenCancels()
	{
		//Arrange ---
		//Check if guest really does exist
		assertNotNull(hotel.findGuestByPhoneNumber(existingPhone));
		hotel.roomsByType.get(rtSingle).get(101).book(new Guest("BookedBeforeYou", "S", 34), arrival, stayLength, 1, new CreditCard(ccType, ccNumber, ccCcv));

		//Act ---
		//Check the state as we go, because why not?
		bookingCTL.phoneNumberEntered(existingPhone);
		assertEquals(BookingCTL.State.ROOM, bookingCTL.getState());
		bookingCTL.roomTypeAndOccupantsEntered(rtSingle, 1);
		assertEquals(BookingCTL.State.TIMES, bookingCTL.getState());
		bookingCTL.bookingTimesEntered(arrival, stayLength);
		assertEquals(BookingCTL.State.ROOM, bookingCTL.getState());

		bookingCTL.cancel();
		assertEquals(BookingCTL.State.CANCELLED, bookingCTL.getState());

		//Assert ---
		verify(bookingUI, times(4)).setState(any());
		verify(hotel).isRegistered(existingPhone);
		verify(hotel, times(2)).findGuestByPhoneNumber(existingPhone);
		verify(bookingUI).displayGuestDetails(existingName, existingAddress, existingPhone);
		verify(bookingUI).displayMessage("Booking cancelled");
	}

}
