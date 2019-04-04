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
import model.Person;

/** The class containing the AddBorrowerTransaction for the KSSPE application */
//==============================================================
public class AddBorrowerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Borrower myBorrower;
	private Person myPerson; 

	public AddBorrowerTransaction() throws Exception
	{
		super();
	}

	public void processTransaction(Properties props)
	{
		try
		{
			Borrower b = new Borrower(props);
			
			errorMessage = "ERROR: Borrower with id: " + b.getState("BannerId") + " already exists!";	
		}
		catch (InvalidPrimaryKeyException ex) 
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
				
				errorMessage = "Borrower with id: " + myBorrower.getState("BannerId") + " added Successfully";
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
				myBorrower = new Borrower((Properties)value);
				
				errorMessage = "ERROR: Borrower with Bannerid " + ((Properties)value).getProperty("BannerId") + " already exists!";
		
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

