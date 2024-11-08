package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Define endpoints here
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

    private void handleUserRegistration(Context context) {
        // Call Service layer to handle registration
        // Validate input and create a new user if valid
    }

    private void handleUserLogin(Context context) {
        // Call Service layer to handle login
        // Check username and password against database
    }

    private void handleCreateMessage(Context context) {
        // Call Service layer to handle new message creation
        // Validate input and add message to database if valid
    }

    private void handleGetAllMessages(Context context) {
        // Retrieve all messages from the Service layer
    }

    private void handleGetMessageById(Context context) {
        // Retrieve a specific message by ID from the Service layer
    }

    private void handleDeleteMessage(Context context) {
        // Call Service layer to delete a message by ID
    }

    private void handleUpdateMessageText(Context context) {
        // Call Service layer to update message text by ID
    }

    private void handleGetMessagesByUserId(Context context) {
        // Retrieve all messages by a specific user ID from the Service layer
    }
}
