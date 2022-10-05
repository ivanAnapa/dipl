package ru.netology.web.data.db;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionHelper {
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");
    private static Connection connection;


    public static Connection getConnection() {
        connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the SQL server successfully.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return connection;
    }

    // Удалить старые данные о покупках из таблиц
    public static void deletePrevOrders() {
        val runner = new QueryRunner();
        val order = "DELETE FROM order_entity";
        val paymentBuy = "DELETE FROM payment_entity";
        val creditBuy = "DELETE FROM credit_request_entity";

        try (val connection = getConnection()) {
            runner.update(connection, order);
            runner.update(connection, paymentBuy);
            runner.update(connection, creditBuy);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // **************************************** order_entity ****************************************
    // Получить дату покупки из таблицы order_entity
    public static String getPurchaseDateFromOrderEntity() {
        String statusQuery = "SELECT * FROM order_entity";
        val runner = new QueryRunner();
        try (Connection connection = getConnection()) {
            val purchaseDate = runner.query(connection, statusQuery, new BeanHandler<>(OrderEntity.class));
            return purchaseDate.getCreated().substring(0, 10);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // **************************************** payment_entity ****************************************
    // Получить статус покупки из таблицы payment_entity
    public static String getStatusFromPaymentEntity() {
        String statusQuery = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (Connection connection = getConnection()) {
            val status = runner.query(connection, statusQuery, new BeanHandler<>(PaymentEntity.class));
            return status.getStatus();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // Получить сумму покупки из таблицы payment_entity
    public static String getAmountFromPaymentEntity() {
        String statusQuery = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (Connection connection = getConnection()) {
            val amount = runner.query(connection, statusQuery, new BeanHandler<>(PaymentEntity.class));
            return amount.getAmount();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // Получить дату покупки из таблицы payment_entity
    public static String getPurchaseDateFromPaymentEntity() {
        String statusQuery = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (Connection connection = getConnection()) {
            val purchaseDate = runner.query(connection, statusQuery, new BeanHandler<>(PaymentEntity.class));
            return purchaseDate.getCreated().substring(0, 10);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }


    // **************************************** credit_request_entity ****************************************
    // Получить статус покупки из таблицы credit_request_entity
    public static String getStatusFromCreditRequestEntity() {
        String statusQuery = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (Connection connection = getConnection()) {
            val status = runner.query(connection, statusQuery, new BeanHandler<>(CreditRequestEntity.class));
            return status.getStatus();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // Получить ИД банка покупки из таблицы credit_request_entity
    public static String getBankIdFromCreditRequestEntity() {
        String statusQuery = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (Connection connection = getConnection()) {
            val bankId = runner.query(connection, statusQuery, new BeanHandler<>(CreditRequestEntity.class));
            return bankId.getBank_id();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // Получить дату покупки из таблицы credit_request_entity
    public static String getPurchaseDateFromCreditRequestEntity() {
        String statusQuery = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (Connection connection = getConnection()) {
            val purchaseDate = runner.query(connection, statusQuery, new BeanHandler<>(PaymentEntity.class));
            return purchaseDate.getCreated().substring(0, 10);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }







}
