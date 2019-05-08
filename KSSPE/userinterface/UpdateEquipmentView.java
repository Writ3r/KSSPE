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
import javafx.scene.control.ComboBox;

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

/** The class containing the Update Category View  for the KSSPE
 *  application 
 */
//==============================================================
public class UpdateEquipmentView extends AddEquipmentView
{

	//

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public UpdateEquipmentView(Transaction t)
	{
		super(t);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** UPDATING EQUIPMENT **";
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		removeDisables();

		String bc = (String)myController.getState("Barcode");
		if (bc != null)
		{
			barcode.setText(bc);
			barcode.setDisable(true);
		}
		String nameText = (String)myController.getState("Name");
		if (nameText != null)
		{
			equipmentName.setText(nameText);
		}
		String catText = (String)myController.getState("CategoryName");
		if (catText != null)
		{
			category.setValue(catText);
		}
		String noteText = (String)myController.getState("Notes");
		if (noteText != null)
		{
			notes.setText(noteText);
		}
		String poorCountText = (String)myController.getState("PoorCount");
		if (poorCountText != null)
		{
			poor.setText(poorCountText);
		}
		String fairCountText = (String)myController.getState("FairCount");
		if (fairCountText != null)
		{
			fair.setText(fairCountText);
		}
		String goodCountText = (String)myController.getState("GoodCount");
		if (goodCountText != null)
		{
			good.setText(goodCountText);
		}
		
		
		submitButton.setText("Update"); //fix submitbutton
		ImageView icon = new ImageView(new Image("/images/savecolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		submitButton.setGraphic(icon);
	}


	public void clearValues(){

	}

	private void removeDisables()
	{
		equipmentName.setDisable(false);
		poor.setDisable(false);
		category.setDisable(false);
		fair.setDisable(false);
		good.setDisable(false);
		notes.setDisable(false);
	}

}

//---------------------------------------------------------------
//	Revision History:
//


