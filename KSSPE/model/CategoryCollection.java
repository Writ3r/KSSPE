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


/** The class containing the CategoryCollection for the KSSPE
 *  application 
 */
//==============================================================
public class CategoryCollection extends EntityBase
{
	private static final String myTableName = "Category";

	private Vector<Category> categories;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public CategoryCollection( ) 
	{
		super(myTableName);
	}

	//-----------------------------------------------------------
	private void populateCollectionHelper(String query)
	{

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			categories = new Vector<Category>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextCData = allDataRetrieved.elementAt(cnt);
				
				try
				{
					Category c = new Category(nextCData);
					
					addCategory(c);
				}
				catch(Exception e)
				{
					new Event(Event.getLeafLevelClassName(this), "populateCollectionHelper",
					"Error in creating a category from category collection", Event.ERROR);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findByBarcodePrefix(String barcodePrefix)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((Status = 'Active') AND (BarcodePrefix LIKE '%" + barcodePrefix + "%'))";
		populateCollectionHelper(query);
	}
	
	//-----------------------------------------------------------
	public void findAllByBarcodePrefix(String barcodePrefix)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (BarcodePrefix LIKE '%" + barcodePrefix + "%')";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findAll()
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (Status = 'Active')";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findByName(String name)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((Status = 'Active') AND (Name LIKE '%" + name + "%'))";
		populateCollectionHelper(query);
	}
	
	//-----------------------------------------------------------
	public void findAllByName(String name)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (Name = '" + name + "')";
		populateCollectionHelper(query);
	}


	//----------------------------------------------------------------------------------
	private void addCategory(Category c)
	{
		int index = findIndexToAdd(c);
		categories.insertElementAt(c,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Category c)
	{
		
		int low=0;
		int high = categories.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Category midSession = categories.elementAt(middle);

			int result = Category.compare(c,midSession);

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
		if (key.equals("Categories"))
			return categories;
		else
			if (key.equals("CategoryList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
	}

	//----------------------------------------------------------
	public Category retrieve(String barcodePrefix)
	{
		Category retValue = null;
		for (int cnt = 0; cnt < categories.size(); cnt++)
		{
			Category nextC = categories.elementAt(cnt);
			String nextBarcodePrefix = (String)nextC.getState("BarcodePrefix");
			if (nextBarcodePrefix.equals(barcodePrefix) == true)
			{
				retValue = nextC;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}

	//----------------------------------------------------------
	public void remove(String barcodePrefix)
	{
		for (int cnt = 0; cnt < categories.size(); cnt++)
		{
			Category nextC = categories.elementAt(cnt);
			String nextBarcodePrefix = (String)nextC.getState("BarcodePrefix");
			if (nextBarcodePrefix.equals(barcodePrefix) == true)
			{
				categories.remove(cnt);
			}
		}
	}
	
	//----------------------------------------------------------
	public int getSize()
	{
		int numElems = 0;
		if (categories != null)
			numElems = categories.size();
		
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
