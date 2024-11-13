package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The AccountDAO class mediates the transformation of Account data between Java objects and database rows.
 * It assumes a database table named 'account' with columns: 
 * - id (int, primary key, auto-incremented),
 * - username (varchar),
 * - password (varchar).
 */
public class AccountDAO {

    /**
     * Retrieves all accounts from the account table.
     * 
     * @return a list of all Account objects.
     */
    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), 
                rs.getString("username"), 
                rs.getString("password"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * Inserts a new account into the account table.
     * 
     * @param account the Account object to be persisted.
     * @return the persisted Account with its generated ID, or null if insertion fails.
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set parameters
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_account_id = (int)pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves an account by username.
     * 
     * @param username the username of the account.
     * @return the Account object if found, otherwise null.
     */
    public Account getAccountByUsername(String account) {
        Connection connection = ConnectionUtil.getConnection();
        System.out.println("Database Connection: " + connection);
        try {
            // System.out.println("Executing SQL query to fetch account: " + account.getUsername());
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account);
            // preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                System.out.println("Account found with username: " + rs.getString("username"));
                return new Account(rs.getInt("account_id"), 
                                   rs.getString("username"), 
                                   rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
