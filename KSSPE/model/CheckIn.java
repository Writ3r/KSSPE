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

/** The class containing the CheckOut for the KSSPE application */
//==============================================================
public class CheckIn extends EntityBase
{
	private static final String myTableName = "CheckIn";
	
	// GUI Components
	private String updateStatusMessage = "";

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
		String aVal = (String)a.getState("Barcode");
		String bVal = (String)b.getState("Barcode");

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
			Integer atID = insertAutoIncrementalPersistentState(mySchema, persistentState);
			persistentState.setProperty("ID", "" + atID.intValue());
			updateStatusMessage = "Check In record inserted successfully!";
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "ERROR: Check In record could not be installed in database!";
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


