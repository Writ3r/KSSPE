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
import model.Borrower;
import model.Worker;
import model.Person;

/** The class containing the AddBorrowerTransaction for the KSSPE application */
//==============================================================
public class AddBorrowerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Borrower myBorrower;

	public AddBorrowerTransaction() throws Exception
	{
		super();
	}

	public void processTransaction(Properties props)
	{
		if (myBorrower != null)
		{
			
			myBorrower.stateChangeRequest("Status", "Active");
			myBorrower.stateChangeRequest("FirstName", props.getProperty("FirstName"));
			myBorrower.stateChangeRequest("LastName", props.getProperty("LastName"));
			myBorrower.stateChangeRequest("Email", props.getProperty("Email"));
			myBorrower.stateChangeRequest("Penalty", "0");
			myBorrower.stateChangeRequest("BlockStatus", "Unblocked");
			myBorrower.stateChangeRequest("PhoneNumber", props.getProperty("PhoneNumber"));
			myBorrower.stateChangeRequest("Notes", props.getProperty("Notes"));
			myBorrower.stateChangeRequest("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			myBorrower.save();
			
			errorMessage = (String)myBorrower.getState("UpdateStatusMessage");
			
			if (errorMessage.startsWith("ERR") == false)
			{
				errorMessage = "Borrower with id: " + myBorrower.getState("BannerId") + " saved/reinstated successfully";
			}
			
		}
		else
		{
			try
			{
				props.setProperty("Status", "Active");
				props.setProperty("BlockStatus", "Unblocked");
				props.setProperty("Penalty", "0");
				props.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				props.setProperty("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				myBorrower = new Borrower(props, true);
				myBorrower.save();
				
				errorMessage = (String)myBorrower.getState("UpdateStatusMessage");
				if (errorMessage.startsWith("ERR") == false)
				{
					errorMessage = "Borrower with id: " + myBorrower.getState("BannerId") + " added successfully";
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
		else if (key.equals("TestBorrower") == true)
		{
			if(myBorrower != null)
			{
				return true;
			}
			return false;
		}
		else
		{
			String val = (String)myBorrower.getState(key);
			if (val != null)
				return val;
			else
				return null;
		}
	}

	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("BorrowerData") == true)
		{
			processTransaction((Properties)value);
		}
		if(key.equals("processBannerId") == true)
		{
			try
			{
				myBorrower = new Borrower((Properties)value);
				
				if(myBorrower.getState("Status").equals("Active"))
				{
					errorMessage = "ERROR: Borrower with id " + ((Properties)value).getProperty("BannerId") + " already exists!";
				}
				else
				{
					errorMessage = "Former Borrower with id " + ((Properties)value).getProperty("BannerId") +  " found!";
				}
			}
			catch(Exception ex)
			{
				try
				{
					Worker w = new Worker((Properties)value);
					
					Properties bProp = new Properties();
					bProp.setProperty("BannerId", (String)w.getState("BannerId"));
					bProp.setProperty("Status", "Active");
					bProp.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					bProp.setProperty("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					bProp.setProperty("Penalty", "0");
					bProp.setProperty("BlockStatus", "Unblocked");
					bProp.setProperty("PhoneNumber", "");
					bProp.setProperty("Notes", "");
					myBorrower = new Borrower(bProp, true);
					
					errorMessage = "Person with id " + ((Properties)value).getProperty("BannerId") + " found!";
					
				}
				catch (Exception excep)
				{
					//do nothing here. If here, nothing exists to fill. 
				}
				
			}
		}
		if(key.equals("clearState") == true)
		{
			myBorrower = null;
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
		Scene currentScene = myViews.get("AddBorrowerView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("AddBorrowerView", this);
			currentScene = new Scene(newView);
			myViews.put("AddBorrowerView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

