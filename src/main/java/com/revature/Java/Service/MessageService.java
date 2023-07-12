package com.revature.Java.Service;

import java.util.List;

import com.revature.Java.DAO.AccountDAO;
import com.revature.Java.DAO.MessageDAO;
import com.revature.Java.Model.Message;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    /**
     * Create a new message and persist it to the database.
     *
     * @param message the message to create.
     * @return the created message with the generated message ID, or null if not
     *         successful.
     */
    public Message createMessage(Message message) {
        AccountDAO accountDAO = new AccountDAO();
        if (isMessageValid(message) && accountDAO.doesAccountExistAccountID(message.getPosted_by())) {
            return messageDAO.insertMessage(message);
        }
        return null;
    }

    /**
     * Retrieve all messages from the database.
     *
     * @return a list of all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieve a message by its ID.
     *
     * @param messageId the ID of the message to retrieve.
     * @return the message with the specified ID, or null if not found.
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Delete a message by its ID.
     *
     * @param messageId the ID of the message to delete.
     * @return true if the deletion is successful, false otherwise.
     */
    public Message deleteMessage(int messageId) {
        Message message = getMessageById(messageId);
        if (message != null) {
            messageDAO.deleteMessage(messageId);
            return message;
        }
        return null;
    }

    /**
     * Update the text of a message.
     *
     * @param messageId   the ID of the message to update.
     * @param updatedText the updated message text.
     * @return the updated message, or null if the update is not successful.
     */
    public Message updateMessageText(Message message) {
        Message existingmessage = getMessageById(message.getMessage_id());
        if ((existingmessage != null) && isMessageValid(message)) {
            // messageDAO.setMessage_text(message.getMessage_text());
            messageDAO.updateMessageText(message.getMessage_id(), message.getPosted_by(), message.getMessage_text(),
                    message.getTime_posted_epoch());
            return messageDAO.getMessageById(message.getMessage_id());
        }
        return null;
    }

    /**
     * Retrieve all messages written by a particular user.
     *
     * @param accountId the ID of the user account.
     * @return a list of messages written by the user, or an empty list if no
     *         messages found.
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }

    /**
     * Check if a message is valid.
     *
     * @param message the message to validate.
     * @return true if the message is valid, false otherwise.
     */
    private boolean isMessageValid(Message message) {
        String messageText = message.getMessage_text();
        return !messageText.isBlank() && messageText.length() < 255;
    }
}
