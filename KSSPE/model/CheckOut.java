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

	private Equipment myEquipment;
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
		
		String barcodeSent = (String)getState("Barcode");
		if (barcodeSent != null)
		{
			try
			{
				Properties p = new Properties();
				p.setProperty("Barcode", barcodeSent);
				myEquipment = new Equipment(p);
			}
			catch (InvalidPrimaryKeyException excep)
			{
				myEquipment = null;
			}
		}
		else
		{
			myEquipment = null;
		}
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;
		
		if (key.equals("Equipment") == true)
			return myEquipment;
		
		String val = persistentState.getProperty(key);
		if (val != null)
		{
			return val;
		}
		else
		{
			if (myEquipment != null)
			{
				return myEquipment.getState(key);
			}
			else
			{
				return null;
			}
		}
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		persistentState.setProperty(key, (String)value);
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
		if (myEquipment != null)
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
		else
		{
			updateStatusMessage = "ERROR: Checking out invalid equipment!";
			new Event(Event.getLeafLevelClassName(this), "updateStateInDatabase", "Checkout record is not associated with valid equipment Barcode sent was: " + 
					persistentState.getProperty("Barcode"), Event.ERROR);	
		}
			
	}
	
	//-------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("ID"));
		v.addElement(persistentState.getProperty("BannerId"));
		v.addElement(persistentState.getProperty("Barcode"));
		if (myEquipment != null)
			v.addElement((String)myEquipment.getState("Barcode"));
		else
			v.addElement("Unknown barcode");
		v.addElement(persistentState.getProperty("UnitsTaken"));
		v.addElement(persistentState.getProperty("TotalUnitsReturned"));
		v.addElement(persistentState.getProperty("DueDate"));
		v.addElement(persistentState.getProperty("RentDate"));
		//v.addElement(persistentState.getProperty("Status"));

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


