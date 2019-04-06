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
import model.Person;

/** The class containing the AddWorkerTransaction for the KSSPE application */
//==============================================================
public class AddWorkerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Worker myWorker;
	private Person myPerson; 

	public AddWorkerTransaction() throws Exception
	{
		super();
	}

	public void processTransaction(Properties props)
	{
		try
		{
			myWorker = new Worker(props);
			
			if(myWorker.getState("Status").equals("Active")) //if a worker already exists and is active
			{
				errorMessage = "ERROR: Worker with id: " + myWorker.getState("BannerId") + " already exists!";
			}
			else //update the borrower with the default information and set it to active again.
			{
				myWorker.stateChangeRequest("Status", "Active");
				myWorker.stateChangeRequest("FirstName", props.getProperty("FirstName"));
				myWorker.stateChangeRequest("LastName", props.getProperty("LastName"));
				myWorker.stateChangeRequest("Email", props.getProperty("Email"));
				myWorker.stateChangeRequest("Credential", props.getProperty("Credential"));
				myWorker.stateChangeRequest("Password", props.getProperty("Password"));
				myWorker.stateChangeRequest("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				myWorker.save();
				
				errorMessage = "Worker with id: " + myWorker.getState("BannerId") + " reinstated Successfully";
			}			
		}
		catch (InvalidPrimaryKeyException ex) 
		{
			
			try
			{
				props.setProperty("Status", "Active");
				props.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				props.setProperty("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				myWorker = new Worker(props, true);
				myWorker.save();
				
				errorMessage = "Worker with id: " + myWorker.getState("BannerId") + " added successfully";
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
				if(myWorker.getState("Status").equals("Active"))
					return true;
				else
					return false;
			}
			return false;
		}
		else if (key.equals("FirstName") == true)
		{
			if(myPerson != null)
				return myPerson.getState("FirstName");
			return null;
		}
		else if (key.equals("LastName") == true)
		{
			if(myPerson != null)
				return myPerson.getState("LastName");
			return null;
		}
		else if (key.equals("Email") == true)
		{
			if(myPerson != null)
				return myPerson.getState("Email");
			return null;
		}
		else if (key.equals("PhoneNumber") == true)
		{
			if(myPerson != null)
				return myPerson.getState("PhoneNumber");
			return null;
		}
		else
			return null;
	}

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
		if(key.equals("getPersonData") == true)
		{
			try
			{
				myWorker = new Worker((Properties)value);
				
				if(myWorker.getState("Status").equals("Active"))
				{
					errorMessage = "ERROR: Worker with Bannerid " + ((Properties)value).getProperty("BannerId") + " already exists!";
				}
				else
				{
					try
					{
						myPerson = new Person((Properties)value);
						
						errorMessage = "Former Worker with Bannerid " + ((Properties)value).getProperty("BannerId") +  " Found!";
					}
					catch(Exception ex2)
					{
						//how the hell is this even possible???????????????? 
					}
				}
			}
			catch(Exception ex)
			{
				try
				{
					myPerson = new Person((Properties)value);
					
					errorMessage = "Person with Bannerid " + ((Properties)value).getProperty("BannerId") +  " Found!";
				}
				catch(Exception ex2)
				{
					//do nothing. Idc if nobody was found. 
				}
			}
		}
		if(key.equals("removePersonData") == true)
		{
			myPerson = null;
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

