package com.revature.Java.Controller;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
/*public class SocialMediaController {
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     
    private void exampleHandler(Context context) {
        //context.json("sample text");
    }
    
}
*/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Java.DAO.AccountDAO;
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

    public Javalin startAPI() {

        Javalin app = Javalin.create();

        // Register a user
        app.post("/register", this::registerUserHandler);
        // User login
        app.post("/login", this::loginUserHandler);
        // Create a message
        app.post("/messages", this::createMessageHandler);
        // Retrieve all messages
        app.get("/messages", this::getAllMessagesHandler);
        // Retrieve a message by ID
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        // localhost:8080/accounts/{account_id}/messages.
        app.get("/accounts/{account_id}/messages", this::getMessageByUserIdHandler);
        // Delete message by ID
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        // Update a message
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);

        return app;
    }

    private void createMessageHandler(Context ctx) {
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
            if (createdMessage != null) {
                String response = objectMapper.writeValueAsString(createdMessage);
                ctx.json(response).status(200);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
        /*
         * try {
         * Message message = objectMapper.readValue(ctx.body(), Message.class);
         * 
         * // Set the message_id to 0 (omitted)
         * message.setMessage_id(0);
         * 
         * // Create the message and retrieve the inserted message with the generated
         * // message_id
         * Message createdMessage = messageService.createMessage(message);
         * 
         * if (createdMessage != null) {
         * String response = objectMapper.writeValueAsString(createdMessage);
         * // ctx.result(response).contentType("application/json").status(200);
         * ctx.co.json(response).status(200);
         * }
         * } catch (JsonProcessingException e) {
         * e.printStackTrace();
         * ctx.status(400);
         * }
         */

    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        // ctx.json(messages).status(200);
        if (messages != null) {
            ctx.json(messages).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        // ctx.json(message).status(200);
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
        /*
         * int messageId = Integer.parseInt(ctx.pathParam("message_id"));
         * Message deletedMessage = messageService.deleteMessage(messageId);
         * 
         * if (deletedMessage != null) {
         * // ctx.json(deletedMessage).status(200);
         * try {
         * String response = objectMapper.writeValueAsString(deletedMessage);
         * ctx.status(200).result(response);
         * } catch (Exception e) {
         * // TODO: handle exception
         * ctx.status(200).result("");
         * }
         * } else {
         * ctx.status(200).result("");
         * }
         */
    }

    private void updateMessageByIDHandler(Context ctx) {
        // int message_d = Integer.parseInt(ctx.pathParam("message_id"));
        // int message_text = Integer.parseInt(ctx.pathParam("message_id"));
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            Message updatedMessage = messageService.updateMessageText(message);
            if (updatedMessage != null) {
                String response = objectMapper.writeValueAsString(updatedMessage);
                // ctx.result(response).status(200);
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
            // This will omit the account_id
            // account.setAccount_id(0);
            Account registeredAccount = accountService.registerAccount(account.username, account.password);
            if (registeredAccount != null) {
                // String response = objectMapper.writeValueAsString(registeredAccount);
                // ctx.result(response).contentType("application/json").status(200);
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
            // String username = ctx.pathParam("username");
            // String password = ctx.pathParam("password");
            Account loggedInAccount = accountService.login(account.username, account.password);
            if (loggedInAccount != null) {
                // String response = objectMapper.writeValueAsString(loggedInAccount);
                // ctx.result(response).contentType("application/json").status(200);
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
        // ctx.json(message).status(200);
        if (messages != null) {
            ctx.json(messages).status(200);
        } else {
            ctx.status(200).result("");
        }
    }
}
