package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    @SneakyThrows
    private static Connection getConn() {
        return DriverManager.getConnection(System.getProperty("db.url"), System.getProperty("db.username"),
                System.getProperty("db.password"));
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var payment = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var conn = SQLHelper.getConn();
        return QUERY_RUNNER.query(conn, payment, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var conn = getConn();
        QUERY_RUNNER.execute(conn, "DELETE FROM payment_entity");
        QUERY_RUNNER.execute(conn, "DELETE FROM order_entity");
    }
}
