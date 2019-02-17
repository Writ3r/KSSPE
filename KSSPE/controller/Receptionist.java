// specify the package
package controller;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.ISlideShow;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

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
		if (key.equals("bannerID") == true)
		{
			if (currentWorker != null)
			{
				return currentWorker.getState("bannerID");
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

				boolean flag = loginAccountHolder((Properties)value);
				
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
		if ((key.equals("AddWorker") == true) || (key.equals("UpdateArticleType") == true) ||
			(key.equals("RemoveArticleType") == true) || (key.equals("AddColor") == true) ||
			(key.equals("UpdateColor") == true) || (key.equals("RemoveColor") == true) ||
			(key.equals("AddClothingItem") == true) || (key.equals("UpdateClothingItem") == true) ||
			(key.equals("RemoveClothingItem") == true) || (key.equals("CheckoutClothingItem") == true) ||
			(key.equals("AddRequest") == true) || (key.equals("FulfillRequest") == true) ||
			(key.equals("RemoveRequest") == true) || (key.equals("ListAvailableInventory") == true) ||
            (key.equals("UntillDateReport") == true) || (key.equals("TopDonatorReport") == true))
			{
				String transType = key;

				transType = transType.trim();
					
				if (currentWorker != null)
				{
					if(key.equals("AddWorker") && !currentWorker.getState("Credential").equals("Admin")){
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
			myViews.remove("ReceptionistView");
			errorMessage = "";
			createAndShowLoginView();
		}

		setChanged();
        notifyObservers(errorMessage);
		//myRegistry.updateSubscribers(key, this);
	}

	// Login Worker corresponding to user name and password.
	//----------------------------------------------------------
	public boolean loginAccountHolder(Properties props)
	{

		try
		{
			currentWorker = new Worker(props);
			// DEBUG System.out.println("Worker: " + currentWorker.getState("Name") + " successfully logged in");
			return true;
		}
		catch (InvalidPrimaryKeyException ex)
		{
			errorMessage = "ERROR: " + ex.getMessage();
			return false;
		}
		catch (PasswordMismatchException exec)
		{
			errorMessage = "ERROR: " + exec.getMessage();
			return false;
		}
		catch (NullPointerException ex)
		{
			errorMessage = "ERROR: " + "Not Connected To Database";
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

