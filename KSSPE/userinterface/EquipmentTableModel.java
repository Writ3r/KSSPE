package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class EquipmentTableModel
{
	private final SimpleStringProperty Barcode;
	private final SimpleStringProperty Name;
	private final SimpleStringProperty CategoryName;
	private final SimpleStringProperty Notes;
	private final SimpleStringProperty GoodCount;
	private final SimpleStringProperty FairCount;
	private final SimpleStringProperty PoorCount;
	private final SimpleStringProperty AvailableCount;
	private final SimpleStringProperty InStockCount;
	private final SimpleStringProperty DateAdded;
	private final SimpleStringProperty DateLastUsed;

	//----------------------------------------------------------------------------
	public EquipmentTableModel(Vector<String> atData)
	{
		Barcode =  new SimpleStringProperty(atData.elementAt(0));
		Name =  new SimpleStringProperty(atData.elementAt(1));
		CategoryName =  new SimpleStringProperty(atData.elementAt(2));
		Notes =  new SimpleStringProperty(atData.elementAt(3));
		GoodCount =  new SimpleStringProperty(atData.elementAt(4));
		FairCount =  new SimpleStringProperty(atData.elementAt(5));
		PoorCount =  new SimpleStringProperty(atData.elementAt(6));
		AvailableCount =  new SimpleStringProperty(atData.elementAt(7));
		InStockCount =  new SimpleStringProperty(atData.elementAt(8));
		DateAdded =  new SimpleStringProperty(atData.elementAt(9));
		DateLastUsed =  new SimpleStringProperty(atData.elementAt(10));
	}

	//----------------------------------------------------------------------------
	public String getBarcode() {
		return Barcode.get();
	}

	//----------------------------------------------------------------------------
	public void setBarcode(String pref) {
		Barcode.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getName() {
		return Name.get();
	}

	//----------------------------------------------------------------------------
	public void setName(String desc) {
		Name.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getCategoryName() {
		return CategoryName.get();
	}

	//----------------------------------------------------------------------------
	public void setCategoryName(String desc) {
		CategoryName.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getNotes() {
		return Notes.get();
	}

	//----------------------------------------------------------------------------
	public void setNotes(String desc) {
		Notes.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getGoodCount() {
		return GoodCount.get();
	}

	//----------------------------------------------------------------------------
	public void setGoodCount(String desc) {
		GoodCount.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getFairCount() {
		return FairCount.get();
	}

	//----------------------------------------------------------------------------
	public void setFairCount(String desc) {
		FairCount.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getPoorCount() {
		return PoorCount.get();
	}

	//----------------------------------------------------------------------------
	public void setPoorCount(String desc) {
		PoorCount.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getAvailableCount() {
		return AvailableCount.get();
	}

	//----------------------------------------------------------------------------
	public void setAvailableCount(String desc) {
		AvailableCount.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getInStockCount() {
		return InStockCount.get();
	}

	//----------------------------------------------------------------------------
	public void setInStockCount(String desc) {
		InStockCount.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getDateAdded() {
		return DateAdded.get();
	}

	//----------------------------------------------------------------------------
	public void setDateAdded(String desc) {
		DateAdded.set(desc);
	}
	
	//----------------------------------------------------------------------------
	public String getDateLastUsed() {
		return DateLastUsed.get();
	}

	//----------------------------------------------------------------------------
	public void setDateLastUsed(String desc) {
		DateLastUsed.set(desc);
	}
}
