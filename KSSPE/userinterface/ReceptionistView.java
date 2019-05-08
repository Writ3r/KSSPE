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
	private Button removeWorkerButton;
	
	private Button addBorrowerButton;
	private Button updateBorrowerButton;
	private Button removeBorrowerButton;
	
	private Button addCategoryButton;
	private Button updateCategoryButton;
	private Button removeCategoryButton;
	
	private Button addEquipmentButton;
	private Button updateEquipmentButton;
	private Button removeEquipmentButton;
	
	private Button licenseButton;

	private Button reserveButton;
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
		
		populateFields();
		
		myController.addObserver(this);
	}
	
	//-------------------------------------------------------------
	public void populateFields()
	{
		if(((String)myController.getState("Credential")).equals("Ordinary"))
		{
			addWorkerButton.setDisable(true);
			updateWorkerButton.setDisable(true);
			removeWorkerButton.setDisable(true);
			addCategoryButton.setDisable(true);
			updateCategoryButton.setDisable(true);
			removeCategoryButton.setDisable(true);
		}
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
		
		Text workerText = new Text("Worker: " + ((String)myController.getState("FirstName") + " " +
			(String)myController.getState("LastName")) + " / Cred: " + ((String)myController.getState("Credential")));
		workerText.setFont(Font.font("Copperplate", FontWeight.BOLD, 23));
		workerText.setTextAlignment(TextAlignment.CENTER);
		workerText.setFill(Color.DARKGREEN);
		container.getChildren().add(workerText);

		//bport icon
		ImageView bportIcon = new ImageView(new Image("/images/bportB.png",225,225 ,true,true));
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
		
		
		ImageView icon = new ImageView(new Image("/images/datecolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
		MenuItem reserveItems = new MenuItem("Reserve Equipment", icon);
		
		icon = new ImageView(new Image("/images/entercolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
		MenuItem returnItems = new MenuItem("Return Equipment", icon);
		
		icon = new ImageView(new Image("/images/buyingcolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
        MenuButton manageButton = new MenuButton("Reservation", icon, reserveItems, returnItems);
			manageButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
            manageButton.setStyle("-fx-selection-bar:gold");
			reserveItems.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("ReserveEquipment", null);
			});
			returnItems.setOnAction((ActionEvent e) -> {
				// DEBUG System.out.println("Returning equipment");
				myController.stateChangeRequest("ReturnEquipment", null);
			});
			manageButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				manageButton.setEffect(shadow);
                statusLog.displayInfoMessage("Reserve or Return Equipment");
			});
			manageButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				manageButton.setEffect(null);
                clearErrorMessage();
			});
        checkoutCont.getChildren().add(manageButton);
		
   
		icon = new ImageView(new Image("/images/list.png"));
			icon.setFitHeight(20);
			icon.setFitWidth(20);
		MenuItem allInventory = new MenuItem("All Inventory", icon);
		
		icon = new ImageView(new Image("/images/listcolor.png"));
			icon.setFitHeight(20);
			icon.setFitWidth(20);
		MenuItem availInventory = new MenuItem("Available Inventory", icon);
		
		icon = new ImageView(new Image("/images/checkout.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
		MenuItem currentlyReserved = new MenuItem("Currently Reserved", icon);
		
		icon = new ImageView(new Image("/images/datecolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
		MenuItem overdueItems = new MenuItem("Overdue Items", icon);
	

        icon = new ImageView(new Image("/images/reportcolor.png"));
			icon.setFitHeight(25);
			icon.setFitWidth(25);
        MenuButton reportsButton = new MenuButton("   Reports   ", icon, allInventory, availInventory, currentlyReserved, overdueItems);
			reportsButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
            reportsButton.setStyle("-fx-selection-bar:gold");
			allInventory.setOnAction((ActionEvent e) -> {
				// DEBUG System.out.println("All Inventory Requested");
				myController.stateChangeRequest("ListAllInventory", null);
			});
			availInventory.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("ListAvailableInventory", null);
			});
			currentlyReserved.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("ListReservedInventory", null);
			});
			overdueItems.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("ListOverdueInventory", null);
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
				myController.stateChangeRequest("ModifyWorker", null);
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
			
		removeWorkerButton = new Button("Remove", icon);
			removeWorkerButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			removeWorkerButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("RemoveWorker", null);
			});
			removeWorkerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				removeWorkerButton.setEffect(shadow);
                statusLog.displayInfoMessage("Remove Worker from the records");
			});
			removeWorkerButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				removeWorkerButton.setEffect(null);
                clearErrorMessage();
			});
		workerBox.getChildren().add(removeWorkerButton); //remove worker

		container.getChildren().add(workerBox);

		//--------- Worker HBOX Done
		
		HBox borrowerCont = new HBox(10);
			borrowerCont.setAlignment(Pos.CENTER);
			borrowerCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				borrowerCont.setStyle("-fx-background-color: DARKGREEN");
			});
			borrowerCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				borrowerCont.setStyle("-fx-background-color: SLATEGREY");
			});
		
		Label colorLabel = new Label("BORROWERS         : ");    
			colorLabel.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
			colorLabel.setTextFill(Color.GOLD);
		borrowerCont.getChildren().add(colorLabel);
		
		icon = new ImageView(new Image("/images/pluscolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		addBorrowerButton = new Button("Add", icon);
			addBorrowerButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			addBorrowerButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("AddBorrower", null);
			});
			addBorrowerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				addBorrowerButton.setEffect(shadow);
                statusLog.displayInfoMessage("Add new Borrower to the records");
			});
			addBorrowerButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				addBorrowerButton.setEffect(null);
                clearErrorMessage();
			});
		borrowerCont.getChildren().add(addBorrowerButton);
		
		icon = new ImageView(new Image("/images/editcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		updateBorrowerButton = new Button("Update", icon);
			updateBorrowerButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			updateBorrowerButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("ModifyBorrower", null);
			});
			updateBorrowerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				updateBorrowerButton.setEffect(shadow);
                statusLog.displayInfoMessage("Update Borrower in the records");
			});
			updateBorrowerButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				updateBorrowerButton.setEffect(null);
                clearErrorMessage();
			});
		borrowerCont.getChildren().add(updateBorrowerButton);
		
		icon = new ImageView(new Image("/images/trashcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		removeBorrowerButton = new Button("Remove",icon);
			removeBorrowerButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			removeBorrowerButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("RemoveBorrower", null);
			});
			removeBorrowerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				removeBorrowerButton.setEffect(shadow);
                 statusLog.displayInfoMessage("Remove Borrower from the records");
			});
			removeBorrowerButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				removeBorrowerButton.setEffect(null);
                 clearErrorMessage();
			});
		borrowerCont.getChildren().add(removeBorrowerButton);
		container.getChildren().add(borrowerCont);
		
		//------------- Borrower Hbox Done

		HBox catCont = new HBox(10);
		catCont.setAlignment(Pos.CENTER_LEFT);
			catCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				catCont.setStyle("-fx-background-color: DARKGREEN");
			});
            catCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				catCont.setStyle("-fx-background-color: SLATEGREY");
			});  
		Label ciLabel = new Label   ("CATEGORIES          : ");
			ciLabel.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
            ciLabel.setTextFill(Color.GOLD);
		catCont.getChildren().add(ciLabel);
		
		icon = new ImageView(new Image("/images/pluscolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		addCategoryButton = new Button("Add", icon);
			addCategoryButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			addCategoryButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("AddCategory", null);
			});
			addCategoryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				addCategoryButton.setEffect(shadow);
                statusLog.displayInfoMessage("Add new Category to the records");
			});
			addCategoryButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				addCategoryButton.setEffect(null);
				clearErrorMessage();
			});
		catCont.getChildren().add(addCategoryButton);
		
		icon = new ImageView(new Image("/images/editcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		updateCategoryButton = new Button("Update",icon);
			updateCategoryButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			updateCategoryButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("ModifyCategory", null);
			});
			updateCategoryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				updateCategoryButton.setEffect(shadow);
                statusLog.displayInfoMessage("Update Category in the records");
			});
			updateCategoryButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				updateCategoryButton.setEffect(null);
                clearErrorMessage();
			});
		catCont.getChildren().add(updateCategoryButton);
		
		icon = new ImageView(new Image("/images/trashcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		removeCategoryButton = new Button("Remove", icon);
			removeCategoryButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			removeCategoryButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("RemoveCategory", null);
			});
			removeCategoryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				removeCategoryButton.setEffect(shadow);
                statusLog.displayInfoMessage("Remove Category from the records");
			});
			removeCategoryButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				removeCategoryButton.setEffect(null);
                clearErrorMessage();
			});
			catCont.getChildren().add(removeCategoryButton);
			
        catCont.setAlignment(Pos.CENTER);
		container.getChildren().add(catCont);
		
		//------------------------------------ category hbox done

		HBox equipCont = new HBox(10);
		equipCont.setAlignment(Pos.CENTER_LEFT);
            equipCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				equipCont.setStyle("-fx-background-color: DARKGREEN");
			});
            equipCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				equipCont.setStyle("-fx-background-color: SLATEGREY");
			});
		
		Label reqLabel = new Label("EQUIPMENT           : ");
			reqLabel.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
            reqLabel.setTextFill(Color.GOLD);
		equipCont.getChildren().add(reqLabel);
		
		icon = new ImageView(new Image("/images/pluscolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		addEquipmentButton = new Button("Add", icon);
			addEquipmentButton.setMinWidth(65);
			addEquipmentButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			addEquipmentButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("AddEquipment", null);
			});
			addEquipmentButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				addEquipmentButton.setEffect(shadow);
                statusLog.displayInfoMessage("Add new Equipment to the records");
			});
			addEquipmentButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				addEquipmentButton.setEffect(null);
                clearErrorMessage();
			});
		equipCont.getChildren().add(addEquipmentButton);
		
		icon = new ImageView(new Image("/images/editcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		updateEquipmentButton = new Button("Update", icon);
			updateEquipmentButton.setMinWidth(85);
			updateEquipmentButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			updateEquipmentButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("UpdateEquipment", null);
			});
			updateEquipmentButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			updateEquipmentButton.setEffect(shadow);
                statusLog.displayInfoMessage("Update Equipment in the records");
			});
			updateEquipmentButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				updateEquipmentButton.setEffect(null);
                clearErrorMessage();
			});
		equipCont.getChildren().add(updateEquipmentButton);
		
		icon = new ImageView(new Image("/images/trashcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		removeEquipmentButton = new Button("Remove",icon);
			removeEquipmentButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			removeEquipmentButton.setOnAction((ActionEvent e) -> {
				myController.stateChangeRequest("RemoveEquipment", null);
			});
			removeEquipmentButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				removeEquipmentButton.setEffect(shadow);
                statusLog.displayInfoMessage("Remove Equipment from the records");
			});
			removeEquipmentButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				removeEquipmentButton.setEffect(null);
                clearErrorMessage();
			});
		equipCont.getChildren().add(removeEquipmentButton);
		
        equipCont.setAlignment(Pos.CENTER);
		container.getChildren().add(equipCont);

		//------------------------------------------------- logout/licensebox start

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		
		icon = new ImageView(new Image("/images/log.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		licenseButton = new Button("License",icon);
		licenseButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		licenseButton.setOnAction((ActionEvent e) -> {
			Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Licensing");
				alert.setHeaderText("Copyright (c): Dream Team");
				alert.setContentText("Members: Lucas Wing, Nicholas Barnard, Matthew Fritschi, Liam Allport \n\nLicensed to: The College At Brockport \nFor: Commercial use, Non-commercial use, Modification.");
				alert.showAndWait();
		});
		licenseButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			licenseButton.setEffect(shadow);
                        statusLog.displayInfoMessage("License for software use");
		});
		licenseButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			licenseButton.setEffect(null);
                        clearErrorMessage();
		});
		doneCont.getChildren().add(licenseButton);
		
		
		icon = new ImageView(new Image("/images/exitcolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		cancelButton = new Button("Logout",icon);
		cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		cancelButton.setOnAction((ActionEvent e) -> {
			myController.stateChangeRequest("Logout", null);    
		});
		
		cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			cancelButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Return to Login Screen");
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
        

	//------------------------------------------------------------
	public void update(Observable o, Object value)
	{
		clearErrorMessage();
		
		displayErrorMessage((String)value);
	}

	//-------------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	//-------------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}


