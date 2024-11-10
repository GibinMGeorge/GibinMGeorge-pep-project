package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

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
        boolean isLoggedIn = accountService.login(account.getUsername(), account.getPassword());
        if (isLoggedIn) {
            ctx.status(200).json(account);
        } else {
            ctx.status(401).json("");
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
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200).json("");
        }
    }

    private void handleDeleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        boolean isDeleted = messageService.deleteMessage(messageId);
        if (isDeleted) {
            ctx.status(200); // No content
        } else {
            ctx.status(404).json("Message not found.");
        }
    }

    private void handleUpdateMessageText(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newText = ctx.body();
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            ctx.status(400).json("Invalid message text.");
            return;
        }

        Message updatedMessage = messageService.updateMessageText(messageId, newText);
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(404).json("Message not found.");
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
