package br.com.minecart.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.utilities.HttpRequest;

public class teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String products = HttpRequest.UrlJsonRequest("https://minecart.com.br/api/getProducts?shop_server=teste&username=SnowRunescape");
		
		System.out.println("Return MineCart: " + products);
		
		JsonObject jsonObject = new JsonParser().parse(products).getAsJsonObject();
		
		JsonElement StatusAPI = jsonObject.get("status");
		
		if(StatusAPI.getAsInt() == 200){
			JsonArray productsPlayer = jsonObject.getAsJsonArray("products");
			
			for (JsonElement product : productsPlayer){
				JsonObject productObj = product.getAsJsonObject();
				
				String item = productObj.get("item").getAsString();
				Integer quantity = productObj.get("quantity").getAsInt();
				Integer duration = productObj.get("duration").getAsInt();
			}
			
			//JsonObject productsPlayer =new JsonParser().parse(jsonObject.get("products").toString()).getAsJsonObject();
			System.out.println(productsPlayer);
		}
	}

}
