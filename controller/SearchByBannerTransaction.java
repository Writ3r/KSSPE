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

/** The class containing the AddArticleTypeTransaction for the Professional Clothes Closet application */
//==============================================================
public class SearchByBannerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Person myPerson = null;

	public SearchByBannerTransaction() throws Exception
	{
		super();
	}

	public void processTransaction(Properties props)
	{
		
	}
		
	private void createNewWorker(Properties workerInfo) //helper method. 
	{
		Worker newWorker = new Worker(workerInfo);
		newWorker.createNewRecord();
		errorMessage = (String)newWorker.getState("UpdateStatusMessage");
	}
	
	private void getPersonData(String s)
	{
		try
		{
			Worker oldWorker = new Worker(s);
			errorMessage = "ERROR: Worker with ID: " + s + " already exists!";
		}
		catch (Exception ex)
		{
			try
			{
				myPerson = new Person(s);
			}
			catch (Exception e)
			{
				//do nothing. Idc either way. 
			}
			errorMessage = "";
		}
		
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("FirstName") == true)
		{
			if(myPerson != null)
				return myPerson.getState("FirstName");
			else
				return null;
		}
		else if (key.equals("LastName") == true)
		{
			if(myPerson != null)
				return myPerson.getState("LastName");
			else
				return null;
		}
		else if (key.equals("Email") == true)
		{
			if(myPerson != null)
				return myPerson.getState("Email");
			else
				return null;
		}
		else if (key.equals("PhoneNumber") == true)
		{
			if(myPerson != null)
				return myPerson.getState("PhoneNumber");
			else
				return null;
		}
		else
			return null;
	}

	public void stateChangeRequest(String key, Object value)
	{
		// DEBUG System.out.println("AddArticleTypeTransaction.sCR: key: " + key);
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
			getPersonData((String)value);
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
		Scene currentScene = myViews.get("SearchByBannerView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchBannerView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchBannerView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

