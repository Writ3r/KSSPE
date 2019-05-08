// specify the package
package controller;

// system imports
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;
import model.Borrower;
import model.Equipment;
import model.CheckOut;
import model.BorrowerCollection;
import utilities.ReserveReceipt;

/** The class containing the Reserve Equipment Transaction for the KSSPE application */
//==============================================================
public class ReserveEquipmentTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Borrower myBorrower;
	private Equipment myCurrentEquipment;
	private BorrowerCollection myBorrowerList;
	private Vector<Properties> reservedEquipment = new Vector<Properties>();
	private String myWorkerId;

	//----------------------------------------------------------------
	public ReserveEquipmentTransaction() throws Exception
	{
		super();
	}

	/**
     * This method actually just handles finding all the Borrowers who match
     * the entered search criteria in this transaction. Not the actual reservation
     * of equipment	 
	*/
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
		else if (props.getProperty("PhoneNumber") != null)
		{
			String phone = props.getProperty("PhoneNumber");
			myBorrowerList.findByPhone(phone);
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
		else if (key.equals("BorrowerBannerId") == true)
		{
			return myBorrower.getState("BannerId");
		}
		else if (key.equals("BorrowerName") == true)
		{
			return (String)myBorrower.getState("FirstName") + " " + (String)myBorrower.getState("LastName");
		}
		else if (key.equals("WorkerBannerId") == true)
		{
			return myWorkerId;
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
		else if (key.equals("TestEquipment") == true)
		{
			if(myCurrentEquipment != null)
				return true;
			else
				return false;
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
			
			try
			{
				Scene newScene = createReserveEquipmentView();
				swapToView(newScene);
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
						"Error in creating ModifyBorrowerView", Event.ERROR);
			}
		}
		if (key.equals("CheckOutData") == true)
		{
			reserveEquipment((Properties)value);
		}
		if (key.equals("TestEquipment") == true)
		{
			myCurrentEquipment = null; //clears out past equipment.
			
			try
			{
				myCurrentEquipment = new Equipment((Properties)value);
				
				errorMessage = "Equipment with Barcode: " + ((Properties)value).getProperty("Barcode") +  " found!";
			}
			catch(Exception ex)
			{
				errorMessage = "ERROR: No Equipment matching Barcode: " + ((Properties)value).getProperty("Barcode") +  " found!";
			}
		}
		if (key.equals("CancelBorrowerList") == true)
		{
			Scene oldScene = createView();	
			swapToView(oldScene);
		}
		if (key.equals("CancelTransactionAndMakeReceipt") == true)
		{
			
			if(!reservedEquipment.isEmpty())
			{
			
				Properties props = new Properties();
				props.setProperty("WorkerName", (String)myReceptionist.getState("Name"));
				props.setProperty("WorkerBannerId", myWorkerId);
				props.setProperty("BorrowerName", (String)this.getState("BorrowerName"));
				props.setProperty("BorrowerBannerId", (String)this.getState("BorrowerBannerId"));
				
				try
				{
					new ReserveReceipt(props, reservedEquipment);
				}
				catch(Exception ex)
				{
					errorMessage = ex.getMessage();
				}
					
				myReceptionist.stateChangeRequest("CancelTransaction", null);
			}
			else
				myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		
		setChanged();
        notifyObservers(errorMessage);
	}

	//--------------------------------------------------------------
	private boolean checkForPenaltiesAndBlocks()
	{
		String penalty = (String)this.getState("Penalty");
		String block = (String)this.getState("BlockStatus");
		
		double penaltyVal = 0.0;
		try
		{
			penaltyVal = Double.parseDouble(penalty);
		}
		catch (Exception ex) {}
		
		String blockVal = "Unblocked";
		if (block != null)
		{
			blockVal = block;
		}
		
		boolean borrowerBlocked = (penaltyVal > 0.0) || (blockVal.equals("Unblocked") == false);

		if (borrowerBlocked)
		{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"BannerId: "+(String)getState("BorrowerBannerId")
					+"\nBlocked Status: "+ block
					+"\nPenalty: " + penalty + ".\n OK to proceed with check out?", ButtonType.OK, ButtonType.CANCEL);
					
			alert.setTitle("Penalized or Blocked Borrower");
			alert.setHeaderText("Borrower has been Blocked or Penalized.");
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("images/BPT_LOGO_All-In-One_Color.png"));
			Optional<ButtonType> result= alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	//------------------------------------------------------------------------
	private void reserveEquipment(Properties props)
	{
		boolean OKtoProceed = checkForPenaltiesAndBlocks();
		
		if (OKtoProceed == true)
		{
			int stock = Integer.parseInt((String)myCurrentEquipment.getState("InStockCount"));
			int taken = Integer.parseInt(props.getProperty("UnitsTaken"));
			
			if(stock - taken >= 0)
			{
				Calendar cal = Calendar.getInstance();
				Date rightNow = cal.getTime();
				
				props.setProperty("BannerId", (String)getState("BorrowerBannerId"));
				props.setProperty("TotalUnitsReturned", "0");
				props.setProperty("RentDate", new SimpleDateFormat("yyyy-MM-dd").format(rightNow));
				props.setProperty("CheckOutWorkerID", myWorkerId);
				
				CheckOut checkOut = new CheckOut(props);
				checkOut.save();
				
				myCurrentEquipment.stateChangeRequest("InStockCount", Integer.toString(stock - taken));
				myCurrentEquipment.stateChangeRequest("DateLastUsed", new SimpleDateFormat("yyyy-MM-dd").format(rightNow));
				
				myCurrentEquipment.save();
				
				errorMessage = (String)checkOut.getState("UpdateStatusMessage");
				// DEBUG System.out.println("Lucas error message: " + errorMessage);
				if (errorMessage.startsWith("ERR") == false)
				{
					errorMessage = "Item successfully reserved!";
				}
				// DEBUG System.out.println("Liam error message: " + errorMessage);
				
				//receipt code
				Properties sendData = new Properties();
				sendData.setProperty("Name", (String)myCurrentEquipment.getState("Name"));
				sendData.setProperty("Barcode", (String)myCurrentEquipment.getState("Barcode"));
				sendData.setProperty("Count", props.getProperty("UnitsTaken"));
				sendData.setProperty("DueDate", props.getProperty("DueDate"));
				reservedEquipment.add(sendData);
			}
			else
				errorMessage = "ERROR: Cannot exceed the " + stock + " units in stock";
		}
		else
			errorMessage = "ERROR: Borrower with id: " + getState("BorrowerBannerId") + " blocked or has a fine!";
		
	}
	
	//-----------------------------------------------------------------------
	protected Scene createReserveEquipmentView()
	{
		Scene currentScene = myViews.get("ReserveEquipmentView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("ReserveEquipmentView", this);
			currentScene = new Scene(newView);
			myViews.put("ReserveEquipmentView", currentScene);

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

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchBorrowerReserveView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchBorrowerReserveView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchBorrowerReserveView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

