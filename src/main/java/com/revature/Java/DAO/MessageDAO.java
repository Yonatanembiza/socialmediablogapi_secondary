package com.revature.Java.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.Java.Model.Message;
import com.revature.Java.Util.ConnectionUtil;

public class MessageDAO {
    /**
     * Retrieve all messages from the message table.
     *
     * @return all messages
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Retrieve a specific message using its message ID.
     *
     * @param messageId a message ID.
     * @return the message object with the specified ID, or null if not found
     */
    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Insert a new message record into the database.
     *
     * @param message an object representing a message. The message object does not
     *                contain a message ID.
     * @return the newly inserted message object with the generated message ID, or
     *         null if not successful
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if (pkeyResultSet.next()) {
                    int generated_message_id = pkeyResultSet.getInt(1);
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),
                            message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Delete the message identified by the message ID.
     *
     * @param messageId a message ID.
     */
    public void deleteMessage(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update the message text identified by the message ID.
     *
     * @param messageId   a message ID.
     * @param updatedText the updated message text.
     */
    public void updateMessageText(int messageId, int posted_by, String updatedText, Long time_posted_epoch) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text = ?, posted_by = ?, time_posted_epoch = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, updatedText);
            preparedStatement.setInt(2, posted_by);
            preparedStatement.setLong(3, time_posted_epoch);
            preparedStatement.setInt(4, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieve all messages written by a particular user.
     *
     * @param accountId the ID of the user account.
     * @return a list of messages written by the user, or an empty list if no
     *         messages found.
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
