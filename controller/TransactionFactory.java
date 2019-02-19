// specify the package
package controller;

// system imports
import java.util.Vector;


// project imports

/** The class containing the TransactionFactory for the KSSPE Project 
 *  Closet application 
 */
//==============================================================
public class TransactionFactory
{

	/**
	 *
	 */
	//----------------------------------------------------------
	public static Transaction createTransaction(String transType) throws Exception
	{
		Transaction retValue = null;
		if (transType.equals("AddWorker") == true)
		{
			retValue = new AddWorkerTransaction();
		} 
		else if (transType.equals("UpdateWorker") == true)
		{
			retValue = new UpdateWorkerTransaction();
		} 
		else if (transType.equals("SearchBanner") == true)
		{
			retValue = new SearchByBannerTransaction();
		}
		return retValue;
	}
}
