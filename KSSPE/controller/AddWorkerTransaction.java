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
import model.Borrower;

/** The class containing the AddWorkerTransaction for the KSSPE application */
//==============================================================
public class AddWorkerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Worker myWorker; 

	//-----------------------------------------------------------
	public AddWorkerTransaction() throws Exception
	{
		super();
	}

	//-----------------------------------------------------------
	public void processTransaction(Properties props)
	{
		if (myWorker != null)
		{
			
			myWorker.stateChangeRequest("Status", "Active");
			myWorker.stateChangeRequest("FirstName", props.getProperty("FirstName"));
			myWorker.stateChangeRequest("LastName", props.getProperty("LastName"));
			myWorker.stateChangeRequest("Email", props.getProperty("Email"));
			myWorker.stateChangeRequest("PhoneNumber", props.getProperty("PhoneNumber"));
			myWorker.stateChangeRequest("Credential", props.getProperty("Credential"));
			myWorker.stateChangeRequest("Password", props.getProperty("Password"));
			myWorker.stateChangeRequest("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			myWorker.save();
			
			errorMessage = (String)myWorker.getState("UpdateStatusMessage");
			if (errorMessage.startsWith("ERR") == false)
			{
				errorMessage = "Worker with id: " + myWorker.getState("BannerId") + " saved/reinstated successfully";
			}
			
		}
		else
		{
			try
			{
				props.setProperty("Status", "Active");
				props.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				props.setProperty("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				myWorker = new Worker(props, true);
				myWorker.save();
				
				errorMessage = (String)myWorker.getState("UpdateStatusMessage");
				if (errorMessage.startsWith("ERR") == false)
				{
					errorMessage = "Worker with id: " + myWorker.getState("BannerId") + " added successfully";
				}
			}
			catch (InvalidPrimaryKeyException ex2) 
			{
				errorMessage = ex2.getMessage();
			}
			
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("TestWorker") == true)
		{
			if(myWorker != null)
			{
				return true;
			}
			return false;
		}
		else
		{
			String val = (String)myWorker.getState(key);
			if (val != null)
				return val;
			else
				return null;
		}
	}

	//-----------------------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("WorkerData") == true)
		{
			processTransaction((Properties)value);
		}
		if(key.equals("processBannerId") == true)
		{
			try
			{
				myWorker = new Worker((Properties)value);
				
				if(myWorker.getState("Status").equals("Active"))
				{
					errorMessage = "ERROR: Worker with id " + ((Properties)value).getProperty("BannerId") + " already exists!";
				}
				else
				{
					errorMessage = "Former Worker with id " + ((Properties)value).getProperty("BannerId") +  " found!";
				}
			}
			catch(Exception ex)
			{
				try
				{
					Borrower b = new Borrower((Properties)value);
					
					Properties wProp = new Properties();
					wProp.setProperty("BannerId", (String)b.getState("BannerId"));
					wProp.setProperty("Password", "Default"); //this is overwritten when the user enters it. 
					wProp.setProperty("Credential", "Ordinary");
					wProp.setProperty("Status", "Active");
					wProp.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					wProp.setProperty("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					myWorker = new Worker(wProp, true);
					
					errorMessage = "Person with id " + ((Properties)value).getProperty("BannerId") + " found!";
					
				}
				catch (Exception excep)
				{
					errorMessage = "Enter data for worker with id " + ((Properties)value).getProperty("BannerId");
				}
				
			}
		}
		if(key.equals("clearState") == true)
		{
			myWorker = null;
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("AddWorkerView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("AddWorkerView", this);
			currentScene = new Scene(newView);
			myViews.put("AddWorkerView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

