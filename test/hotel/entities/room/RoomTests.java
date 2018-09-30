package hotel.entities.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;
import hotel.entities.Booking;
import hotel.entities.Room;
import hotel.entities.RoomType;
import hotel.entities.Guest;


@ExtendWith(MockitoExtension.class)

class RoomTest {


@Mock Booking booking;
@Spy ArrayList<Booking> bookings;
@Mock Guest guest;
@Mock Date arrivalDate;
@Mock CreditCard creditCard;

int roomId = 0;
RoomType roomType = RoomType.DOUBLE;
int stayLength = 5;
int numberOfOccupants = 2;

@InjectMocks Room room = new Room(roomId, roomType);

@BeforeEach

void setUp() throws Exception {	
room = new Room(roomId, roomType);
	}

@Test
void testCheckinReady()

{

//Arrange
//Act
room.checkin();
//Assert
assertTrue(room.isOccupied());

	}


@Test
void testCheckinOccupied()
{

//Arrange
room.checkin();
//Act
Executable e = () -> room.checkin();
Throwable t = assertThrows(RuntimeException.class, e);
//Assert
assertEquals("Room is not ready, unable to check-in", t.getMessage());

	}


@Test
void testCheckoutOccupied()
{

//Arrange
bookings.add(booking);
room.checkin();
//Act
room.checkout(booking);
//Assert
assertTrue(room.isReady());

	}

@Test
void testNewBooking()
{
//Arrange
//Act
Booking newBook = room.book(guest, arrivalDate, stayLength, numberOfOccupants, creditCard);
//Assert
assertNotNull(newBook);
assertTrue(room.isReady());

	}

}
