package br.com.minecart.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class LOGStorage {
	public static void resgatarVIP(String a){
		try {
			FileWriter buffWrite = new FileWriter("plugins/Minecart/RedeemVIP_error.log", true);
	    	
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			
	    	buffWrite.write("[" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) + " - "+ calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "] " + a + "\n");
			buffWrite.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void resgatarCASH(String a){
		try {
			FileWriter buffWrite = new FileWriter("plugins/Minecart/RedeemCASH_error.log", true);
	    	
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			
	    	buffWrite.write("[" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) + " - "+ calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "] " + a + "\n");
			buffWrite.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
