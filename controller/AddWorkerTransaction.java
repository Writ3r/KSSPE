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
public class AddWorkerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Person myPerson = null;

	public AddWorkerTransaction() throws Exception
	{
		super();
	}
	/*
		break up person/worker information. 
		Check if person exists or not. If not, create it. If so, modify it.
		Create a new worker with the information. 
	*/
	public void processTransaction(Properties props)
	{
		String bannerId = props.getProperty("BannerId");
		try
		{

			Worker oldWorker = new Worker(bannerId);
			errorMessage = "ERROR: Worker with ID: " + bannerId + " already exists!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Worker with Banner Id : " + bannerId + " already exists!",
					Event.ERROR);
		}
		catch (InvalidPrimaryKeyException ex) //we want to end up here since the Worker shouldn't already exist. 
		{
			props.setProperty("Status", "Active");
			
			Properties personInfo = Person.getPersonInfo(props);
			Properties workerInfo = Worker.getWorkerInfo(props);
			
			workerInfo.setProperty("DateAdded", new SimpleDateFormat("MM-dd-yyyy").format(new Date()));
			workerInfo.setProperty("DateLastUpdated", new SimpleDateFormat("MM-dd-yyyy").format(new Date()));

			try
			{
				Person oldPerson = new Person(bannerId); //checks if person exists already. Continues if so. 
				oldPerson.insertNewInformation(personInfo);
				oldPerson.update();
				
				createNewWorker(workerInfo);
				errorMessage = "New Worker Created!";
			}
			catch (InvalidPrimaryKeyException ex2) //ends up here if person does not already exist.  
			{
				Person newPerson = new Person(personInfo);
				newPerson.createNewRecord();
			
				createNewWorker(workerInfo);
				errorMessage = "New Worker and Person Created!";
			}
			catch (MultiplePrimaryKeysException ex2) //how the heck did you get here?
			{
				errorMessage = "ERROR: Multiple People with Banner ID!";
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Found multiple People with id : " + bannerId + ". Reason: " + ex2.toString(),
					Event.ERROR);
			}
			catch (NullPointerException e)
			{
				errorMessage = "ERROR: No Database connection!";
			}
		}
		catch (MultiplePrimaryKeysException ex2) //how the heck did you get here?
		{
			errorMessage = "ERROR: Multiple Workers with Banner ID!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Found multiple Workers with id : " + bannerId + ". Reason: " + ex2.toString(),
					Event.ERROR);
		}
		catch (NullPointerException e)
		{
			errorMessage = "ERROR: No Database connection!";
		}
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

