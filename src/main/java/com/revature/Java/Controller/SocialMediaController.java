package com.revature.Java.Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Java.Model.Account;
import com.revature.Java.Model.Message;
import com.revature.Java.Service.AccountService;
import com.revature.Java.Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    private final MessageService messageService;
    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    public SocialMediaController() {
        messageService = new MessageService();
        accountService = new AccountService();
        objectMapper = new ObjectMapper();
    }

    /*
     * initializes a Javalin instance,
     * sets up the routes for handling HTTP requests,
     * and returns the initialized Javalin instance.
     */
    public Javalin startAPI() {

        Javalin app = Javalin.create();

        // Register a new user
        app.post("/register", this::registerUserHandler);
        // User login
        app.post("/login", this::loginUserHandler);
        // Create a new message
        app.post("/messages", this::createMessageHandler);
        // Retrieve all messages
        app.get("/messages", this::getAllMessagesHandler);
        // Retrieve a message by message ID
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        // Retrieve all messages by account ID
        app.get("/accounts/{account_id}/messages", this::getMessageByUserIdHandler);
        // Delete message by message ID
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        // Update a message by message ID
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);

        return app;
    }

    private void createMessageHandler(Context ctx) {
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
            if (createdMessage != null) {
                // String response = objectMapper.writeValueAsString(createdMessage);
                ctx.json(createdMessage).status(200);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        if (messages != null) {
            ctx.json(messages).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    private void deleteMessageByIDHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);

        if (deletedMessage != null) {
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    private void updateMessageByIDHandler(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            Message updatedMessage = messageService.updateMessageText(message.getMessage_text(), message_id);
            if (updatedMessage != null) {
                ctx.json(updatedMessage).status(200);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    private void registerUserHandler(Context ctx) {
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            Account registeredAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
            if (registeredAccount != null) {
                ctx.json(registeredAccount).status(200);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    private void loginUserHandler(Context ctx) {
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            if (loggedInAccount != null) {
                ctx.json(loggedInAccount).status(200);
            } else {
                ctx.status(401);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    private void getMessageByUserIdHandler(Context ctx) {
        int postedby = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(postedby);
        if (messages != null) {
            ctx.json(messages).status(200);
        } else {
            ctx.status(200).result("");
        }
    }
}
