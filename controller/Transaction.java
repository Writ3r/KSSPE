// specify the package
package controller;

// system imports
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Observable;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import impresario.*;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.WindowPosition;

/** The class containing Transaction for the KSSPE application */
//==============================================================
abstract public class Transaction extends Observable
{

	protected Properties dependencies;
	protected Stage myStage;
	protected Hashtable<String, Scene> myViews;

	//----------------------------------------------------------
	protected Transaction() throws Exception
	{

		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

	}

	//---------------------------------------------------------
	protected abstract Scene createView();

	//---------------------------------------------------------
	protected void doYourJob()
	{
		try
		{
			Scene newScene = createView();

			swapToView(newScene);

		}
		catch (Exception ex)
		{
			new Event(Event.getLeafLevelClassName(this), "Transaction",
					"Error in creating Transaction view", Event.ERROR);
		}
	}
	
	//-----------------------------------------------------------
	public abstract Object getState(String key);

	//-----------------------------------------------------------
	public abstract void stateChangeRequest(String key, Object value);
	
	//-----------------------------------------------------------------------------
	public void swapToView(Scene newScene)
	{

		if (newScene == null)
		{
			System.out.println("Transaction.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
					"Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();

		WindowPosition.placeCenter(myStage);

	}

}

