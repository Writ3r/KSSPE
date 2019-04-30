// specify the package
package model;

// system imports
import utilities.GlobalVariables;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;
import java.text.SimpleDateFormat;
import java.util.Date;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the CheckOutCollection for the KSSPE
 *  application 
 */
//==============================================================
public class CheckOutCollection extends EntityBase
{
	private static final String myTableName = "CheckOut";

	private Vector<CheckOut> checkoutList;

	// constructor for this class
	//----------------------------------------------------------
	public CheckOutCollection() 
	{
		super(myTableName);
	}

	//-----------------------------------------------------------
	private void populateCollectionHelper(String query)
	{

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			checkoutList = new Vector<CheckOut>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextCheckOutData = allDataRetrieved.elementAt(cnt);
				
				try
				{
					CheckOut c = new CheckOut(nextCheckOutData);
					
					addCheckOut(c);
				}
				catch(Exception e)
				{
					new Event(Event.getLeafLevelClassName(this), "populateCollectionHelper",
					"Error in creating a checkout from checkout collection", Event.ERROR);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findReserved()
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (TotalUnitsReturned < UnitsTaken)";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findOverDue()
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((TotalUnitsReturned < UnitsTaken) AND (DueDate < '" + 
			new SimpleDateFormat("yyyy-MM-dd").format(new Date()) +"'))";
		populateCollectionHelper(query);
	}

	//----------------------------------------------------------------------------------
	private void addCheckOut(CheckOut c)
	{
		int index = findIndexToAdd(c);
		checkoutList.insertElementAt(c, index); // Sorts by barcode
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(CheckOut c)
	{
		
		int low=0;
		int high = checkoutList.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			CheckOut midSession = checkoutList.elementAt(middle);

			int result = CheckOut.compare(c, midSession);

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
		if (key.equals("CheckOuts"))
			return checkoutList;
		else
			if (key.equals("CheckOutList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
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
