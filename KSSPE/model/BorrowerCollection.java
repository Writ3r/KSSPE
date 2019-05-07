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


/** The class containing the BorrowerCollection for the KSSPE
 *  application 
 */
//==============================================================
public class BorrowerCollection extends EntityBase
{
	private static final String myTableName = "Borrower";

	private Vector<Borrower> borrowers;

	// constructor for this class
	//----------------------------------------------------------
	public BorrowerCollection( ) 
	{
		super(myTableName);
	}

	//-----------------------------------------------------------
	private void populateCollectionHelper(String query)
	{

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			borrowers = new Vector<Borrower>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBorrowerData = allDataRetrieved.elementAt(cnt);
				
				try
				{
					Borrower b = new Borrower(nextBorrowerData);
					
					addBorrower(b);
				}
				catch(Exception e)
				{
					new Event(Event.getLeafLevelClassName(this), "populateCollectionHelper",
					"Error in creating a Borrower from Borrower collection", Event.ERROR);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findByBannerId(String bannerId)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Borrower.Status = 'Active') AND (Borrower.BannerId = '" + bannerId + "') AND (Borrower.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}
	
	//----------------------------------------------------------
	public void findByFirstName(String firstname)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Borrower.Status = 'Active') AND (FirstName LIKE '%" + firstname + "%') AND (Borrower.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}
	
	//-------------------------------------------------------------
	public void findByLastName(String lastname)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Borrower.Status = 'Active') AND (LastName LIKE '%" + lastname + "%') AND (Borrower.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}
	
	//-------------------------------------------------------------
	public void findByFirstAndLast(Properties props)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Borrower.Status = 'Active') AND (LastName LIKE '%" + props.getProperty("LastName") + "%') AND (FirstName LIKE '%" + props.getProperty("FirstName") + "%') AND (Borrower.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}

	//-------------------------------------------------------------
	public void findByPhone(String phone)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Borrower.Status = 'Active') AND (PhoneNumber LIKE '%" + phone + "%') AND (Borrower.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}
	
	//-----------------------------------------------------------
	public void findAll()
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Borrower.BannerId = Person.BannerId) AND (Borrower.Status = 'Active'))";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	// public void findByName(String firstName, String lastName)
	// {
	// 	String query = "SELECT * FROM " + myTableName + " WHERE ((Status = 'Active') AND (FirstName LIKE '%" + firstName + "%')" 
	// 	+ "AND (LastName LIKE '%" + lastName + "%'))";
	// 	populateCollectionHelper(query);
	// }


	//----------------------------------------------------------------------------------
	private void addBorrower(Borrower w)
	{
		int index = findIndexToAdd(w);
		borrowers.insertElementAt(w,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Borrower w)
	{
		
		int low=0;
		int high = borrowers.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Borrower midSession = borrowers.elementAt(middle);

			int result = Borrower.compare(w, midSession);

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
		if (key.equals("Borrowers"))
			return borrowers;
		else
			if (key.equals("BorrowerList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
	}

	//----------------------------------------------------------
	public Borrower retrieve(String bannerId)
	{
		Borrower retValue = null;
		for (int cnt = 0; cnt < borrowers.size(); cnt++)
		{
			Borrower nextC = borrowers.elementAt(cnt);
			String nextBanner = (String)nextC.getState("BannerId");
			if (nextBanner.equals(bannerId) == true)
			{
				retValue = nextC;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}
	
	//----------------------------------------------------------
	public void remove(String bannerId)
	{
		for (int cnt = 0; cnt < borrowers.size(); cnt++)
		{
			Borrower nextC = borrowers.elementAt(cnt);
			String nextBannerId = (String)nextC.getState("BannerId");
			if (nextBannerId.equals(bannerId) == true)
			{
				borrowers.remove(cnt);
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
