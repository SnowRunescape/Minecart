package br.com.minecart.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.minecart.MineCart;
import br.com.minecart.MineCartKey;
import br.com.minecart.database.Database;

public class SQLStorage {
	public boolean saveMineCartKey(MineCartKey minecartKey){
		Database database = MineCart.getDB();
		
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
			
			preparedStatement.executeUpdate();
			
			return true;
        } catch (final SQLException sqlException) {
        	sqlException.printStackTrace();
        }
        
        return false;
	}
}
