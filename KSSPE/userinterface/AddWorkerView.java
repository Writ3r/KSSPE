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
import javafx.scene.control.PasswordField;

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

/** The class containing the Add clothing item View  for the Professional Clothes
 *  Closet application 
 */
//==============================================================
public class AddWorkerView extends View implements Observer
{

	// GUI components
	protected TextField bannerId;
	protected TextField firstName;
	protected TextField lastName;
	protected TextField email;
	protected TextField phoneNumber;
	protected ComboBox credential;
	protected PasswordField password;
	protected Text actionText;
	protected Text prompt;

	protected HBox bannerBox;
	protected HBox doneCont;
	protected Button submitButton;
	protected Button cancelButton;

	protected MessageView statusLog;

	// constructor for this class -- takes a controller object
	//----------------------------------------------------------
	public AddWorkerView(Transaction t)
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
		return "** ADD NEW WORKER **";
	}

	public void populateFields(){

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
		
		Font myFont = Font.font("Copperplate", FontWeight.THIN, 16);
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
				clearOutlines();
			});
		bannerBox.getChildren().add(bannerId);
		

		GridPane grid = new GridPane();
			grid.setHgap(15);
			grid.setVgap(15);
			grid.setPadding(new Insets(0, 20, 25, 15));
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
				clearOutlines();
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
				clearOutlines();
			});
		grid.add(lastName, 1, 2);
		
		Text passwordLabel = new Text(" Password : ");
			passwordLabel.setFill(Color.GOLD);
			passwordLabel.setFont(myFont);
			passwordLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(passwordLabel, 0, 3);

		password = new PasswordField();
			password.setMinWidth(150);
			password.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
				clearOutlines();
			});
		grid.add(password, 1, 3);

		
		Text emailLabel = new Text(" Email : ");
			emailLabel.setFill(Color.GOLD);
			emailLabel.setFont(myFont);
			emailLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(emailLabel, 2, 1);

		email = new TextField();
			email.setMinWidth(150);
			email.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
				clearOutlines();
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
				clearOutlines();
			});
		grid.add(phoneNumber, 3, 2);


		Text credentialLabel = new Text(" Credential : ");
		credentialLabel.setFill(Color.GOLD);
		credentialLabel.setFont(myFont);
		credentialLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(credentialLabel, 2, 3);
		
		
		credential = new ComboBox();
			credential.getItems().addAll(
				"Ordinary",
				"Admin"
			);
			credential.setPromptText("Choose Credential");
			credential.setMinWidth(150);
		grid.add(credential, 3, 3);

		
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
		vbox.getChildren().add(doneCont);
	
		clearOutlines();
               
		return vbox;
	}

	private void sendToController()
	{
		clearErrorMessage();
        clearOutlines();
		
		String BannerID = bannerId.getText();
		String FirstName = firstName.getText();
		String LastName = lastName.getText();
		String Email = email.getText();
		String PhoneNumber = phoneNumber.getText();
		String Password = password.getText();
		String Credential;
		
		if(BannerID.length() == GlobalVariables.BANNERID_LENGTH && BannerID.matches("\\d+")) 
		{
			if(FirstName.length() != 0 && FirstName.length() < GlobalVariables.NAME_LENGTH)
			{
				if(LastName.length() != 0 && LastName.length() < GlobalVariables.NAME_LENGTH)
				{
					if(Email.length() != 0 && Email.length() < GlobalVariables.EMAIL_LENGTH)
					{
						if(PhoneNumber.length() != 0 && PhoneNumber.length() < GlobalVariables.PHONE_LENGTH && Utilities.checkProperPhoneNumber(PhoneNumber))
						{
							if(Password.length() != 0 && Password.length() < GlobalVariables.PASSWORD_LENGTH)
							{
								if(credential.getValue() != null) //credential can't be null when sending it to controller. 
								{
									Credential = credential.getValue().toString();
							
									Properties props = new Properties();
									props.setProperty("BannerId", BannerID);
									props.setProperty("FirstName", FirstName);
									props.setProperty("LastName", LastName);
									props.setProperty("Email", Email);
									props.setProperty("PhoneNumber", PhoneNumber);
									props.setProperty("Credential", Credential);
									props.setProperty("Password", Password);
								
									myController.stateChangeRequest("WorkerData", props);
									
								}
								else
								{
									displayErrorMessage("Please enter a credential.");
									credential.requestFocus();
								}
							}
							else
							{
								displayErrorMessage("Please enter a valid password.");
								password.requestFocus();
							}
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

	public void clearValues()
	{

		bannerId.clear();
		firstName.clear();
		lastName.clear();
		email.clear();
		phoneNumber.clear();
		password.clear();
		credential.getSelectionModel().select(null);
		/*
		barcode.clear();
		gender.getSelectionModel().select(null);
		articleType.getSelectionModel().select(null);
		color1.getSelectionModel().select(null);
		color2.getSelectionModel().select(null);
		bannerId.clear();
		brand.clear();
		notes.clear();
		SaveDonorFName = donorFName.getText();
		SaveDonorLName = donorLName.getText();
		SaveDonorPhone = donorPhone.getText();
		SaveDonorEmail = donorEmail.getText();
		donorFName.clear();
		donorLName.clear();
		donorPhone.clear();
		donorEmail.clear();
		*/
	}

	private void clearOutlines()
	{
		/*
		barcode.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		gender.setStyle("-fx-background-color: white; -fx-selection-bar:green;");
		articleType.setStyle("-fx-background-color: white; -fx-selection-bar:green;");
		color1.setStyle("-fx-background-color: white; -fx-selection-bar:green;");
		color2.setStyle("-fx-background-color: white; -fx-selection-bar:green;");
		size.setStyle("-fx-border-color: transparent;  -fx-focus-color: darkgreen;");
		brand.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
		notes.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
		donorFName.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
		donorLName.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
		donorPhone.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
		donorEmail.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
		*/
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
