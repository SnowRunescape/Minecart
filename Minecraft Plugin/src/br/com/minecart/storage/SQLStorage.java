package br.com.minecart.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.minecart.MineCart;
import br.com.minecart.MineCartKey;
import br.com.minecart.database.Database;

public class SQLStorage {
	public boolean saveMineCartKey(MineCartKey key){
		Database database = MineCart.getDB();
		
		Connection connection = database.getConnection();
        PreparedStatement preparedStatement = null;
        
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE `minecart_keys` SET ");
            queryBuilder.append("`score` = ?, `games_played` = ?, ");
            queryBuilder.append("`games_won` = ?, `kills` = ?, ");
            queryBuilder.append("`deaths` = ?, `last_seen` = NOW() ");
            queryBuilder.append("WHERE `player_name` = ?;");

            preparedStatement = connection.prepareStatement(queryBuilder.toString());
//            preparedStatement.setInt(1, gamePlayer.getScore());
//            preparedStatement.setInt(2, gamePlayer.getGamesPlayed());
//            preparedStatement.setInt(3, gamePlayer.getGamesWon());
//            preparedStatement.setInt(4, gamePlayer.getKills());
//            preparedStatement.setInt(5, gamePlayer.getDeaths());
//            preparedStatement.setString(6, gamePlayer.getName());
            preparedStatement.executeUpdate();
        } catch (final SQLException sqlException) {
        	sqlException.printStackTrace();
        }
        
        return false;
	}
}
