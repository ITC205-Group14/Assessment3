package hotel.credit;

/**
 * Return a new CreditCard
 *
 * ~ Unit testing, priceless, for everything else, there's CreditCardHelper ~
 *
 * @author Corey
 *
 */
public class CreditCardHelper
{
	public CreditCard loadCreditCard(CreditCardType type, int number, int ccv)
	{
		return new CreditCard(type, number, ccv);
	}
}
