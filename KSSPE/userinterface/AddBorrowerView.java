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

/** The class containing the Add Borrower View for the KSSPE
 *  application 
 */
//==============================================================
public class AddBorrowerView extends View implements Observer
{

	// GUI components
	protected TextField bannerId;
	protected TextField firstName;
	protected TextField lastName;
	protected TextField email;
	protected TextField phoneNumber;
	protected TextField notes;
	
	protected GridPane grid;
	protected Font myFont;

	protected Text actionText;
	protected Text prompt;

	protected HBox bannerBox;
	protected HBox notesBox;
	
	protected HBox doneCont;
	protected Button submitButton;
	protected Button cancelButton;

	protected MessageView statusLog;

	// constructor for this class -- takes a controller object
	//----------------------------------------------------------
	public AddBorrowerView(Transaction t)
	{
		super(t);

		VBox container = new VBox(10);
		container.setStyle("-fx-background-color: slategrey");
		container.setPadding(new Insets(15, 5, 5, 5));

		container.getChildren().add(createTitle());

		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);
		
		populateFields();

		myController.addObserver(this);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** ADD NEW BORROWER **";
	}
	
	public void populateFields()
	{
		
	}

	protected void processBannerId(String BannerId)
	{
		
		Properties props = new Properties();
		props.setProperty("BannerId", BannerId);
		
		myController.stateChangeRequest("removePersonData", props); //cleans out the past person.
		
		myController.stateChangeRequest("getPersonData", props);
		
		if((Boolean)myController.getState("TestBorrower"))
		{
			clearValues();
			setDisables();
		}
		else //if the borrower doesn't exist, continue on testing if it can autofill or not. 
		{
			removeDisables();
			
			String firstNameState = (String)myController.getState("FirstName"); 
			String lastNameState = (String)myController.getState("LastName");
			String emailState = (String)myController.getState("Email");
			String phoneState = (String)myController.getState("PhoneNumber");
			
			if(firstNameState != null) //checks if the person exists or not. 
			{
				firstName.setText(firstNameState);
				lastName.setText(lastNameState);
				email.setText(emailState);
				phoneNumber.setText(phoneState);
				
				firstName.setDisable(true);
				lastName.setDisable(true);
				email.setDisable(true);
				phoneNumber.setDisable(true);
				
				notes.requestFocus();
			}
			else
			{
				firstName.requestFocus();
			}
			
			bannerId.setText(BannerId);

		}
		
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
		
		myFont = Font.font("Copperplate", FontWeight.THIN, 16);
		Font bannerFont = Font.font("copperplate", FontWeight.BOLD, 18);   

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		vbox.getChildren().add(blankText);

		
		bannerBox = new HBox(10);
		bannerBox.setAlignment(Pos.CENTER);
		bannerBox.setPadding(new Insets(0, 20, 10, 0));
		
		Text bannerIdLabel = new Text("Banner ID :");
			bannerIdLabel.setFill(Color.GOLD);
			bannerIdLabel.setFont(bannerFont);
			bannerIdLabel.setUnderline(true);
			bannerIdLabel.setTextAlignment(TextAlignment.RIGHT);
		bannerBox.getChildren().add(bannerIdLabel);

		bannerId = new TextField();
			bannerId.setMinWidth(150);
			bannerId.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
				setDisables();
				
				if(Utilities.checkBannerId(bannerId.getText()))
				{
					removeDisables();
					processBannerId(bannerId.getText());
				}
				else
				{
					clearValuesExceptBanner();
				}
			});
		bannerBox.getChildren().add(bannerId);
		
		//---------------------------------------------------------------- BannerBox done
		grid = new GridPane();
			grid.setHgap(15);
			grid.setVgap(15);
			grid.setPadding(new Insets(0, 20, 20, 15));
			grid.setAlignment(Pos.CENTER);

		
		Text firstNameLabel = new Text(" First Name : ");
			firstNameLabel.setFill(Color.GOLD);
			firstNameLabel.setFont(myFont);
			firstNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(firstNameLabel, 0, 1);
		
		firstName = new TextField();
			firstName.setMinWidth(150);
			firstName.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(firstName, 1, 1);


		Text lastNameLabel = new Text(" Last Name : ");
			lastNameLabel.setFill(Color.GOLD);
			lastNameLabel.setFont(myFont);
			lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(lastNameLabel, 0, 2);

		lastName = new TextField();
			lastName.setMinWidth(150);
			lastName.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(lastName, 1, 2);
		
		Text emailLabel = new Text(" Email : ");
			emailLabel.setFill(Color.GOLD);
			emailLabel.setFont(myFont);
			emailLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(emailLabel, 2, 1);

		email = new TextField();
			email.setMinWidth(150);
			email.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(email, 3, 1);

					
		Text phoneNumberLabel = new Text(" Phone Number : ");
			phoneNumberLabel.setFill(Color.GOLD);
			phoneNumberLabel.setFont(myFont);
			phoneNumberLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(phoneNumberLabel, 2, 2);

		phoneNumber = new TextField();
			phoneNumber.setMinWidth(150);
			phoneNumber.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(phoneNumber, 3, 2);
		
		
		//-------------------------------------------  grid done
		
		notesBox = new HBox(10);
		notesBox.setAlignment(Pos.CENTER);
		notesBox.setPadding(new Insets(0, 20, 20, 0));
		
		Text notesLabel = new Text("Notes :");
			notesLabel.setFill(Color.GOLD);
			notesLabel.setFont(myFont);
			notesLabel.setTextAlignment(TextAlignment.RIGHT);
		notesBox.getChildren().add(notesLabel);

		notes = new TextField();
			notes.setMinWidth(150);
			notes.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				if(!notes.getText().equals(""))
					clearErrorMessage();
			});
		notesBox.getChildren().add(notes);

		//------------------------------------------------------------  notes box done
		
		doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
            doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
		});
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
		});
		
		ImageView icon = new ImageView(new Image("/images/pluscolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		submitButton = new Button("Add", icon);
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
		
		vbox.getChildren().add(bannerBox);
		vbox.getChildren().add(grid);
		vbox.getChildren().add(notesBox);
		vbox.getChildren().add(doneCont);
	
		setOutlines();
		
		setDisables(); //disable everything until the bannerId is entered.
               
		return vbox;
	}

	//---------------------------------------------------------------------------------------------------
	protected void sendToController()
	{
		clearErrorMessage();
		
		String BannerID = bannerId.getText();
		String FirstName = firstName.getText();
		String LastName = lastName.getText();
		String Email = email.getText();
		String PhoneNumber = phoneNumber.getText();
		String Notes = notes.getText();
		
		// VALIDATE the data before passing it to controller - code below does that
		if(Utilities.checkBannerId(BannerID)) 
		{
			if(Utilities.checkName(FirstName))
			{
				if(Utilities.checkName(LastName))
				{
					if(Utilities.checkEmail(Email))
					{
						if(Utilities.checkPhone(PhoneNumber))
						{
							
							Properties props = new Properties();
							props.setProperty("BannerId", BannerID);
							props.setProperty("FirstName", FirstName);
							props.setProperty("LastName", LastName);
							props.setProperty("Email", Email);
							props.setProperty("PhoneNumber", Utilities.formatUSPhoneNumber(PhoneNumber));
							props.setProperty("Notes", Notes);
							removeDisables();
							myController.stateChangeRequest("WorkerData", props);
							setDisables();
										
						}
						else
						{
							displayErrorMessage("Please enter a valid phone number.");
							phoneNumber.requestFocus();
						}
					}
					else
					{
						displayErrorMessage("Please enter a valid email.");
						email.requestFocus();
					}
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
		else
		{
			displayErrorMessage("Please enter a valid Banner Id.");
			bannerId.requestFocus();
		}
		
	}
	
	
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void clearValues()
	{
		bannerId.clear();
		firstName.clear();
		lastName.clear();
		email.clear();
		phoneNumber.clear();
		notes.clear();
	}
	
	//----------------------------------------------------------------------------------------
	private void clearValuesExceptBanner()
	{
		firstName.clear();
		lastName.clear();
		email.clear();
		phoneNumber.clear();
		notes.clear();
	}
	
	//------------------------------------------------------------------------------------------
	private void removeDisables()
	{
		firstName.setDisable(false);
		lastName.setDisable(false);
		email.setDisable(false);
		phoneNumber.setDisable(false);
		notes.setDisable(false);
	}
	
	//------------------------------------------------------------------------------------------
	protected void setDisables()
	{
		firstName.setDisable(true);
		lastName.setDisable(true);
		email.setDisable(true);
		phoneNumber.setDisable(true);
		notes.setDisable(true);
	}

	//------------------------------------------------------------------------------------------
	private void setOutlines()
	{
		bannerId.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		firstName.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		lastName.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		email.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		phoneNumber.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		notes.setStyle("-fx-border-color: transparent;  -fx-focus-color: darkgreen;");
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

//---------------------------------------------------------------
//	Revision History:
//
