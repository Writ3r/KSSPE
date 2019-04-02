// specify the package
package controller;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import java.util.Date;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;
import model.Worker;
import model.WorkerCollection;

/** The class containing the RemoveWorkerTransaction for the KSSPE application */
//==============================================================
public class RemoveWorkerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Worker myWorker;
	private WorkerCollection myWorkerList;

	//----------------------------------------------------------------
	public RemoveWorkerTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------------
	public void processTransaction(Properties props)
	{
		myWorkerList = new WorkerCollection();
		
		if (props.getProperty("BannerId") != null)
		{
			String bannerId = props.getProperty("BannerId");
			myWorkerList.findByBannerId(bannerId);
		}
		else if(props.getProperty("FirstName") != null || props.getProperty("LastName") != null)
		{
			if(props.getProperty("FirstName") != null && props.getProperty("LastName") != null)
			{
				myWorkerList.findByFirstAndLast(props);
			}
			else if(props.getProperty("FirstName") != null)
			{
				String name = props.getProperty("FirstName");
				myWorkerList.findByFirstName(name);
			}
			else
			{
				String name = props.getProperty("LastName");
				myWorkerList.findByLastName(name);
			}
		}
		else
		{
			myWorkerList.findAll();
		}
		
		try
		{	
			Scene newScene = createWorkerCollectionView();	
			swapToView(newScene);
		}
		catch (Exception ex)
		{
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Error in creating WorkerCollectionView", Event.ERROR);
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("Alert") == true)
		{
			return true;
		}
		else if (key.equals("WorkerList") == true)
		{
			return myWorkerList;
		}
		else if (key.equals("BannerId") == true)
		{
			return myWorker.getState("BannerId");
		}
		else if (key.equals("FirstName") == true)
		{
			return myWorker.getState("FirstName");
		}
		else if (key.equals("LastName") == true)
		{
			return myWorker.getState("LastName");
		}
		else if (key.equals("Email") == true)
		{
			return myWorker.getState("Email");
		}
		else if (key.equals("PhoneNumber") == true)
		{
			return myWorker.getState("PhoneNumber");
		}
		else if (key.equals("Credential") == true)
		{
			return myWorker.getState("Credential");
		}
		else
			return null;
	}

	//-------------------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("SearchWorker") == true)
		{
			processTransaction((Properties)value);
		}
		if (key.equals("WorkerSelected") == true)
		{
			myWorker = myWorkerList.retrieve((String)value);
			
			removeWorkerHelper();
		}
		if (key.equals("CancelWorkerList") == true)
		{
			Scene oldScene = createView();	
			swapToView(oldScene);
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}
	
	//------------------------------------------------------------------------
	private void removeWorkerHelper()
	{
		myWorker.stateChangeRequest("Status", "Inactive");
		myWorker.stateChangeRequest("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		myWorker.save();
		
		errorMessage = (String)myWorker.getState("UpdateStatusMessage");
		
		if(errorMessage.startsWith("ERR") == false)
		{
			if (myWorkerList != null)
			{
				myWorkerList.remove((String)myWorker.getState("BannerId"));
			}
			errorMessage = errorMessage.replace("updated", "removed");
		}
	}
	
	//--------------------------------------------------------------------------
	protected Scene createWorkerCollectionView()
	{
		Scene currentScene;

		View newView = ViewFactory.createView("WorkerCollectionView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchWorkerView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchWorkerView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchWorkerView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

