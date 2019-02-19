// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Observer;
import java.util.Observable;

// project imports
import controller.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Vector;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import utilities.GlobalVariables;

/** The class containing the Transaction Choice View  for the ATM application */
//==============================================================
public class ReceptionistView extends View implements Observer
{

	// other private data
	private final int labelWidth = 120;
	private final int labelHeight = 25;

	// GUI components
	private Button addWorkerButton;
	private Button updateWorkerButton;
	private Button removeArticleTypeButton;
	private Button addColorButton;
	private Button updateColorButton;
	private Button removeColorButton;
	private Button addClothingItemButton;
	private Button updateClothingItemButton;
	private Button removeClothingItemButton;
	private Button logRequestButton;
	private Button fulfillRequestButton;
	private Button removeRequestButton;

	private Button checkoutClothingItemButton;
	private MenuButton reportsButton;

	private Button cancelButton;

	private MessageView statusLog;
	private DropShadow shadow = new DropShadow();

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public ReceptionistView(Transaction t)
	{
		super(t);

		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 15, 5, 15));
        container.setStyle("-fx-background-color: slategrey");

		container.getChildren().add(createTitle());

		container.getChildren().add(createFormContents());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);
		
		myController.addObserver(this);
	}

	// Create the labels and fields
	//-------------------------------------------------------------
	private VBox createTitle()
	{
		VBox container = new VBox(10);

		Text clientText = new Text("KSSPE DEPARTMENT");
		clientText.setFont(Font.font("Copperplate", FontWeight.EXTRA_BOLD, 36));
        clientText.setEffect(shadow);
		clientText.setTextAlignment(TextAlignment.CENTER);
		clientText.setFill(Color.WHITESMOKE);
		container.getChildren().add(clientText);

		Text titleText = new Text(" Reservation Management System ");
		titleText.setFont(Font.font("Copperplate", FontWeight.THIN, 28));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		container.getChildren().add(titleText);

		//bport icon
		ImageView bportIcon = new ImageView(new Image("/images/BPT_LOGO_All-In-One_Color.png",225,225 ,true,true));
		bportIcon.setEffect(new DropShadow());
		container.getChildren().add(bportIcon);

		container.setAlignment(Pos.CENTER);
                
		return container;
	}


	// Create the navigation buttons
	//-------------------------------------------------------------
	private VBox createFormContents()
	{

		VBox container = new VBox(15);
		HBox checkoutCont = new HBox(10);
			checkoutCont.setAlignment(Pos.CENTER);
		
		ImageView icon = new ImageView(new Image("/images/buyingcolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
			
		checkoutClothingItemButton = new Button("Reserve Items", icon);
			checkoutClothingItemButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			checkoutClothingItemButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("CheckoutClothingItem", null);
			});
			checkoutClothingItemButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				checkoutClothingItemButton.setEffect(shadow);
                statusLog.displayInfoMessage("Check Out a Clothing Item to a Student");
			});
			checkoutClothingItemButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				checkoutClothingItemButton.setEffect(null);
                clearErrorMessage();
			});
			
		checkoutCont.getChildren().add(checkoutClothingItemButton);
   
		icon = new ImageView(new Image("/images/listcolor.png"));
			icon.setFitHeight(20);
			icon.setFitWidth(20);
		MenuItem availInventory = new MenuItem("Available Inventory", icon);
		
		icon = new ImageView(new Image("/images/datecolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
		MenuItem itemCheckedOutTillDate = new MenuItem("Checked Out Items", icon);
		
		icon = new ImageView(new Image("/images/medalcolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
		MenuItem topDonators = new MenuItem("Top Donor", icon);

        icon = new ImageView(new Image("/images/reportcolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
        MenuButton reportsButton = new MenuButton("   Reports   ", icon, availInventory, itemCheckedOutTillDate, topDonators);
			reportsButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
            reportsButton.setStyle("-fx-selection-bar:gold");
			availInventory.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("ListAvailableInventory", null);
			});
			topDonators.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("TopDonatorReport", null);
			});
			reportsButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				reportsButton.setEffect(shadow);
                statusLog.displayInfoMessage("Displays List of Reports to Choose From");
			});
			reportsButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			reportsButton.setEffect(null);
                        clearErrorMessage();
			});
        checkoutCont.getChildren().add(reportsButton);
		
		container.getChildren().add(checkoutCont);
		//--------------------------------------------- Top Hbox done

		HBox workerBox = new HBox(10);
		    workerBox.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				workerBox.setStyle("-fx-background-color: DARKGREEN");
			});
            workerBox.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				workerBox.setStyle("-fx-background-color: SLATEGREY");
			});
		workerBox.setAlignment(Pos.CENTER);
		
		
		Label atLabel = new Label("WORKERS              : ");
            atLabel.setAlignment(Pos.CENTER_LEFT);
			atLabel.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
			atLabel.setTextFill(Color.GOLD);
		workerBox.getChildren().add(atLabel);
		
		icon = new ImageView(new Image("/images/pluscolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		addWorkerButton = new Button("Add",icon);
			addWorkerButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			addWorkerButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("AddWorker", null);
			});
			addWorkerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			addWorkerButton.setEffect(shadow);
                statusLog.displayInfoMessage("Add New Workers");    
			});
			addWorkerButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			addWorkerButton.setEffect(null);
                clearErrorMessage();
			});
		workerBox.getChildren().add(addWorkerButton); // add worker

		icon = new ImageView(new Image("/images/editcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		updateWorkerButton = new Button("Update", icon);
			updateWorkerButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			updateWorkerButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("SearchBanner", null);
			});
			updateWorkerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				updateWorkerButton.setEffect(shadow);
                statusLog.displayInfoMessage("Update Worker Information");
			});
			updateWorkerButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				updateWorkerButton.setEffect(null);
                clearErrorMessage();
			});
		workerBox.getChildren().add(updateWorkerButton); // update worker
		
		icon = new ImageView(new Image("/images/trashcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		removeArticleTypeButton = new Button("Remove", icon);
			removeArticleTypeButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			removeArticleTypeButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("RemoveArticleType", null);
			});
			removeArticleTypeButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				removeArticleTypeButton.setEffect(shadow);
                statusLog.displayInfoMessage("Remove Article Types from the records");
			});
			removeArticleTypeButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				removeArticleTypeButton.setEffect(null);
                clearErrorMessage();
			});
		workerBox.getChildren().add(removeArticleTypeButton); //remove worker

		container.getChildren().add(workerBox);

		//--------- Worker HBOX Done
		
		HBox colorCont = new HBox(10);
			colorCont.setAlignment(Pos.CENTER);
			colorCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				colorCont.setStyle("-fx-background-color: DARKGREEN");
			});
			colorCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				colorCont.setStyle("-fx-background-color: SLATEGREY");
			});
		
		Label colorLabel = new Label("BORROWERS         : ");    
			colorLabel.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
			colorLabel.setTextFill(Color.GOLD);
		colorCont.getChildren().add(colorLabel);
		
		icon = new ImageView(new Image("/images/pluscolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		addColorButton = new Button("Add", icon);
			addColorButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			addColorButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("AddColor", null);
			});
			addColorButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				addColorButton.setEffect(shadow);
                statusLog.displayInfoMessage("Add new Colors to the records");
			});
			addColorButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				addColorButton.setEffect(null);
                clearErrorMessage();
			});
		colorCont.getChildren().add(addColorButton);
		
		icon = new ImageView(new Image("/images/editcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		updateColorButton = new Button("Update", icon);
			updateColorButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			updateColorButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("UpdateColor", null);
			});
			updateColorButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				updateColorButton.setEffect(shadow);
                statusLog.displayInfoMessage("Update Colors in the records");
			});
			updateColorButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				updateColorButton.setEffect(null);
                clearErrorMessage();
			});
		colorCont.getChildren().add(updateColorButton);
		
		icon = new ImageView(new Image("/images/trashcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		removeColorButton = new Button("Remove",icon);
			removeColorButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			removeColorButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("RemoveColor", null);
			});
			removeColorButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				removeColorButton.setEffect(shadow);
                 statusLog.displayInfoMessage("Remove Colors from the records");
			});
			removeColorButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				removeColorButton.setEffect(null);
                 clearErrorMessage();
			});
		colorCont.getChildren().add(removeColorButton);
		container.getChildren().add(colorCont);
		
		//------------- Borrower Hbox Done

		HBox clothingItemCont = new HBox(10);
		clothingItemCont.setAlignment(Pos.CENTER_LEFT);
		Label ciLabel = new Label("CLOTHING ITEMS : ");
                clothingItemCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                    clothingItemCont.setStyle("-fx-background-color: DARKGREEN");
		});
                clothingItemCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                    clothingItemCont.setStyle("-fx-background-color: SLATEGREY");
		});
		ciLabel.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
                ciLabel.setTextFill(Color.GOLD);
		clothingItemCont.getChildren().add(ciLabel);
		icon = new ImageView(new Image("/images/pluscolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		addClothingItemButton = new Button("Add", icon);
		addClothingItemButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addClothingItemButton.setOnAction((ActionEvent e) -> {
			myController.stateChangeRequest("AddClothingItem", null);
		});
		addClothingItemButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			addClothingItemButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Add new Clothing Items to the records");
		});
		addClothingItemButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			addClothingItemButton.setEffect(null);
                        clearErrorMessage();
		});
		clothingItemCont.getChildren().add(addClothingItemButton);
		icon = new ImageView(new Image("/images/editcolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		updateClothingItemButton = new Button("Update",icon);
		updateClothingItemButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateClothingItemButton.setOnAction((ActionEvent e) -> {
			myController.stateChangeRequest("UpdateClothingItem", null);
		});
		updateClothingItemButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			updateClothingItemButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Update Clothing Items in the records");
		});
		updateClothingItemButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			updateClothingItemButton.setEffect(null);
                        clearErrorMessage();
		});
		clothingItemCont.getChildren().add(updateClothingItemButton);
		icon = new ImageView(new Image("/images/trashcolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		removeClothingItemButton = new Button("Remove", icon);
		removeClothingItemButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		removeClothingItemButton.setOnAction((ActionEvent e) -> {
			myController.stateChangeRequest("RemoveClothingItem", null);
		});
		removeClothingItemButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			removeClothingItemButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Remove Clothing Items from the records");
		});
		removeClothingItemButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			removeClothingItemButton.setEffect(null);
                        clearErrorMessage();
		});
		clothingItemCont.getChildren().add(removeClothingItemButton);
                clothingItemCont.setAlignment(Pos.CENTER);
		container.getChildren().add(clothingItemCont);

		HBox requestCont = new HBox(10);
		requestCont.setAlignment(Pos.CENTER_LEFT);
		Label reqLabel = new Label("REQUESTS             : ");
                requestCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                    requestCont.setStyle("-fx-background-color: DARKGREEN");
		});
                requestCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                    requestCont.setStyle("-fx-background-color: SLATEGREY");
		});
		reqLabel.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
                reqLabel.setTextFill(Color.GOLD);
		requestCont.getChildren().add(reqLabel);
		icon = new ImageView(new Image("/images/logcolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		logRequestButton = new Button("Log", icon);
		logRequestButton.setMinWidth(65);
		logRequestButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		logRequestButton.setOnAction((ActionEvent e) -> {
			myController.stateChangeRequest("AddRequest", null);
		});
		logRequestButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			logRequestButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Log new Requests to the records");
		});
		logRequestButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			logRequestButton.setEffect(null);
                        clearErrorMessage();
		});
		requestCont.getChildren().add(logRequestButton);
		icon = new ImageView(new Image("/images/entercolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		fulfillRequestButton = new Button(" Fulfill ", icon);
		fulfillRequestButton.setMinWidth(85);
		fulfillRequestButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		fulfillRequestButton.setOnAction((ActionEvent e) -> {
			myController.stateChangeRequest("FulfillRequest", null);
		});
		fulfillRequestButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			fulfillRequestButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Fulfill Requests in the records");
		});
		fulfillRequestButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			fulfillRequestButton.setEffect(null);
                        clearErrorMessage();
		});
		requestCont.getChildren().add(fulfillRequestButton);
		icon = new ImageView(new Image("/images/trashcolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		removeRequestButton = new Button("Remove",icon);
		removeRequestButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		removeRequestButton.setOnAction((ActionEvent e) -> {
			myController.stateChangeRequest("RemoveRequest", null);
		});
		removeRequestButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			removeRequestButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Remove Requests from the records");
		});
		removeRequestButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			removeRequestButton.setEffect(null);
                        clearErrorMessage();
		});
		requestCont.getChildren().add(removeRequestButton);
                requestCont.setAlignment(Pos.CENTER);

		container.getChildren().add(requestCont);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		icon = new ImageView(new Image("/images/exitcolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		cancelButton = new Button("Logout",icon);
		cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myController.stateChangeRequest("Logout", null);    
            	     }
        	});
		cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			cancelButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Go to Login Screen");
		});
		cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			cancelButton.setEffect(null);
                        clearErrorMessage();
		});
		doneCont.getChildren().add(cancelButton);
                doneCont.setAlignment(Pos.CENTER);

		container.getChildren().add(doneCont);

		return container;
	}

	// Create the status log field
	//-------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}
        

	
	public void update(Observable o, Object value)
	{
		clearErrorMessage();
		
		displayErrorMessage((String)value);
	}

	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}


