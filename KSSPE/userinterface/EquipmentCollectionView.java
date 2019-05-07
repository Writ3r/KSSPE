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
import model.Equipment;
import model.EquipmentCollection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

//==============================================================================
public class EquipmentCollectionView extends View implements Observer
{
	protected TableView<EquipmentTableModel> tableOfEquipment;
	protected Button cancelButton;
	protected Button saveButton;
	protected MessageView statusLog;
	protected Text actionText; 
        
	//--------------------------------------------------------------------------
	public EquipmentCollectionView(Transaction t)
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
		
		tableOfEquipment.getSelectionModel().select(0); //autoselect first element
	}

	//--------------------------------------------------------------------------
	protected void populateFields()
	{
		getEntryTableModelValues();
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues()
	{
		ObservableList<EquipmentTableModel> tableData = FXCollections.observableArrayList();
		try
		{
			EquipmentCollection equipmentCollection = 
					(EquipmentCollection)myController.getState("EquipmentList");

			Vector entryList = (Vector)equipmentCollection.getState("Equipment");
			
			if (entryList.size() > 0)
			{
				Enumeration entries = entryList.elements();

				while (entries.hasMoreElements() == true)
				{
					Equipment nextE = (Equipment)entries.nextElement();

					Vector<String> view = nextE.getEntryListView();

					// add this list entry to the list
					EquipmentTableModel nextTableRowData = new EquipmentTableModel(view);
					tableData.add(nextTableRowData);

				}

				if(entryList.size() == 1)
					actionText.setText(entryList.size()+" Item Found!");
				else
					actionText.setText(entryList.size()+" Items Found!");
				actionText.setFill(Color.LIGHTGREEN);
			}
			else
			{
				actionText.setText("No Equipment Found!");
				actionText.setFill(Color.FIREBRICK);
			}

			tableOfEquipment.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
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
		return "** LIST OF EQUIPMENT **";
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		tableOfEquipment = new TableView<EquipmentTableModel>();
		tableOfEquipment.setEffect(new DropShadow());
		tableOfEquipment.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
		tableOfEquipment.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn barcodeColumn = new TableColumn("Barcode") ;
		barcodeColumn.setMinWidth(116.6);
		barcodeColumn.setStyle(" -fx-alignment: CENTER;");
		barcodeColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("Barcode"));


		TableColumn nameColumn = new TableColumn("Name") ;
		nameColumn.setMinWidth(116.6);
		nameColumn.setStyle(" -fx-alignment: CENTER;");
		nameColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("Name"));
		
		TableColumn categoryNameColumn = new TableColumn("Category") ;
		categoryNameColumn.setMinWidth(116.6);
		categoryNameColumn.setStyle(" -fx-alignment: CENTER;");
		categoryNameColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("CategoryName"));

		TableColumn notesColumn = new TableColumn("Notes") ;
		notesColumn.setMinWidth(116.6);
		notesColumn.setStyle(" -fx-alignment: CENTER;");
		notesColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("Notes"));

		TableColumn goodCountColumn = new TableColumn("Good Count") ;
		goodCountColumn.setMinWidth(116.6);
		goodCountColumn.setStyle(" -fx-alignment: CENTER;");
		goodCountColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("GoodCount"));

		TableColumn fairCountColumn = new TableColumn("Fair Count") ;
		fairCountColumn.setMinWidth(116.6);
		fairCountColumn.setStyle(" -fx-alignment: CENTER;");
		fairCountColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("FairCount"));

		TableColumn poorCountColumn = new TableColumn("Poor Count") ;
		poorCountColumn.setMinWidth(116.6);
		poorCountColumn.setStyle(" -fx-alignment: CENTER;");
		poorCountColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("PoorCount"));

		//NOTE: Took this out because it didn't make much sense to display to the user
		//		If you want it back, make sure you add the column back in line 260
		// TableColumn availableCountColumn = new TableColumn("Available Count") ;
		// availableCountColumn.setMinWidth(116.6);
		// availableCountColumn.setStyle(" -fx-alignment: CENTER;");
		// availableCountColumn.setCellValueFactory(
		// 		new PropertyValueFactory<EquipmentTableModel, String>("AvailableCount"));

		TableColumn stockCountColumn = new TableColumn("Currently in Stock") ;
		stockCountColumn.setMinWidth(116.6);
		stockCountColumn.setStyle(" -fx-alignment: CENTER;");
		stockCountColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("InStockCount"));

		TableColumn lastUsedColumn = new TableColumn("Date Last Used") ;
		lastUsedColumn.setMinWidth(116.6);
		lastUsedColumn.setStyle(" -fx-alignment: CENTER;");
		lastUsedColumn.setCellValueFactory(
				new PropertyValueFactory<EquipmentTableModel, String>("DateLastUsed"));

		tableOfEquipment.getColumns().addAll(barcodeColumn, nameColumn, categoryNameColumn, notesColumn, 
			goodCountColumn, fairCountColumn, poorCountColumn, stockCountColumn, lastUsedColumn);

		ImageView icon = new ImageView(new Image("/images/return.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		cancelButton = new Button("Return", icon);
		cancelButton.setGraphic(icon);
		cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		cancelButton.setOnAction((ActionEvent e) -> {
			clearErrorMessage();
			myController.stateChangeRequest("CancelEquipmentList", null);
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

        icon = new ImageView(new Image("/images/savecolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		saveButton = new Button("Save to File", icon);
		saveButton.setGraphic(icon);
                saveButton.setPadding(new Insets(5,5,5,5));
		saveButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();
                                saveToExcelFile();
			}
		});
		saveButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			saveButton.setEffect(new DropShadow());
		});
		saveButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			saveButton.setEffect(null);
		});

        btnContainer.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            btnContainer.setStyle("-fx-background-color: GOLD");
		});
        btnContainer.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            btnContainer.setStyle("-fx-background-color: SLATEGREY");
		});

		btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(saveButton);
		btnContainer.getChildren().add(cancelButton);

		tableOfEquipment.setPrefHeight(275);
        tableOfEquipment.setMaxWidth(3000);
		
		vbox.getChildren().add(tableOfEquipment);
		vbox.getChildren().add(btnContainer);
		vbox.setPadding(new Insets(10,10,10,10));
                vbox.setAlignment(Pos.CENTER);

		return vbox;
	}
       
	//--------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}
	
	//--------------------------------------------------------------------------
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

	protected void writeToFile(String fName)
    {
    	Vector allColumnNames = new Vector();

        try
        {
    	    FileWriter outFile = new FileWriter(fName);
            PrintWriter out = new PrintWriter(outFile);
            EquipmentCollection equipmentCollection = 
					(EquipmentCollection)myController.getState("EquipmentList");

			Vector entryList = (Vector)equipmentCollection.getState("Equipment");

            if ((entryList == null) || (entryList.size() == 0))
                return;

            allColumnNames.addElement("Barcode");
            allColumnNames.addElement("Name");
            allColumnNames.addElement("CategoryName");
            allColumnNames.addElement("Notes");
            allColumnNames.addElement("GoodCount");
            allColumnNames.addElement("FairCount");
            allColumnNames.addElement("PoorCount");
            allColumnNames.addElement("AvailableCount");
            allColumnNames.addElement("InStockCount");
            allColumnNames.addElement("DateAdded");
            allColumnNames.addElement("DateLastUsed");

            String line = "Barcode, Name, CategoryName, Notes, GoodCount, FairCount, PoorCount, " 
            					+ "AvailableCount, InStockCount, DateAdded, DateLastUsed";

            out.println(line);

            for (int k = 0; k < entryList.size(); k++)
            {
                String valuesLine = "";
                Equipment nextE = (Equipment)entryList.elementAt(k);
                Vector<String> nextRow = nextE.getEntryListView();

                for (int j = 0; j < allColumnNames.size()-1; j++)
                {
                    String nextValue = nextRow.elementAt(j);
                        if(nextValue != null)
                            valuesLine += nextValue + ", ";
                }

                out.println(valuesLine);
            }

            // Also print the shift count and filter type
            out.println("\nTotal number of Equipment Items: " + entryList.size());

            // Finally, print the time-stamp
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
            Date date = new Date();
            String timeStamp = dateFormat.format(date) + " " +
                    timeFormat.format(date);

            out.println("Equipment Report created on " + timeStamp);

            out.close();

            // Acknowledge successful completion to user with JOptionPane
            //JOptionPane.showMessageDialog(null, "Report data saved successfully to selected file");
            }

            catch (FileNotFoundException e)
            {
            //     JOptionPane.showMessageDialog(null, "Could not access file to save: "
            //             + fName, "Save Error", JOptionPane.ERROR_MESSAGE );
            }
            catch (IOException e)
            {
            //     JOptionPane.showMessageDialog(null, "Error in saving to file: "
            //             + e.toString(), "Save Error", JOptionPane.ERROR_MESSAGE );

            }
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
