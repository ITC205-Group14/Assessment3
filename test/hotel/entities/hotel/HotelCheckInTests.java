package hotel.entities.hotel;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.entities.Hotel;
@ExtendWith(MockitoExtension.class)
class HotelCheckInTests
{
	@Spy Hotel hotel = new Hotel();

	@Test
	void hotelCheckinWithInvalidConfirmationNumber()
	{
		//Arrange
		int confirmationNumber = 99999;

		//Act
		Executable test = () -> hotel.checkin(confirmationNumber);

		//Assert
		assertThrows(RuntimeException.class, test);
	}

	@Test
	void hotelCheckinWithValidConfirmationNumber()
	{
		//Arrange
		int confirmationNumber = 0;

		//Act
		Executable test = () -> hotel.checkin(confirmationNumber);

		//Assert
		assertThrows(RuntimeException.class, test);
	}

}
