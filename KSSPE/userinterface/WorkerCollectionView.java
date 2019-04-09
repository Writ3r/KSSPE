package userinterface;

// system imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
import java.util.Vector;
import java.util.Enumeration;
import java.util.Observer;
import java.util.Observable;

// project imports
import impresario.IModel;
import controller.Transaction;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import model.Worker;
import model.WorkerCollection;

//==============================================================================
public class WorkerCollectionView extends View implements Observer
{
	protected TableView<WorkerTableModel> tableOfWorkers;
	protected Button cancelButton;
	protected Button submitButton;
	protected MessageView statusLog;
	protected Text actionText; 
        
	//--------------------------------------------------------------------------
	public WorkerCollectionView(Transaction t)
	{
		super(t);

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setStyle("-fx-background-color: slategrey");
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                                            "));

		getChildren().add(container);
		
		populateFields();

		myController.addObserver(this);
		
		tableOfWorkers.getSelectionModel().select(0); //autoselect first element
	}

	//--------------------------------------------------------------------------
	protected void populateFields()
	{
		getEntryTableModelValues();
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues()
	{

		ObservableList<WorkerTableModel> tableData = FXCollections.observableArrayList();
		try
		{
	
			WorkerCollection workerCollection = 
					(WorkerCollection)myController.getState("WorkerList");

			Vector entryList = (Vector)workerCollection.getState("Workers");
			// DEBUG System.out.println("Worker list size = " + entryList.size());

			if (entryList.size() > 0)
			{
				Enumeration entries = entryList.elements();

				while (entries.hasMoreElements() == true)
				{
					Worker nextW = (Worker)entries.nextElement();
					Vector<String> view = nextW.getEntryListView();

					// add this list entry to the list
					WorkerTableModel nextTableRowData = new WorkerTableModel(view);
					tableData.add(nextTableRowData);

				}
				if(entryList.size() == 1)
					actionText.setText(entryList.size()+" Worker Found!");
				else 
					actionText.setText(entryList.size()+" Workers Found!");

				actionText.setFill(Color.LIGHTGREEN);
			}
			else
			{
				actionText.setText("No Workers Found!");
				actionText.setFill(Color.FIREBRICK);
			}

			tableOfWorkers.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
			// DEBUG System.out.println("Exception " + e);
		}

	}

	// Create the title container
	//-------------------------------------------------------------
private Node createTitle()
	{
		VBox container = new VBox(10);
		container.setPadding(new Insets(1, 10, 12, 10));
		
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
	
	protected String getActionText()
	{
		return "** CHOOSE A WORKER **";
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		tableOfWorkers = new TableView<WorkerTableModel>();
		tableOfWorkers.setEffect(new DropShadow());
		tableOfWorkers.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
		tableOfWorkers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn bannerIdColumn = new TableColumn("Banner Id") ;
		bannerIdColumn.setMinWidth(116.6);
		bannerIdColumn.setStyle(" -fx-alignment: CENTER;");
		bannerIdColumn.setCellValueFactory(
				new PropertyValueFactory<WorkerTableModel, String>("BannerId"));


		TableColumn firstNameColumn = new TableColumn("First Name") ;
		firstNameColumn.setMinWidth(116.6);
		firstNameColumn.setStyle(" -fx-alignment: CENTER;");
		firstNameColumn.setCellValueFactory(
				new PropertyValueFactory<WorkerTableModel, String>("FirstName"));
		
		TableColumn lastNameColumn = new TableColumn("Last Name") ;
		lastNameColumn.setMinWidth(116.6);
		lastNameColumn.setStyle(" -fx-alignment: CENTER;");
		lastNameColumn.setCellValueFactory(
				new PropertyValueFactory<WorkerTableModel, String>("LastName"));

		tableOfWorkers.getColumns().addAll(bannerIdColumn, firstNameColumn, lastNameColumn);

		tableOfWorkers.setOnMousePressed((MouseEvent event) -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
				if(myController.getState("Alert") != null)
                                   displayRemoveAlert();
                                else
                                    processWorkerSelected();
			}
		});
		ImageView icon = new ImageView(new Image("/images/check.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		submitButton = new Button("Select",icon);
		submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		submitButton.requestFocus();
		submitButton.setOnAction((ActionEvent e) -> {
			clearErrorMessage();
			
			if(myController.getState("Alert") != null)
                             displayRemoveAlert();
                         else
                             processWorkerSelected();
		});
		submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			submitButton.setEffect(new DropShadow());
		});
		submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			submitButton.setEffect(null);
		});
		icon = new ImageView(new Image("/images/return.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		cancelButton = new Button("Return", icon);
		cancelButton.setGraphic(icon);
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
		HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.CENTER);
                btnContainer.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                    btnContainer.setStyle("-fx-background-color: GOLD");
		});
                btnContainer.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                    btnContainer.setStyle("-fx-background-color: SLATEGREY");
		});
		btnContainer.getChildren().add(submitButton);
		btnContainer.getChildren().add(cancelButton);

		tableOfWorkers.setPrefHeight(250);
        tableOfWorkers.setMaxWidth(350);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(350, 150);
		scrollPane.setContent(tableOfWorkers); // should we use this?
		
		vbox.getChildren().add(tableOfWorkers);
		vbox.getChildren().add(btnContainer);
		vbox.setPadding(new Insets(10,10,10,10));
                vbox.setAlignment(Pos.CENTER);

		return vbox;
	}

	//--------------------------------------------------------------------------
	protected void processWorkerSelected()
	{
		WorkerTableModel selectedItem = tableOfWorkers.getSelectionModel().getSelectedItem();

		if(selectedItem != null)
		{
			String selectedBannerId = selectedItem.getBannerId();

			myController.stateChangeRequest("WorkerSelected", selectedBannerId);
		}
	}
        
	private void displayRemoveAlert(){
		clearErrorMessage();
		Alert alert = new Alert(Alert.AlertType.ERROR,"BannerId: "+tableOfWorkers.getSelectionModel().getSelectedItem().getBannerId()
				+"\nFirstName: "+tableOfWorkers.getSelectionModel().getSelectedItem().getFirstName()
				+"\nLastName: "+tableOfWorkers.getSelectionModel().getSelectedItem().getLastName(), ButtonType.YES, ButtonType.NO);
		alert.setHeaderText(null);
		alert.setTitle("Remove Worker");
		alert.setHeaderText("Are you sure want to remove this Worker?");
		((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("images/BPT_LOGO_All-In-One_Color.png"));
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			processWorkerSelected();	
		}
	}
       
	//--------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}
	
	
	public void update(Observable o, Object value)
	{
		clearErrorMessage();

		String val = (String)value;
		if (val.startsWith("ERR") == true)
		{
			displayErrorMessage(val);
			getEntryTableModelValues();
		}
		else
		{
			displayMessage(val);
			getEntryTableModelValues();
		}
	}
	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
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
