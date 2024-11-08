package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

/**
 * The MessageService class handles business logic related to messages, 
 * sitting between the controller and persistence layer (DAO).
 */
public class MessageService {
    private MessageDAO messageDAO;

    /**
     * No-args constructor for creating a new MessageService with a new MessageDAO.
     */
    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    /**
     * Constructor for MessageService when a MessageDAO is provided.
     * Used in testing with a mock MessageDAO to test MessageService independently.
     * 
     * @param messageDAO the MessageDAO to be used by this service
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * Retrieves all messages.
     * 
     * @return a list of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Persists a new message.
     * 
     * @param message a Message object to be added
     * @return the persisted Message if successful, otherwise null
     */
    public Message createMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    /**
     * Retrieves a message by its ID.
     * 
     * @param messageId the ID of the message
     * @return the Message object if found, otherwise null
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Updates the text of an existing message.
     * 
     * @param messageId the ID of the message
     * @param newText the new text for the message
     * @return the updated Message if successful, otherwise null
     */
    public Message updateMessageText(int messageId, String newText) {
        return messageDAO.updateMessageText(messageId, newText);
    }

    /**
     * Deletes a message by its ID.
     * 
     * @param messageId the ID of the message
     * @return true if the message was deleted, false otherwise
     */
    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    /**
     * Retrieves all messages for a specific user.
     * 
     * @param accountId the ID of the account
     * @return a list of messages for the specified account
     */
    public List<Message> getMessagesByUserId(int accountId) {
        return messageDAO.getMessagesByUserId(accountId);
    }
}
