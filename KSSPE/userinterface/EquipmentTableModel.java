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
	private final SimpleStringProperty AvaiableCount;
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
		FairCount = new SimpleStringProperty(atData.elementAt(5));
		PoorCount = new SimpleStringProperty(atData.elementAt(6));
		AvaiableCount = new SimpleStringProperty(atData.elementAt(7));
		InStockCount = new SimpleStringProperty(atData.elementAt(8));
		DateAdded = new SimpleStringProperty(atData.elementAt(9));
		DateLastUsed = new SimpleStringProperty(atData.elementAt(10));
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
	public void setName(String pref) {
		Name.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getCategoryName() {
		return CategoryName.get();
	}

	//----------------------------------------------------------------------------
	public void setCategoryName(String pref) {
		CategoryName.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getNotes() {
		return Notes.get();
	}

	//----------------------------------------------------------------------------
	public void setNotes(String pref) {
		Notes.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getGoodCount() {
		return GoodCount.get();
	}

	//----------------------------------------------------------------------------
	public void setGoodCount(String pref) {
		GoodCount.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getFairCount() {
		return FairCount.get();
	}

	//----------------------------------------------------------------------------
	public void setFairCount(String pref) {
		FairCount.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getPoorCount() {
		return PoorCount.get();
	}

	//----------------------------------------------------------------------------
	public void setPoorCount(String pref) {
		PoorCount.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getAvaiableCount() {
		return AvaiableCount.get();
	}

	//----------------------------------------------------------------------------
	public void setAvaiableCount(String pref) {
		AvaiableCount.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getInStockCount() {
		return InStockCount.get();
	}

	//----------------------------------------------------------------------------
	public void setInStockCount(String pref) {
		InStockCount.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getDateAdded() {
		return DateAdded.get();
	}

	//----------------------------------------------------------------------------
	public void setDateAdded(String pref) {
		DateAdded.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getDateLastUsed() {
		return DateLastUsed.get();
	}

	//----------------------------------------------------------------------------
	public void setDateLastUsed(String pref) {
		DateLastUsed.set(pref);
	}
}
