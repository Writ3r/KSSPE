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


/** The class containing the WorkerCollection for the KSSPE
 *  application 
 */
//==============================================================
public class WorkerCollection extends EntityBase
{
	private static final String myTableName = "Worker";

	private Vector<Worker> workers;

	// constructor for this class
	//----------------------------------------------------------
	public WorkerCollection( ) 
	{
		super(myTableName);
	}

	//-----------------------------------------------------------
	private void populateCollectionHelper(String query)
	{

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			workers = new Vector<Worker>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextWorkerData = allDataRetrieved.elementAt(cnt);
				
				try
				{
					Worker w = new Worker(nextWorkerData);
					
					addWorker(w);
				}
				catch(Exception e)
				{
					new Event(Event.getLeafLevelClassName(this), "populateCollectionHelper",
					"Error in creating a worker from worker collection", Event.ERROR);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findByBannerId(String bannerId)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Worker.Status = 'Active') AND (Worker.BannerId = '" + bannerId + "') AND (Worker.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}

	//-------------------------------------------------------------
	public void findByFirstName(String firstname)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Worker.Status  = 'Active') AND (FirstName LIKE '%" + firstname + "%') AND (Worker.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}
	
	//-------------------------------------------------------------------
	public void findByLastName(String lastname)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Worker.Status  = 'Active') AND (LastName LIKE '%" + lastname + "%') AND (Worker.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}
	
	//---------------------------------------------------------------
	public void findByFirstAndLast(Properties props)
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Worker.Status  = 'Active') AND (LastName LIKE '%" + props.getProperty("LastName") + "%') AND (FirstName LIKE '%" + props.getProperty("FirstName") + "%') AND (Worker.BannerId = Person.BannerId))";
		populateCollectionHelper(query);
	}

	//----------------------------------------------------------------
	public void findAll()
	{
		String query = "SELECT * FROM " + myTableName + ", Person WHERE ((Worker.BannerId = Person.BannerId) AND (Worker.Status = 'Active'))";
		populateCollectionHelper(query);
	}

	//----------------------------------------------------------------------------------
	private void addWorker(Worker w)
	{
		int index = findIndexToAdd(w);
		workers.insertElementAt(w,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Worker w)
	{
		
		int low=0;
		int high = workers.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Worker midSession = workers.elementAt(middle);

			int result = Worker.compare(w, midSession);

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
		if (key.equals("Workers"))
			return workers;
		else
			if (key.equals("WorkerList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
	}

	//----------------------------------------------------------
	public Worker retrieve(String bannerId)
	{
		Worker retValue = null;
		for (int cnt = 0; cnt < workers.size(); cnt++)
		{
			Worker nextW = workers.elementAt(cnt);
			String nextBannerId = (String)nextW.getState("BannerId");
			if (nextBannerId.equals(bannerId) == true)
			{
				retValue = nextW;
				return retValue;
			}
		}

		return retValue;
	}

//----------------------------------------------------------
	public void remove(String bannerId)
	{
		for (int cnt = 0; cnt < workers.size(); cnt++)
		{
			Worker nextW = workers.elementAt(cnt);
			String nextBannerId = (String)nextW.getState("BannerId");
			if (nextBannerId.equals(bannerId) == true)
			{
				workers.remove(cnt);
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
