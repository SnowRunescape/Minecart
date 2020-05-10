package br.com.minecart.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.minecart.MineCart;

public class HttpRequest {
	public static String UrlJsonRequest(String Url){
		String json = null;
		
		try {
			URL url = new URL(Url);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestProperty("User-Agent", "MineCart");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.addRequestProperty ("Authorization", MineCart.instance.MineCartAutorization);
	        connection.setRequestProperty("ServerToken", MineCart.instance.MineCartServerToken);
	        
	        connection.setUseCaches(false);
	        
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            
            String line;
            
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            
            br.close();
            
            json = sb.toString().replaceAll("\\s+"," ");
		} catch (Exception e){
			
		}
		
		return json;
	}
}
