// specify the package
package controller;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;


// project imports
import impresario.ISlideShow;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;
import exception.PasswordMismatchException;
import event.Event;



import controller.TransactionFactory;
import model.Worker;

/** The class containing the Receptionist for the KSSPE application */
//==============================================================
public class Receptionist extends Transaction
{
	//user account
	private Worker currentWorker;
	
	// GUI Components
	private Hashtable<String, Scene> myViews;
	private Stage myStage;

	private String errorMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Receptionist() throws Exception
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// Set up the initial view
		createAndShowLoginView();
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("LoginError") == true)
		{
			return errorMessage;
		}
		else
		if (key.equals("TransactionError") == true)
		{
			return errorMessage;
		}
		else
		if (key.equals("BannerId") == true)
		{
			if (currentWorker != null)
			{
				return currentWorker.getState("BannerId");
			}
			else
				return "Undefined";
		}
		if (key.equals("Credential") == true)
		{
			if (currentWorker != null)
			{
				return currentWorker.getState("Credential");
			}
			else
				return "Undefined";
		}
		if (key.equals("Name") == true)
		{
			if (currentWorker != null)
			{
				return (String)currentWorker.getState("FirstName") + " " + (String)currentWorker.getState("LastName");
			}
			else
				return "Undefined";
		}
		else
			return "";
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		if (key.equals("Login") == true)
		{
			if (value != null)
			{
				errorMessage = "";

				boolean flag = loginWorker((Properties)value);
				
				if (flag == true)
				{
					createAndShowReceptionistView();
				}
			}
		}
		else
		if (key.equals("CancelTransaction") == true)
		{
			createAndShowReceptionistView();
		}
		else
		if (key.equals("ExitSystem") == true)
		{
			System.exit(0);
		}	
		else
		if ((key.equals("AddWorker") == true) || (key.equals("ModifyWorker") == true) || 
			(key.equals("UpdateWorker") == true) || (key.equals("RemoveWorker")) || 
			(key.equals("AddBorrower") == true) || (key.equals("ModifyBorrower") == true) ||
			(key.equals("AddCategory") == true) || (key.equals("ModifyCategory") == true) || 
			(key.equals("RemoveCategory") == true) || (key.equals("RemoveBorrower") == true) || 
			(key.equals("AddEquipment") == true) || (key.equals("ReserveEquipment") == true) ||
			(key.equals("ListAllInventory") == true) || (key.equals("ListAvailableInventory") == true) ||
			(key.equals("ListReservedInventory") == true) || (key.equals("ListOverDueInventory") == true) || 
			(key.equals("ReturnEquipment") == true ))
			{
				String transType = key;
					
				if (currentWorker != null)
				{
					if(key.equals("AddWorker") && !currentWorker.getState("Credential").equals("Admin") || key.equals("ModifyWorker") && !currentWorker.getState("Credential").equals("Admin") || key.equals("RemoveWorker") && !currentWorker.getState("Credential").equals("Admin")){
						errorMessage = "You must be 'Admin' to do this.";
					}
					else
						doTransaction(transType);
				}
				else
				{
					errorMessage = "Transaction impossible: Login not identified";
				}
			}
		else
		if (key.equals("Logout") == true)
		{
			currentWorker = null;
			myViews.remove("ReceptionistView"); //resets receptionist view back to the origional state.
			myViews.remove("LoginView"); //resets loginview back to the origional state.
			errorMessage = "";
			createAndShowLoginView();
		}

		setChanged();
        notifyObservers(errorMessage);

	}

	// Login Worker corresponding to user name and password.
	//----------------------------------------------------------
	public boolean loginWorker(Properties props)
	{
		
		try
		{
			currentWorker = new Worker(props);
			
			if (((String)currentWorker.getState("Status")).equals("Active"))
			{
				try
				{
					currentWorker.checkPasswordMatch(props.getProperty("Password"));
					return true;
				}
				catch (PasswordMismatchException px)
				{
					errorMessage = px.getMessage();
					
					return false;
				} 
			}
			else
			{
				errorMessage = "ERROR: No valid worker found for id: " + props.getProperty("BannerId");
				return false;
			}
			
		}
		catch (InvalidPrimaryKeyException ex)
		{
			errorMessage = ex.getMessage();
			
			return false;
		}

	}
	//----------------------------------------------------------
	public void doTransaction(String transactionType)
	{
		
		try
		{
			Transaction trans = TransactionFactory.createTransaction(transactionType);
			trans.stateChangeRequest("DoYourJob", this);
		}
		catch (Exception ex)
		{
			errorMessage = "FATAL ERROR: Unrecognized transaction!!";
			new Event(Event.getLeafLevelClassName(this), "createTransaction",
					"Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
					Event.ERROR);
		} 
	}
	//------------------------------------------------------------
	private void createAndShowReceptionistView()
	{
        swapToView(createView());
	}
	
	private void createAndShowLoginView()
	{
		Scene currentScene = (Scene)myViews.get("LoginView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("LoginView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("LoginView", currentScene);
		}
				
		swapToView(currentScene);
		
	}

	protected Scene createView()
	{
		Scene currentScene = (Scene)myViews.get("ReceptionistView");
		
		if (currentScene == null)
		{
			View newView = ViewFactory.createView("ReceptionistView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("ReceptionistView", currentScene);
		}

        return currentScene;
	}

}

