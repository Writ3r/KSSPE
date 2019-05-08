package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class EquipmentTableModel
{
	private final SimpleStringProperty barcode;
	private final SimpleStringProperty name;
	//private final SimpleStringProperty status;

	//----------------------------------------------------------------------------
	public EquipmentTableModel(Vector<String> atData)
	{
		barcode =  new SimpleStringProperty(atData.elementAt(0));
		name =  new SimpleStringProperty(atData.elementAt(1));
		//status =  new SimpleStringProperty(atData.elementAt(2));
	}

	//----------------------------------------------------------------------------
	public String getBarcode() {
		return barcode.get();
	}

	//----------------------------------------------------------------------------
	public void setBarcode(String pref) {
		barcode.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getName() {
		return name.get();
	}

	//----------------------------------------------------------------------------
	public void setName(String desc) {
		name.set(desc);
	}

	//----------------------------------------------------------------------------
	// public String getStatus() {
	// 	return status.get();
	// }

	//----------------------------------------------------------------------------
	// public void setStatus(String stat)
	// {
	// 	status.set(stat);
	// }
}
