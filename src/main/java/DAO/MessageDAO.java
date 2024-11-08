package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The MessageDAO class mediates the transformation of Message data between Java objects and database rows.
 * It assumes a database table named 'message' with columns:
 * - id (int, primary key, auto-incremented),
 * - account_id (int, foreign key referencing Account table),
 * - content (varchar).
 */
public class MessageDAO {

    /**
     * Retrieves all messages from the message table.
     * 
     * @return a list of all Message objects.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );
            messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Inserts a new message into the message table.
     * 
     * @param message the Message object to be persisted.
     * @return the persisted Message with its generated ID, or null if insertion fails.
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
            // Set parameters using the appropriate getter methods
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
    
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = pkeyResultSet.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    

    /**
     * Retrieves a message by its ID.
     * 
     * @param messageId the ID of the message.
     * @return the Message object if found, otherwise null.
     */
    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Updates the text of a message.
     * 
     * @param messageId the ID of the message to update.
     * @param newText the new content for the message.
     * @return the updated Message if successful, otherwise null.
     */
    public Message updateMessageText(int messageId, String newText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET content = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set parameters
            preparedStatement.setString(1, newText);
            preparedStatement.setInt(2, messageId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Deletes a message by its ID.
     * 
     * @param messageId the ID of the message to delete.
     * @return true if the message was deleted, false otherwise.
     */
    public boolean deleteMessage(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all messages for a specific account.
     * 
     * @param accountId the ID of the account whose messages are to be retrieved.
     * @return a list of messages for the specified account.
     */
    public List<Message> getMessagesByUserId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );
            messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
