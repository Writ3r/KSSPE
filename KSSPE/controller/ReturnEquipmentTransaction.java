// specify the package
package controller;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;
import model.Borrower;
import model.CheckOut;
import model.CheckOutCollection;
import model.Equipment;
import model.BorrowerCollection;
import model.CheckIn;

/** The class containing the ReturnEquipmentTransaction for the KSSPE application */
//==============================================================
public class ReturnEquipmentTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Borrower myBorrower;
	private BorrowerCollection myBorrowerList;
	private CheckOut myCheckOut;
	private CheckOutCollection myCheckOutList;
	private Equipment myCurrentEquipment;
	private Vector<Properties> returnedEquipment = new Vector<Properties>();
	private String myWorkerId;
	private Boolean continueScreenCreation = true;


	//----------------------------------------------------------------
	public ReturnEquipmentTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------------
	public void processTransaction(Properties props)
	{
		myBorrowerList = new BorrowerCollection();
		
		if (props.getProperty("BannerId") != null)
		{
			String bannerId = props.getProperty("BannerId");
			myBorrowerList.findByBannerId(bannerId);
		}
		else if(props.getProperty("FirstName") != null || props.getProperty("LastName") != null)
		{
			if(props.getProperty("FirstName") != null && props.getProperty("LastName") != null)
			{
				myBorrowerList.findByFirstAndLast(props);
			}
			else if(props.getProperty("FirstName") != null)
			{
				String name = props.getProperty("FirstName");
				myBorrowerList.findByFirstName(name);
			}
			else
			{
				String name = props.getProperty("LastName");
				myBorrowerList.findByLastName(name);
			}
		}
		else
		{
			myBorrowerList.findAll();
		}
		
		try
		{	
			Scene newScene = createBorrowerCollectionView();	
			swapToView(newScene);
		}
		catch (Exception ex)
		{
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Error in creating BorrowerCollectionView", Event.ERROR);
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("BorrowerList") == true)
		{
			return myBorrowerList;
		}
		else if (key.equals("BannerId") == true)
		{
			return myBorrower.getState("BannerId");
		}
		else if (key.equals("FirstName") == true)
		{
			return myBorrower.getState("FirstName");
		}
		else if (key.equals("LastName") == true)
		{
			return myBorrower.getState("LastName");
		}
		else if (key.equals("Email") == true)
		{
			return myBorrower.getState("Email");
		}
		else if (key.equals("PhoneNumber") == true)
		{
			return myBorrower.getState("PhoneNumber");
		}
		else if (key.equals("Penalty") == true)
		{
			return myBorrower.getState("Penalty");
		}
		else if (key.equals("BlockStatus") == true)
		{
			return myBorrower.getState("BlockStatus");
		}
		else if (key.equals("Notes") == true)
		{
			return myBorrower.getState("Notes");
		}
		else if (key.equals("EquipmentName") == true)
		{
			if (myCurrentEquipment != null)
				return myCurrentEquipment.getState("Name");
			else
				return "Unknown Equipment";
		}
		else if (key.equals("Barcode") == true)
		{
			if (myCheckOut != null)
				return myCheckOut.getState("Barcode");
			else
				return "Unknown barcode";
		}
		else if (key.equals("DefaultUnitsReturned") == true)
		{
			if (myCheckOut != null)
			{
				try
				{
					int taken  = Integer.parseInt(myCheckOut.getState("UnitsTaken") + "");
					int totalUR = Integer.parseInt(myCheckOut.getState("TotalUnitsReturned") + "");
					return "" + (taken - totalUR);
				}
				catch (Exception e)
				{
					return "0";
				}
			}
			else
				return "0";
		}
		else if (key.equals("CheckOutList") == true)
		{
			return myCheckOutList;
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
			myWorkerId = (String)myReceptionist.getState("BannerId");
			doYourJob();
		}
		if (key.equals("SearchBorrower") == true)
		{
			processTransaction((Properties)value);
		}
		if (key.equals("BorrowerSelected") == true)
		{
			myBorrower = myBorrowerList.retrieve((String)value);
			myCheckOutList = new CheckOutCollection();
			myCheckOutList.findPendingByBannerId((String)getState("BannerId"));
			// DEBUG System.out.println("Size of check out list: " + myCheckOutList.getSize());
			try
			{
				Scene newScene = createCheckOutCollectionView();
				if(continueScreenCreation)
					swapToView(newScene);
				else
					myReceptionist.stateChangeRequest("CancelBorrowerList", null);
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
						"Error in creating CheckOutCollectionView", Event.ERROR);
			}
		}

		if (key.equals("CheckOutSelected") == true)
		{
			myCheckOut = myCheckOutList.retrieve((String)value);
			String barcode = (String)myCheckOut.getState("Barcode");
			try
			{
				Properties p = new Properties();
				p.setProperty("Barcode", barcode);
				myCurrentEquipment = new Equipment(p);
				try
				{
					Scene newScene = createReturnEquipmentView();
					if(continueScreenCreation)
						swapToView(newScene);
					else
						myReceptionist.stateChangeRequest("CancelCheckOutList", null);
				}
				catch (Exception ex)
				{
					new Event(Event.getLeafLevelClassName(this), "processTransaction",
							"Error in creating ReturnEquipmentView", Event.ERROR);
				}
			}
			catch (Exception excep1)
			{
				System.out.println("Exception: " + excep1);
				errorMessage = "ERROR: No equipment with barcode: " + barcode;
			}
			
		}
		if (key.equals("CheckInData") == true)
		{
			
			try
			{
				returnEquipment((Properties)value);
			}
			catch (Exception except2)
			{
				System.out.println("Exception: " + except2);
				except2.printStackTrace();
			}
			
		}

		/*
		if (key.equals("TestEquipment") == true)
		{
			myCurrentEquipment = null; //clears out past equipment.
			
			try
			{
				myCurrentEquipment = new Equipment((Properties)value);
				
				errorMessage = "Equipment: [" + myCurrentEquipment.getState("Name") + "] found!";
			}
			catch(Exception ex)
			{
				errorMessage = "ERROR: Equipment with Barcode: " + ((Properties)value).getProperty("Barcode") +  " does not Exist!";
			}
		}
		*/

		if (key.equals("CancelBorrowerList") == true)
		{
			
			Scene oldScene = createView();	
			swapToView(oldScene);
		}
		if (key.equals("CancelCheckOutList") == true)
		{
			
			Scene oldScene = createView();	
			swapToView(oldScene);
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		if (key.equals("CancelTransactionAfterLoad") == true)
		{
			continueScreenCreation = false;
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}
	
	//-----------------------------------------------------------------------
	protected Scene createCheckOutCollectionView()
	{
		Scene currentScene = myViews.get("CheckOutCollectionView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("CheckOutCollectionView", this);
			currentScene = new Scene(newView);
			myViews.put("CheckOutCollectionView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}

	}
	
	//--------------------------------------------------------------------------
	protected Scene createBorrowerCollectionView()
	{
		Scene currentScene;

		View newView = ViewFactory.createView("BorrowerCollectionView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}

	//------------------------------------------------------------------------
	protected Scene createReturnEquipmentView()
	{
		Scene currentScene = myViews.get("ReturnEquipmentView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("ReturnEquipmentView", this);
			currentScene = new Scene(newView);
			myViews.put("ReturnEquipmentView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}

	}

	//------------------------------------------------------------------------------------------------------
	private void returnEquipment(Properties props)
	{
		int stock = Integer.parseInt((String)myCurrentEquipment.getState("InStockCount"));
		int taken = Integer.parseInt((String)myCheckOut.getState("UnitsTaken"));
		int unitsReturned = Integer.parseInt(props.getProperty("UnitsReturned"));
		String rentDate = (String)myCheckOut.getState("RentDate");
		int totalUR = Integer.parseInt((String)myCheckOut.getState("TotalUnitsReturned"));
		
		
		if(totalUR + unitsReturned <= taken)
		{
			Calendar rightNow = Calendar.getInstance();
			Date today = rightNow.getTime();

			props.setProperty("BannerID", (String)myCheckOut.getState("BannerId"));
			props.setProperty("Barcode", (String)myCurrentEquipment.getState("Barcode"));
			props.setProperty("ReturnDate", (new SimpleDateFormat("yyyy-MM-dd")).format(today));
			props.setProperty("UnitsReturned", unitsReturned + "");
			props.setProperty("CheckoutID", (String)myCheckOut.getState("ID"));
			props.setProperty("CheckInWorkerID", myWorkerId);
			
			CheckIn checkIn = new CheckIn(props);
			checkIn.save();
			
			myCheckOut.stateChangeRequest("TotalUnitsReturned", Integer.toString(unitsReturned + totalUR));

			myCheckOut.save();

			myCurrentEquipment.stateChangeRequest("InStockCount", Integer.toString(stock + unitsReturned));
			
			myCurrentEquipment.save();
			
			errorMessage = (String)checkIn.getState("UpdateStatusMessage");
			if (errorMessage.startsWith("ERROR") == false)
			{
				errorMessage = "Units: " + unitsReturned + " returned successfully!";
			}
			
			//receipt code
			Properties sendData = new Properties();
			sendData.setProperty("Name", (String)myCurrentEquipment.getState("Name"));
			sendData.setProperty("Barcode", (String)myCurrentEquipment.getState("Barcode"));
			sendData.setProperty("EquipmentName", (String)myCurrentEquipment.getState("Name"));
			sendData.setProperty("Count", props.getProperty("UnitsReturned"));
			sendData.setProperty("ReturnDate", props.getProperty("ReturnDate"));
			returnedEquipment.add(sendData);
		}
		else
			errorMessage = "ERROR: Cannot exceed the total " + taken + " units reserved!";
		
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchBorrowerView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchBorrowerView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchBorrowerView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

