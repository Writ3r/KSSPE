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
import model.Borrower;
import model.CheckOut;
import model.Worker;
import model.CheckOutCollection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

//==============================================================================
public class CheckOutCollectionReportView extends CheckOutCollectionView
{
	
	protected Button saveToFileButton;
 
        
	//--------------------------------------------------------------------------
	public CheckOutCollectionReportView(Transaction t)
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
		
	}

	//--------------------------------------------------------------------------
	protected void populateFields()
	{
		getEntryTableModelValues();
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues()
	{

		ObservableList<CheckOutTableModel> tableData = FXCollections.observableArrayList();
		try
		{
	
			CheckOutCollection checkoutCollection = 
					(CheckOutCollection)myController.getState("CheckOutList");

			Vector entryList = (Vector)checkoutCollection.getState("CheckOuts");
			
			if (entryList.size() > 0)
			{
				Enumeration entries = entryList.elements();

				while (entries.hasMoreElements() == true)
				{
					CheckOut nextCO = (CheckOut)entries.nextElement();

					Vector<String> view = nextCO.getEntryListView();

					// add this list entry to the list
					CheckOutTableModel nextTableRowData = new CheckOutTableModel(view);
					tableData.add(nextTableRowData);

				}
				if(entryList.size() == 1)
					actionText.setText(entryList.size()+" Reservation Matching Criteria Found!");
				else 
					actionText.setText(entryList.size()+" Reservations Matching Criteria Found! ");

				actionText.setFill(Color.LIGHTGREEN);
			}
			else
			{
				actionText.setText("No Reservations Found!");
				actionText.setFill(Color.FIREBRICK);
			}

			tableOfCheckOuts.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
			actionText.setText("ERROR: Unexpected error!");
			actionText.setFill(Color.FIREBRICK);
		}

	}

	
	//--------------------------------------------------------------
	protected String getActionText()
	{
		return "** LIST OF RESERVATIONS **";
	}

	// Create the main form content
	//-------------------------------------------------------------
	protected VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		tableOfCheckOuts = new TableView<CheckOutTableModel>();
		tableOfCheckOuts.setEffect(new DropShadow());
		tableOfCheckOuts.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
		tableOfCheckOuts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		
		TableColumn barcodePrefixColumn = new TableColumn("Barcode") ;
		barcodePrefixColumn.setMinWidth(116.6);
		barcodePrefixColumn.setStyle(" -fx-alignment: CENTER;");
		barcodePrefixColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("Barcode"));


		TableColumn bannerColumn = new TableColumn("Banner ID") ;
		bannerColumn.setMinWidth(116.6);
		bannerColumn.setStyle(" -fx-alignment: CENTER;");
		bannerColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("BannerId"));
		
		TableColumn equipNameColumn = new TableColumn("Equipment Name") ;
		equipNameColumn.setMinWidth(116.6);
		equipNameColumn.setStyle(" -fx-alignment: CENTER;");
		equipNameColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("EquipmentName"));
				
		TableColumn takenColumn = new TableColumn("Units Taken") ;
		takenColumn.setMinWidth(136.6);
		takenColumn.setStyle(" -fx-alignment: CENTER;");
		takenColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("UnitsTaken"));

		TableColumn returnedColumn = new TableColumn("Total Units Returned") ;
		returnedColumn.setMinWidth(136.6);
		returnedColumn.setStyle(" -fx-alignment: CENTER;");
		returnedColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("TotalUnitsReturned"));

		TableColumn dueColumn = new TableColumn("Due Date") ;
		dueColumn.setMinWidth(136.6);
		dueColumn.setStyle(" -fx-alignment: CENTER;");
		dueColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("DueDate"));

		TableColumn rentColumn = new TableColumn("Check Out Date") ;
		rentColumn.setMinWidth(136.6);
		rentColumn.setStyle(" -fx-alignment: CENTER;");
		rentColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("RentDate"));

		tableOfCheckOuts.getColumns().addAll(bannerColumn, barcodePrefixColumn, equipNameColumn, takenColumn, returnedColumn, dueColumn, rentColumn);

		ImageView icon = new ImageView(new Image("/images/check.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		saveToFileButton = new Button("Save To File",icon);
		saveToFileButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		saveToFileButton.requestFocus();
		saveToFileButton.setOnAction((ActionEvent e) -> {
			clearErrorMessage();
			saveToExcelFile();
		});
		saveToFileButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			saveToFileButton.setEffect(new DropShadow());
		});
		saveToFileButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			saveToFileButton.setEffect(null);
		});
		icon = new ImageView(new Image("/images/return.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		cancelButton = new Button("Return", icon);
		cancelButton.setGraphic(icon);
		cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		cancelButton.setOnAction((ActionEvent e) -> {
			clearErrorMessage();
			myController.stateChangeRequest("CancelCheckOutList", null);
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
		btnContainer.getChildren().add(saveToFileButton);
		btnContainer.getChildren().add(cancelButton);

		tableOfCheckOuts.setPrefHeight(275);
        tableOfCheckOuts.setMaxWidth(550);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(550, 150);
		scrollPane.setContent(tableOfCheckOuts); // should we use this? (Probably not - FX tables come with their own scroll pane)
		
		vbox.getChildren().add(tableOfCheckOuts);
		vbox.getChildren().add(btnContainer);
		vbox.setPadding(new Insets(10,10,10,10));
                vbox.setAlignment(Pos.CENTER);

		return vbox;
	}

	//-------------------------------------------------------------
	protected void writeToFile(String fName)
    {
    	Vector allColumnNames = new Vector();

        try
        {
    	    FileWriter outFile = new FileWriter(fName);
            PrintWriter out = new PrintWriter(outFile);
            CheckOutCollection CheckOutCollection = 
					(CheckOutCollection)myController.getState("CheckOutList");

			Vector entryList = (Vector)CheckOutCollection.getState("CheckOuts");

            if ((entryList == null) || (entryList.size() == 0))
                return;

            allColumnNames.addElement("BannnerId");
			allColumnNames.addElement("BorrowerName");
            allColumnNames.addElement("Barcode");
            allColumnNames.addElement("EquipmentName");
            allColumnNames.addElement("UnitsTaken");
            allColumnNames.addElement("TotalUnitsReturned");
            allColumnNames.addElement("RentDate");
            allColumnNames.addElement("DueDate");
            allColumnNames.addElement("CheckOutWorkerID");
       

            String line = "BannerId, BorrowerName, Barcode, EquipmentName, UnitsTaken, TotalUnitsReturned, DueDate, RentDate, " 
            					+ "CheckOutWorkerID, CheckOutWorkerName";

            out.println(line);

            for (int k = 0; k < entryList.size(); k++)
            {
                String valuesLine = "";
                CheckOut nextE = (CheckOut)entryList.elementAt(k);
                Vector<String> nextRow = nextE.getEntryListView();
				valuesLine += nextRow.elementAt(1) + ", ";
				try
				{
					Properties p = new Properties();
					p.setProperty("BannerId", nextRow.elementAt(1));
					Borrower b = new Borrower(p);
					valuesLine += (b.getState("FirstName") + " " + b.getState("LastName")) + ", ";
				}
				catch (Exception ex)
				{
					valuesLine += "Unknown Borrower" + ", ";
				}
				valuesLine += nextRow.elementAt(2) + ", ";
				valuesLine += nextRow.elementAt(8) + ", ";
				valuesLine += nextRow.elementAt(3) + ", ";
				valuesLine += nextRow.elementAt(4) + ", ";
				valuesLine += nextRow.elementAt(5) + ", ";
				valuesLine += nextRow.elementAt(6) + ", ";
				valuesLine += nextRow.elementAt(7) + ", ";
				
				try
				{
					Properties p = new Properties();
					p.setProperty("BannerId", nextRow.elementAt(7));
					Worker w = new Worker(p);
					valuesLine += (w.getState("FirstName") + " " + w.getState("LastName")) + ", ";
				}
				catch (Exception ex)
				{
					valuesLine += "Unknown Worker" + ", ";
				}

                /*for (int j = 0; j < allColumnNames.size()-1; j++)
                {
                    String nextValue = nextRow.elementAt(j);
                    if(nextValue != null)
                            valuesLine += nextValue + ", ";
                }*/
				
                out.println(valuesLine);
            }

            // Also print the shift count and filter type
            out.println("\nTotal number of CheckOut Items: " + entryList.size());

            // Finally, print the time-stamp
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
            Date date = new Date();
            String timeStamp = dateFormat.format(date) + " " +
                    timeFormat.format(date);

            out.println("CheckOut Report created on " + timeStamp);

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


}
