package utilities;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import utilities.Utilities;

import model.Equipment;

//===========================================================================
public class ReturnReceipt
{
	// Declare a home directory to save receipts in at the top of the class
	private static final String receiptHomeDirectory = 
			System. getProperty("user.dir") + File.separator + "receipts";

	// Hold the receipt to save as a hash table
	private Hashtable receipt = new Hashtable();
	private int CHECKIN_TIME_HOUR;
	private int CHECKIN_TIME_MINUTE;

	// CREATE the Hashtable if it does not exist in the constructor of whichever class is building up the receipt

	// Put stuff in the Hashtable to build up the receipt as appropriate
	// For example, if the KSSPE people want the worker name and banner id to be printed as part of the receipt,
	// so they must be put in the receipt
	//------------------------------------------------------------------------------
	public ReturnReceipt(Properties p, Vector returns) throws Exception
	{
		receipt.put("WorkerName", p.getProperty("WorkerName"));
		receipt.put("WorkerBannerId", p.getProperty("WorkerBannerId"));
		receipt.put("BorrowerName", p.getProperty("BorrowerName"));
		receipt.put("BorrowerBannerId", p.getProperty("BorrowerBannerId"));
		receipt.put("Returns", returns);
		
		CHECKIN_TIME_HOUR = 12;
		CHECKIN_TIME_MINUTE = 0;
		
		System.out.println("Nick was here");
		printReceipt();
		System.out.println("Lucas was here");
	}

	//----------------------------------------------------------------
	private void printReceipt() throws Exception
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		
		int nowYear = cal.get(Calendar.YEAR);
		int nowMonth = cal.get(Calendar.MONTH);
			
		String yearValue = (new Integer(nowYear)).toString();
		String monthName = Utilities.mapMonthToString(nowMonth);
			
		String receiptDirectoryName = receiptHomeDirectory + File.separator +
				"CheckinReceipts" + File.separator + yearValue + File.separator +
				monthName; 
				
		String nowTimeText = dateFormatter.format(cal.getTime());
		String nowTimeText2 = dateFormatter2.format(cal.getTime());		
		
		receipt.put("ReceiptTime", nowTimeText);
		
		String borrowerName = (String)receipt.get("BorrowerName");
		String receiptFileName = "";
				
		File receiptDirectory = new File(receiptDirectoryName);
		if (receiptDirectory.exists() == false)
		{
			boolean flag = receiptDirectory.mkdirs();
				
			if (flag == true)
			{
				receiptFileName = receiptDirectoryName + File.separator + 
						 borrowerName + "Checkin" + nowTimeText2 + ".txt";
			}
			else
			{
				throw new Exception("Could not create directory to store checkin receipt"); 
			}
		}
		else
		{
			receiptFileName = receiptDirectoryName + File.separator + 
				borrowerName + "Checkin" + nowTimeText2 + ".txt";
		}

		writeReceiptDataToFile(receiptFileName);
		//printFileToDefaultPrinter(receiptFileName);
		
	}

	//----------------------------------------------------------------
	private void writeReceiptDataToFile(String fileName) throws Exception
	{
		BufferedWriter outputFile = null;
			try
			{
				outputFile = new BufferedWriter(new FileWriter(new File(fileName)));
				
				outputFile.newLine();
				outputFile.write("                                KSSPE DEPARTMENT");
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("                           EQUIPMENT CHECK-IN RECEIPT");
				outputFile.newLine();
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Date: " + (String)receipt.get("ReceiptTime"));
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Borrower Name (BannerID): " + (String)receipt.get("BorrowerName") + "(" + (String)receipt.get("BorrowerBannerId") + ")");
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Worker Name (BannerID): " + (String)receipt.get("WorkerName") + "(" + (String)receipt.get("WorkerBannerId") + ")") ;
				outputFile.newLine();
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write(String.format("%20s %25s %20s %20s \r\n", "Barcode", "Name", "Count", "Return Date"));
				outputFile.write("-----------------------------------------------------------------------------------------");
				outputFile.newLine();
				
				// Put all the returns in
				// Your returns should be in a COLLECTION (Vector, ArrayList, ....)
				Vector allReturnsInReceipt = (Vector)receipt.get("Returns");
				if (allReturnsInReceipt != null)
				{
					for (int cnt = 0; cnt < allReturnsInReceipt.size(); cnt++)
					{
						Properties returnInfo = (Properties)allReturnsInReceipt.elementAt(cnt);
							String barcodeID = (String)returnInfo.getProperty("Barcode");
							String itemName = (String)returnInfo.getProperty("EquipmentName");
							String count = (String)returnInfo.getProperty("Count");
							String returnDate = (String)returnInfo.getProperty("ReturnDate");
						
						if (itemName.length() > 25)
						{
							itemName = itemName.substring(0,25);
						}
						
						outputFile.write(String.format("%20s %25s %20s %20s \r\n", barcodeID, itemName, count, returnDate));
					}
				}
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());

				SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aaa");
				String nowTimeText = dateFormatter.format(cal.getTime());

				outputFile.newLine();
				outputFile.newLine();
				
				cal.setTime(new Date());
				cal.add(cal.MINUTE, 15);
				String futureTimeText = dateFormatter.format(cal.getTime());

				outputFile.newLine();
				outputFile.newLine();
				
				String tailMessage = "Thank you!";
					
				outputFile.write(tailMessage);
				outputFile.newLine();
					
				outputFile.close();
				
			}
			catch(IOException e)
			{ 
				throw new Exception("File I/0 error in creating receipt"); 
			}
	}
		
	//----------------------------------------------------------------
	private void printFileToDefaultPrinter(String fileName) throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		String cmd = "notepad /p " + fileName;
		try
		{
			rt.exec(cmd);
		}
		catch (Exception ex)
		{
			throw new Exception("Error in printing receipt"); 				
		}
	}
}
	