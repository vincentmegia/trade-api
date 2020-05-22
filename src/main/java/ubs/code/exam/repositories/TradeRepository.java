package ubs.code.exam.repositories;

import org.springframework.stereotype.Repository;
import ubs.code.exam.models.Trade;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TradeRepository {
    public TradeRepository() {
        initialize();
    }

    public void initialize() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE trades (id INT NOT NULL, side VARCHAR(50) NOT NULL, symbol VARCHAR(50) NOT NULL,"
                    + "price DECIMAL NOT NULL, PRIMARY KEY (id))");
            connection.commit();
            statement.executeUpdate("INSERT INTO trades VALUES (1,'buy', 'ALI', 53.5)");
            statement.executeUpdate("INSERT INTO trades VALUES (2,'buy', 'AC', 30.5)");
            statement.executeUpdate("INSERT INTO trades VALUES (3,'buy', 'SM', 20.5)");
            connection.commit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void cleanup() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE trades");
            connection.commit();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:trades", "user", "user");
    }

    public List<Trade> getAll() {
        List<Trade> trades = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT * FROM trades");
            while (result.next()) {
                Trade trade = new Trade();
                trade.setId(result.getInt("id"));
                trade.setSide(result.getString("side"));
                trade.setSymbol(result.getString("symbol"));
                trade.setPrice(result.getBigDecimal("price"));
                trades.add(trade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trades;
    }

    public boolean deleteAll() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.executeQuery("DELETE FROM trades");
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Trade getById(long tradeId) {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            ResultSet result = statement.executeQuery("SELECT * FROM trades where id = '" + tradeId + "'");
            Trade trade = null;
            if (result.next()) {
                trade = new Trade();
                trade.setId(result.getInt("id"));
                trade.setSide(result.getString("side"));
                trade.setSymbol(result.getString("symbol"));
                trade.setPrice(result.getBigDecimal("price"));
            }
            return trade;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Trade();
    }

    public boolean add(Trade trade) {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO trades VALUES (%1$s,'%2$s', '%3$s', %4$s)",
                    trade.getId(), trade.getSide(), trade.getSymbol(), trade.getPrice());
            statement.executeUpdate(sql);
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
