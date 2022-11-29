package ru.netology.sql;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public static void cleanDb() {
        var deleteCreditRequest = "DELETE FROM credit_request_entity";
        var deleteOrderEntity = "DELETE FROM order_entity";
        var deletePaymentEntity = "DELETE FROM payment_entity";
        var runner = new QueryRunner();
        try (var conn = getConnection();
        ) {
            runner.update(conn, deleteCreditRequest);
            runner.update(conn, deleteOrderEntity);
            runner.update(conn, deletePaymentEntity);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @SneakyThrows
    public static String getPaymentEntity() {
        try (var conn = getConnection();
             var countStmt = conn.createStatement()) {
            var paymentStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
            var resultSet = countStmt.executeQuery(paymentStatus);
            if (resultSet.next()) {
                return resultSet.getString("status");
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public static String getCreditEntity() {
        try (var conn = getConnection();
             var countStmt = conn.createStatement()) {
            var creditStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
            var resultSet = countStmt.executeQuery(creditStatus);
            if (resultSet.next()) {
                return resultSet.getString("status");
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }
}