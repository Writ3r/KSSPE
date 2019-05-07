// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;
import event.Event;

// project imports
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import database.*;

/** The class containing the CheckIn record for the KSSPE application */
//==============================================================
public class CheckIn extends EntityBase
{
	private static final String myTableName = "CheckIn";
	
	// GUI Components
	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public CheckIn(String idToQuery) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + idToQuery + " )";

		Vector allDataRetrieved =  getSelectQueryResult(query);

		// You must get one checkIn at least
		if (allDataRetrieved != null && allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one CheckIn. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple CheckIns matching id : "
					+ idToQuery + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedCheckInData = (Properties)allDataRetrieved.elementAt(0);
				persistentState = new Properties();
				
				Enumeration allKeys = retrievedCheckInData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedCheckInData.getProperty(nextKey);
					
					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}
				
			}
		}
		// If no CheckIn found for this ID, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("ERROR: No CheckIn found for ID: " + idToQuery);
		}
	}

	//----------------------------------------------------------
	public CheckIn(Properties props) 
	{
		super(myTableName);
		
		persistentState = new Properties();
			
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			// DEBUG System.out.println(nextKey + "   ----------   " + nextValue);
			
			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}
		
		
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;
		
		return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		persistentState.setProperty(key, (String)value);

	}
	
	//-----------------------------------------------------------------------------------
	public static int compare(CheckIn a, CheckIn b)
	{
		String aVal = (String)a.getState("ID");
		String bVal = (String)b.getState("ID");

		return aVal.compareTo(bVal);
	}
	//------------------------------------------------------------------
	public void save()
	{
		updateStateInDatabase();
	}
	
	//------------------------------------------------------------------
	private void updateStateInDatabase()
	{
		try
		{
			if (getState("ID") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("ID", persistentState.getProperty("ID"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "CheckIn with Id: " + persistentState.getProperty("ID") + " updated successfully!";
			}
			else
			{
				Integer atID = insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("ID", "" + atID.intValue());
				updateStatusMessage = "CheckIn record inserted successfully!";

				
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "ERROR: CheckIn could not be installed in database!";
			new Event(Event.getLeafLevelClassName(this), "updateStateInDatabase", 
			"CheckIn could not be saved in database: " + ex.toString(), Event.ERROR);
		}
			
	}

	//-------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("ID"));
		v.addElement(persistentState.getProperty("BannerID"));
		v.addElement(persistentState.getProperty("Barcode"));
		v.addElement(persistentState.getProperty("ReturnDate"));
		v.addElement(persistentState.getProperty("UnitsReturned"));
		v.addElement(persistentState.getProperty("CheckoutID"));
		v.addElement(persistentState.getProperty("CheckInWorkerID"));
	

		return v;
	}
	
	
	
	//------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}


