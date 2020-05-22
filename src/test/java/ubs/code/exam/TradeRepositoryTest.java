package ubs.code.exam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ubs.code.exam.models.Trade;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TradeRepositoryTest {
    @BeforeClass
    public static void setup() throws Exception {
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


    @AfterClass
    public static void tearDown() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.executeUpdate("DROP TABLE trades");
            connection.commit();
        }
    }

    /**
     * Create a connection
     *
     * @return connection object
     * @throws SQLException
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:trades", "user", "user");
    }

    /**
     * Get total records in table
     *
     * @return total number of records. In case of exception 0 is returned
     */
    private int getTotalRecords() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            ResultSet result = statement.executeQuery("SELECT count(*) as total FROM trades");
            if (result.next()) {
                return result.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Test
    public void getTotalRecordsTest() {
        assertThat(3, is(getTotalRecords()));
    }

    @Test
    private int getAll() {
        List<Trade> trades = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            ResultSet result = statement.executeQuery("SELECT *  FROM trades");
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
        return 0;
    }
}
