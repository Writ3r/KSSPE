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
		if (transType.equals("AddBorrower") == true)
		{
			retValue = new AddBorrowerTransaction();
		} 
		if (transType.equals("AddCategory") == true)
		{
			retValue = new AddCategoryTransaction();
		}
		if (transType.equals("ModifyWorker") == true)
		{
			retValue = new UpdateWorkerTransaction();
		} 
		if (transType.equals("ModifyBorrower") == true)
		{
			retValue = new UpdateBorrowerTransaction();
		} 
		if (transType.equals("ModifyCategory") == true)
		{
			retValue = new UpdateCategoryTransaction();
		} 
		if (transType.equals("RemoveCategory") == true)
		{
			retValue = new RemoveCategoryTransaction();
		} 
		if (transType.equals("RemoveBorrower") == true)
		{
			retValue = new RemoveBorrowerTransaction();
		}
		if (transType.equals("AddEquipment") == true)
		{
			retValue = new AddEquipmentTransaction();
		} 
		return retValue;
	}
}
