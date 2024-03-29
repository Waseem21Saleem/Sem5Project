package client;

import javafx.application.Application;
import javafx.stage.Stage;
import gui.MainFrameController;

/**
 * The main class for the client-side user interface.
 * This class extends the JavaFX Application class and serves as the entry point for the client application.
 */
public class ClientUI extends Application {
    /**
     * The instance of the ClientController for handling client-server communication.
     */
    public static ClientController chat; // only one instance

    /**
     * The main method to launch the JavaFX application.
     * @param args Command-line arguments.
     * @throws Exception If an error occurs during application startup.
     */
    public static void main(String args[]) throws Exception {
        launch(args);
    } // end main

    /**
     * This method is called when the application should stop.
     * It ensures a clean shutdown of the application.
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * This method is called when the JavaFX application starts.
     * It initializes the main frame of the application.
     * @param primaryStage The primary stage for the application.
     * @throws Exception If an error occurs during initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create and start the main frame controller
        MainFrameController aFrame = new MainFrameController();
        aFrame.start(primaryStage);
    }
}