// specify the package
package model;

// system imports
import utilities.GlobalVariables;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.text.SimpleDateFormat;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the CheckOut Collection for the KSSPE
 *  application 
 */
//==============================================================
public class CheckOutCollection extends EntityBase
{
	private static final String myTableName = "CheckOut";

	private Vector<CheckOut> checkoutList;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public CheckOutCollection( ) 
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
				Properties nextCOData = allDataRetrieved.elementAt(cnt);
				
				try
				{
					CheckOut e = new CheckOut(nextCOData);
					
					addCheckOut(e);
				}
				catch(Exception e)
				{
					new Event(Event.getLeafLevelClassName(this), "populateCollectionHelper",
					"Error in creating CheckOut for CheckOut collection", Event.ERROR);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findPendingByBannerId(String bannerId)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((BannerId = '" + bannerId + "') AND (UnitsTaken > TotalUnitsReturned))";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findAllPending()
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (UnitsTaken > TotalUnitsReturned)";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findOverdueByBannerId(String bannerId)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((BannerId = '" + bannerId + "' AND (TotalUnitsReturned < UnitsTaken) AND (DueDate < '" + 
			new SimpleDateFormat("yyyy-MM-dd").format(new Date()) +"'))";
		populateCollectionHelper(query);
	}
	
	//-----------------------------------------------------------
	public void findAllOverdue( )
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((TotalUnitsReturned < UnitsTaken) AND (DueDate < '" + 
			new SimpleDateFormat("yyyy-MM-dd").format(new Date()) +"'))"; // Liam query
		populateCollectionHelper(query);
	}

	

	//----------------------------------------------------------------------------------
	private void addCheckOut(CheckOut e)
	{
		int index = findIndexToAdd(e);
		checkoutList.insertElementAt(e,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(CheckOut e)
	{
		
		int low=0;
		int high = checkoutList.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			CheckOut midSession = checkoutList.elementAt(middle);

			int result = CheckOut.compare(e,midSession);

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
	public CheckOut retrieve(String checkOutID)
	{
		CheckOut retValue = null;
		for (int cnt = 0; cnt < checkoutList.size(); cnt++)
		{
			CheckOut nextCO = checkoutList.elementAt(cnt);
			String nextID = (String)nextCO.getState("ID");
			if (nextID.equals(checkOutID) == true)
			{
				retValue = nextCO;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}

	//----------------------------------------------------------
	public void remove(String ID)
	{
		for (int cnt = 0; cnt < checkoutList.size(); cnt++)
		{
			CheckOut nextCO = checkoutList.elementAt(cnt);
			String nextID = (String)nextCO.getState("ID");
			if (nextID.equals(ID) == true)
			{
				checkoutList.remove(cnt);
			}
		}
	}
	
	//--------------------------------------------------------------
	public int getSize()
	{
		int numElems = 0;
		if (checkoutList != null)
			numElems = checkoutList.size();
		
		return numElems;
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
