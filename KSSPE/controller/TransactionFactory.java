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
		if (transType.equals("RemoveWorker") == true)
		{
			retValue = new RemoveWorkerTransaction();
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
		if (transType.equals("ModifyEquipment") == true)
		{
			retValue = new UpdateEquipmentTransaction();
		} 
		if (transType.equals("RemoveEquipment") == true)
		{
			retValue = new RemoveEquipmentTransaction();
		} 
		if (transType.equals("ReserveEquipment") == true)
		{
			retValue = new ReserveEquipmentTransaction();
		}
		if (transType.equals("ReturnEquipment") == true)
		{
			retValue = new ReturnEquipmentTransaction();
		}
		if (transType.equals("ListAllInventory") == true)
		{
			retValue = new ListAllInventoryTransaction();
		}
		if (transType.equals("ListAvailableInventory") == true)
		{
			retValue = new ListAvailableInventoryTransaction();
		}
		if (transType.equals("ListReservedInventory") == true)
		{
			retValue = new ListReservedInventoryTransaction();
		}
		if (transType.equals("ListOverdueInventory") == true)
		{
			retValue = new ListOverdueInventoryTransaction();
		}
		return retValue;
	}
}
