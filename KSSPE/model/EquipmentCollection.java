// specify the package
package model;

// system imports
import utilities.GlobalVariables;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the Equipment Collection for the KSSPE
 *  application 
 */
//==============================================================
public class EquipmentCollection extends EntityBase
{
	private static final String myTableName = "Equipment";

	private Vector<Equipment> equipmentList;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public EquipmentCollection( ) 
	{
		super(myTableName);
	}

	//-----------------------------------------------------------
	private void populateCollectionHelper(String query)
	{

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			equipmentList = new Vector<Equipment>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextEData = allDataRetrieved.elementAt(cnt);
				
				try
				{
					Equipment e = new Equipment(nextEData);
					
					addEquipment(e);
				}
				catch(Exception e)
				{
					new Event(Event.getLeafLevelClassName(this), "populateCollectionHelper",
					"Error in creating equipment from equipment collection", Event.ERROR);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findByBarcodePrefix(String barcodePrefix)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((AvailableCount > 0) AND (Barcode LIKE '" + barcodePrefix + "%'))";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findAll()
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (AvailableCount > 0)";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findByName(String name)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((AvailableCount > 0) AND (Name LIKE '%" + name + "%'))";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findByCategoryName(String name)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((AvailableCount > 0) AND (CategoryName LIKE '%" + name + "%'))";
		populateCollectionHelper(query);
	}

	//----------------------------------------------------------------------------------
	private void addEquipment(Equipment e)
	{
		int index = findIndexToAdd(e);
		equipmentList.insertElementAt(e,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Equipment e)
	{
		
		int low=0;
		int high = equipmentList.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Equipment midSession = equipmentList.elementAt(middle);

			int result = Equipment.compare(e,midSession);

			if (result == 0)
			{
				return middle;
			}
			else if (result<0)
			{
				high=middle-1;
			}
			else
			{
				low=middle+1;
			}
		}
		return low;
	}


	/**
	 *
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("AllEquipment"))
			return equipmentList;
		else
			if (key.equals("EquipmentList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
	}

	//----------------------------------------------------------
	public Equipment retrieve(String barcode)
	{
		Equipment retValue = null;
		for (int cnt = 0; cnt < equipmentList.size(); cnt++)
		{
			Equipment nextE = equipmentList.elementAt(cnt);
			String nextBarcode = (String)nextE.getState("Barcode");
			if (nextBarcode.equals(barcode) == true)
			{
				retValue = nextE;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}

	//----------------------------------------------------------
	public void remove(String barcode)
	{
		for (int cnt = 0; cnt < equipmentList.size(); cnt++)
		{
			Equipment nextE = equipmentList.elementAt(cnt);
			String nextBarcode = (String)nextE.getState("Barcode");
			if (nextBarcode.equals(barcode) == true)
			{
				equipmentList.remove(cnt);
			}
		}
	}
	
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}
