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
import database.*;

/** The class containing the Category for the KSSPE application */
//==============================================================
public class Category extends EntityBase
{
	private static final String myTableName = "Category";
	private boolean oldFlag = true;
	
	// GUI Components
	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Category(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		String idToQuery = props.getProperty("BarcodePrefix");

		String query = "SELECT * FROM " + myTableName + " WHERE (BarcodePrefix = " + idToQuery + " )";

		Vector allDataRetrieved =  getSelectQueryResult(query);

		// You must get one category at least
		if (allDataRetrieved != null && allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one category. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple categories matching prefix : "
					+ idToQuery + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedPersonData = (Properties)allDataRetrieved.elementAt(0);
				persistentState = new Properties();
				
				Enumeration allKeys = retrievedPersonData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedPersonData.getProperty(nextKey);
					
					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}
				
				oldFlag = true;
			}
		}
		// If no Categories found for this user name, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("ERROR: No Categories found for Banner Id: " + idToQuery);
		}
	}

	//----------------------------------------------------------
	public Category(Properties props, boolean check) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		oldFlag = false;
		persistentState = new Properties();
		
		if (props.getProperty("BarcodePrefix") == null) throw new InvalidPrimaryKeyException("ERROR: provide a prefix at least");
		
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);
			
			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}
		
		
	}
	
	//---------------------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("BarcodePrefix"));
		v.addElement(persistentState.getProperty("Name"));
		v.addElement(persistentState.getProperty("Status"));

		return v;
	}
	
	
	//-----------------------------------------------------------------------------------
	public static int compare(Category a, Category b)
	{
		String aVal = (String)a.getState("Name");
		String bVal = (String)b.getState("Name");

		return aVal.compareTo(bVal);
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
				whereClause.setProperty("BarcodePrefix", persistentState.getProperty("BarcodePrefix"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Category: " + persistentState.getProperty("BarcodePrefix") + " updated successfully!";
			}
			else
			{
				insertPersistentState(mySchema, persistentState);
				oldFlag = true;
				updateStatusMessage = "Category: " + persistentState.getProperty("BarcodePrefix") + " inserted successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "ERROR: Category could not be installed in database!";
			new Event(Event.getLeafLevelClassName(this), "updateStateInDatabase", "Category with BannerID : " + 
				persistentState.getProperty("BarcodePrefix") + " could not be saved in database: " + ex.toString(), Event.ERROR);
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


