package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::handleUserRegistration);
        app.post("/login", this::handleUserLogin);
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessageText);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByUserId);

        return app;
    }

    private void handleUserRegistration(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        if (account.getUsername() == null || account.getUsername().isEmpty() ||
            account.getPassword() == null || account.getPassword().length() <= 4) {
            ctx.status(400).json("");
            return;
        }
    
        // Check for duplicate username
        Account createdAccount = accountService.createAccount(account);
        if (createdAccount == null) {
            ctx.status(400).json("");
            return;
        }
    
        ctx.status(200).json(createdAccount); // Successfully created account
    }
    

    private void handleUserLogin(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account isLoggedIn = accountService.login(account.getUsername());
        if (isLoggedIn == null || !isLoggedIn.getPassword().equals(account.getPassword())) {
            ctx.status(401);
        } else {
            ctx.status(200).json(isLoggedIn);
        }
    }

    private void handleCreateMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() ||
            message.getMessage_text().length() > 255) {
            ctx.status(400).json("");
            return;
        }

        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400).json("");
        }
    }

    private void handleGetAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void handleGetMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Optional<Message> message = messageService.getMessageById(messageId);
    
        if (message.isPresent()) {
            ctx.status(200).json(message.get());
        } else {
            ctx.status(200).json(""); 
        }
    }
    

    private void handleDeleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Optional<Message> message = messageService.getMessageById(messageId); // Fetch the message first
        boolean isDeleted = messageService.deleteMessage(messageId);
    
        if (isDeleted && message.isPresent()) {
            // Return JSON representation of the deleted message if it was found and deleted
            ctx.status(200).json(message.get());
        } else {
            // Return 200 with an empty body if the message was not found
            ctx.status(200).result("");
        }
    }
    

    private void handleUpdateMessageText(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        // Parse the JSON body as a Map and get the "message_text" field
        @SuppressWarnings("unchecked")
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String newText = body.get("message_text");
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            ctx.status(400).json("");
            return;
        }

        Message updatedMessage = messageService.updateMessageText(messageId, newText);
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400).json("");
        }
    }

    private void handleGetMessagesByUserId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userMessages = messageService.getMessagesByUserId(accountId);
        if (userMessages != null) {
            ctx.json(userMessages);
        } else {
            ctx.status(404).json("No messages found for this user.");
        }
    }
}
