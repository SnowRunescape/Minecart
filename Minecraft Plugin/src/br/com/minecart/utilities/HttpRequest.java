package br.com.minecart.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import br.com.minecart.MineCart;

public class HttpRequest {
	public static String UrlJsonRequest(String Url, Map<String, String> params){
		String json = null, urlParameters = null;
		
		if(params != null){
			StringBuilder postData = new StringBuilder();
			
			for (Entry<String, String> param : params.entrySet()){
		        try {
		        	if(postData.length() != 0) postData.append('&');
		        	
					postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
					
					postData.append('=');
			        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					return null;
				}
		    }
			
			urlParameters = postData.toString();
		}
		
		try {
			URL url = new URL(Url);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestProperty("User-Agent", "MineCart");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.addRequestProperty("Authorization", MineCart.instance.MineCartAutorization);
	        connection.addRequestProperty("ShopServer", MineCart.instance.MineCartShopServer);
	        
	        connection.setUseCaches(false);
	        
	        connection.setConnectTimeout(5000);
	        connection.setReadTimeout(5000);
	        
	        if(urlParameters != null){
	        	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
	        	connection.setRequestMethod("POST");
	        	
	        	connection.setDoOutput(true);
	        	
	            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

	            writer.write(urlParameters);
	            writer.flush();
	        }
	        
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            
            String line;
            
            while((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            
            br.close();
            
            json = sb.toString().replaceAll("\\s+"," ");
		} catch (Exception e){
			
		}
		
		return json;
	}
}
