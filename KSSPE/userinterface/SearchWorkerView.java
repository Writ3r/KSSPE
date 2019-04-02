// specify the package
package userinterface;

// system imports
import utilities.GlobalVariables;
import utilities.Utilities;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.scene.layout.ColumnConstraints;

import java.util.Properties;
import java.util.Observer;
import java.util.Observable;

// project imports
import java.util.Enumeration;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import controller.Transaction;

/** The class containing the Search Worker View for the KSSPE
 *  application 
 */
//==============================================================
public class SearchWorkerView extends View implements Observer
{

	// GUI components
	protected TextField bannerId;
	protected TextField firstName;
	protected TextField lastName;

	protected Text actionText;
	protected Text prompt;

	protected HBox bannerBox;
	protected HBox doneCont;
	protected Button submitButton;
	protected Button cancelButton;

	protected MessageView statusLog;

	// Constructor for this class -- takes a controller object
	//----------------------------------------------------------
	public SearchWorkerView(Transaction t)
	{
		super(t);

		VBox container = new VBox(10);
		container.setStyle("-fx-background-color: slategrey");
		container.setPadding(new Insets(15, 5, 5, 5));

		container.getChildren().add(createTitle());

		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		myController.addObserver(this);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** SEARCH FOR A WORKER **";
	}

	public void populateFields()
	{
		//not needed in this instance 
	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		VBox container = new VBox(10);
		container.setPadding(new Insets(1, 10, 1, 10));
		
        Text clientText = new Text("KSSPE DEPARTMENT");
			clientText.setFont(Font.font("Copperplate", FontWeight.EXTRA_BOLD, 36));
			clientText.setEffect(new DropShadow());
			clientText.setTextAlignment(TextAlignment.CENTER);
			clientText.setFill(Color.WHITESMOKE);
		container.getChildren().add(clientText);

		Text titleText = new Text(" Reservation Management System ");
			titleText.setFont(Font.font("Copperplate", FontWeight.THIN, 28));
			titleText.setTextAlignment(TextAlignment.CENTER);
			titleText.setFill(Color.GOLD);
		container.getChildren().add(titleText);

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		container.getChildren().add(blankText);

		actionText = new Text("     " + getActionText() + "       ");
			actionText.setFont(Font.font("Copperplate", FontWeight.BOLD, 22));
			actionText.setWrappingWidth(450);
			actionText.setTextAlignment(TextAlignment.CENTER);
			actionText.setFill(Color.DARKGREEN);
		container.getChildren().add(actionText);
		
		container.setAlignment(Pos.CENTER);

		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		Font myFont = Font.font("copperplate", FontWeight.THIN, 18);   

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		vbox.getChildren().add(blankText);

		
		GridPane grid = new GridPane();
			grid.setHgap(15);
			grid.setVgap(15);
			grid.setPadding(new Insets(0, 20, 25, 20));
			grid.setAlignment(Pos.CENTER);
			

		Text bannerIdHeader = new Text("Banner ID :");
			bannerIdHeader.setFill(Color.GOLD);
			bannerIdHeader.setFont(myFont);
			bannerIdHeader.setTextAlignment(TextAlignment.RIGHT);
		grid.add(bannerIdHeader, 0, 1);
			
			
		bannerId = new TextField();
			bannerId.setMinWidth(150);
			bannerId.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(bannerId, 1, 1);
		
		
		HBox orCont = new HBox(10);
			orCont.setAlignment(Pos.CENTER);
			
		Text orHeader = new Text("---------- OR SEARCH BY ----------");
			orHeader.setFill(Color.GOLD);
			orHeader.setFont(myFont);
			orHeader.setTextAlignment(TextAlignment.RIGHT);
		orCont.getChildren().add(orHeader);
		
		
		GridPane grid2 = new GridPane();
			grid2.setHgap(15);
			grid2.setVgap(15);
			grid2.setPadding(new Insets(10, 20, 30, 20));
			grid2.setAlignment(Pos.CENTER);
		
		
		Text firstNameHeader = new Text("First Name :"); 
			firstNameHeader.setFill(Color.GOLD);
			firstNameHeader.setFont(myFont);
			firstNameHeader.setTextAlignment(TextAlignment.RIGHT);
		grid2.add(firstNameHeader, 0, 1);
		
		firstName = new TextField();
			firstName.setMinWidth(150);
			firstName.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid2.add(firstName, 1, 1);

		Text lastNameHeader = new Text("Last Name :"); 
			lastNameHeader.setFill(Color.GOLD);
			lastNameHeader.setFont(myFont);
			lastNameHeader.setTextAlignment(TextAlignment.RIGHT);
		grid2.add(lastNameHeader, 0, 2);
		
		lastName = new TextField();
			lastName.setMinWidth(150);
			lastName.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid2.add(lastName, 1, 2);
		
		//---------------------------------------------------------------------------------

		
		doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
            doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
		});
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
		});
		
		ImageView icon = new ImageView(new Image("/images/searchcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		submitButton = new Button("Search", icon);
			submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			submitButton.setOnAction((ActionEvent e) -> {
				sendToController();
			});
			submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				submitButton.setEffect(new DropShadow());
			});
			submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				submitButton.setEffect(null);
			});
		doneCont.getChildren().add(submitButton);
		
		icon = new ImageView(new Image("/images/return.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		cancelButton = new Button("Return", icon);
			cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			cancelButton.setOnAction((ActionEvent e) -> {
				clearErrorMessage();
				myController.stateChangeRequest("CancelTransaction", null);
			});
			cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				cancelButton.setEffect(new DropShadow());
			});
			cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				cancelButton.setEffect(null);
			});
		doneCont.getChildren().add(cancelButton);
		
		vbox.getChildren().add(grid);
		vbox.getChildren().add(orCont);
		vbox.getChildren().add(grid2);
		vbox.getChildren().add(doneCont);
	
		setOutlines();
               
		return vbox;
	}

	private void sendToController()
	{
		clearErrorMessage();
		
		String BannerId = bannerId.getText();
		String FirstName = firstName.getText();
		String LastName = lastName.getText();
		Properties props = new Properties();
		
		if(!BannerId.equals(""))
		{
			if(Utilities.checkBannerId(BannerId))
			{
				props.setProperty("BannerId", BannerId);
				myController.stateChangeRequest("SearchWorker", props);			
			}
			else
			{
				displayErrorMessage("Banner Id: " + bannerId.getText() + " not valid.");
				bannerId.requestFocus();
			}
			
		}
		else if(!FirstName.equals("") || !LastName.equals("")) //if search by first or last name
		{
			if(!FirstName.equals("") && !LastName.equals(""))
			{
				if(Utilities.checkName(FirstName))
				{
					if(Utilities.checkName(LastName))
					{
						props.setProperty("FirstName", FirstName);
						props.setProperty("LastName", LastName);
						myController.stateChangeRequest("SearchWorker", props);
					}
					else
					{
						displayErrorMessage("Please enter a valid last name.");
						lastName.requestFocus();
					}					
				}
				else
				{
					displayErrorMessage("Please enter a valid first name.");
					firstName.requestFocus();
				}
			}
			else if(!FirstName.equals(""))
			{
				if(Utilities.checkName(FirstName))
				{
					props.setProperty("FirstName", FirstName);
					myController.stateChangeRequest("SearchWorker", props);
				}
				else
				{
					displayErrorMessage("Please enter a valid first name.");
					firstName.requestFocus();
				}
			}
			else
			{
				if(Utilities.checkName(LastName))
				{
					props.setProperty("LastName", LastName);
					myController.stateChangeRequest("SearchWorker", props);
				}
				else
				{
					displayErrorMessage("Please enter a valid last name.");
					lastName.requestFocus();
				}	
			}
		}
		else
		{
			myController.stateChangeRequest("SearchWorker", props);
		}
	}
	
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	public void clearValues()
	{
		bannerId.clear();
		firstName.clear();
		lastName.clear();
	}

	private void setOutlines()
	{
		bannerId.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		firstName.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		lastName.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	
	public void update(Observable o, Object value)
	{
		clearErrorMessage();

		String val = (String)value;
		if (val.startsWith("ERR") == true)
		{
			displayErrorMessage(val);
		}
		else
		{
			clearValues();
			displayMessage(val);
		}
		
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}
