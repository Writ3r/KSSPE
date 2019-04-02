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

/** The class containing the Person for the KSSPE application */
//==============================================================
public class Equipment extends EntityBase
{
	private static final String myTableName = "Equipment";
	private boolean oldFlag = true;
	
	// GUI Components
	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Equipment(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		String idToQuery = props.getProperty("Barcode");

		String query = "SELECT * FROM " + myTableName + " WHERE (Barcode = " + idToQuery + " )";

		Vector allDataRetrieved =  getSelectQueryResult(query);

		// You must get one equipment at least
		if (allDataRetrieved != null && allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one equipment. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple Equipment matching barcode : "
					+ idToQuery + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedEquipmentData = (Properties)allDataRetrieved.elementAt(0);
				persistentState = new Properties();
				
				Enumeration allKeys = retrievedEquipmentData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedEquipmentData.getProperty(nextKey);
					
					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}
				
				oldFlag = true;
			}
		}
		// If no Person found for this user name, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("ERROR: No Equipment found for Barcode: " + idToQuery);
		}
	}

	//----------------------------------------------------------
	public Equipment(Properties props, boolean check) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		oldFlag = false;
		persistentState = new Properties();
		
		if (props.getProperty("Barcode") == null) throw new InvalidPrimaryKeyException("ERROR: Idiot - provide a barcode at least");
		
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			System.out.println(nextKey + "   ----------   " + nextValue);
			
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
			if (oldFlag == true)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Barcode", persistentState.getProperty("Barcode"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Equipment: " + persistentState.getProperty("Barcode") + " updated successfully!";
			}
			else
			{
				insertPersistentState(mySchema, persistentState);
				oldFlag = true;
				updateStatusMessage = "Equipment: " + persistentState.getProperty("Barcode") + " inserted successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "ERROR: Equipment could not be installed in database!";
			new Event(Event.getLeafLevelClassName(this), "updateStateInDatabase", "Equipment with Barcode : " + 
				persistentState.getProperty("Barcode") + " could not be saved in database: " + ex.toString(), Event.ERROR);
		}
			
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


