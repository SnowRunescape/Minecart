package br.com.minecart.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import br.com.minecart.MineCart;
import br.com.minecart.MineCartKey;
import br.com.minecart.database.Database;

public class SQLStorage {
	public HashMap<String, MineCartKey> loadAllMineCartKeys(){
		HashMap<String, MineCartKey> minecartKeys = new HashMap<String, MineCartKey>();
		
		Database database = MineCart.getDB();
		
		try {
			if(database.checkConnection()){
				Connection connection = database.getConnection();
		        PreparedStatement preparedStatement = null;
		        
		        try {
		            StringBuilder queryBuilder = new StringBuilder();
		            
		            queryBuilder.append("SELECT * FROM `minecart`");
		            
					preparedStatement = connection.prepareStatement(queryBuilder.toString());
					ResultSet resultSet = preparedStatement.executeQuery();
					
					if(resultSet != null){
						while(resultSet.next()){
							String keyCode = resultSet.getString("key_vip");
							String vipGroup = resultSet.getString("vip_group");
							Integer vipDuration = resultSet.getInt("vip_duration");
							String vipOwner = resultSet.getString("owner");
							
							minecartKeys.put(keyCode, new MineCartKey(vipOwner, keyCode, vipGroup, vipDuration));
		            	}
					}
		        } catch (final SQLException sqlException) {
		        	sqlException.printStackTrace();
		        }
			}
		} catch(Exception e){
			
		}
        
        return minecartKeys;
	}
	
	public boolean saveMineCartKey(MineCartKey minecartKey){
		Database database = MineCart.getDB();
		
		try {
			if(!database.checkConnection()) return false;
		} catch(Exception e){
			return false;
		}
		
		Connection connection = database.getConnection();
        PreparedStatement preparedStatement = null;
        
        try {
            StringBuilder queryBuilder = new StringBuilder();
            
            queryBuilder.append("INSERT INTO `minecart` (`key_vip`, `vip_group`, `vip_duration`, `owner`) VALUES (?, ?, ?, ?)");
            
			preparedStatement = connection.prepareStatement(queryBuilder.toString());
			
			preparedStatement.setString(1, minecartKey.getCode());
			preparedStatement.setString(2, minecartKey.getGrup());
			preparedStatement.setInt(3, minecartKey.getDuration());
			preparedStatement.setString(4, minecartKey.getOwner());
			
			return preparedStatement.executeUpdate() > 0 ? true : false;
        } catch (final SQLException sqlException) {
        	sqlException.printStackTrace();
        }
        
        return false;
	}
	
	public boolean deleteMineCartKey(MineCartKey minecartKey){
		Database database = MineCart.getDB();
		
		try {
			if(!database.checkConnection()) return false;
		} catch(Exception e){
			return false;
		}
		
		Connection connection = database.getConnection();
        PreparedStatement preparedStatement = null;
		
        try {
            StringBuilder queryBuilder = new StringBuilder();
            
            queryBuilder.append("DELETE FROM `minecart` WHERE key_vip = ? LIMIT 1");
            
			preparedStatement = connection.prepareStatement(queryBuilder.toString());
			
			preparedStatement.setString(1, minecartKey.getCode());
			
			return preparedStatement.executeUpdate() > 0 ? true : false;
        } catch (final SQLException sqlException) {
        	sqlException.printStackTrace();
        }
        
		return false;
	}
}
