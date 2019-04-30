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
public class CheckOut extends EntityBase
{
	private static final String myTableName = "CheckOut";
	
	// GUI Components
	private String updateStatusMessage = "";

	//----------------------------------------------------------
	public CheckOut(Properties props)
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

	//-------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();
		
		v.addElement((String)this.getState("Id"));
		v.addElement((String)this.getState("BannerId"));
		v.addElement((String)this.getState("Barcode"));
		v.addElement((String)this.getState("UnitsTaken"));
		v.addElement((String)this.getState("TotalUnitsReturned"));
		v.addElement((String)this.getState("RentDate"));
		v.addElement((String)this.getState("DueDate"));
		v.addElement((String)this.getState("CheckOutWorkerId"));
		
		return v;
	}
	
	//-----------------------------------------------------------------------------------
	public static int compare(CheckOut a, CheckOut b)
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
			updateStatusMessage = "Check Out record inserted successfully!";
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "ERROR: Check out record could not be installed in database!";
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


