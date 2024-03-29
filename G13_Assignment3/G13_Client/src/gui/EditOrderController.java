package gui;



import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Order;
import logic.User;

/**
 * This class controls the Edit Order interface.
 * It handles actions such as editing orders, deleting orders, and navigating back.
 * Implements the Initializable interface for FXML initialization.
 * <p>Author: [Mohammed Dukhi]</p>
 */
public class EditOrderController implements Initializable {

	/** The order being edited */
    private Order order;
    /** The user associated with the order */
    public static User user = ChatClient.user;

    @FXML
    private Label lblOrderNum, lblSave, lblError;

    @FXML
    private TextField txtVisitors, txtEmail, txtPhone, txtTime;

    @FXML
    private Button btnSave, btnBack, btnDeleteOrder;

    @FXML
    private DatePicker datepickerDate;

    @FXML
    private ComboBox<String> cmbPark, cmbOrder, cmbMonth, cmbDay, cmbTime;

    ObservableList<String> list;

    /**
     * Loads the details of the selected order into the form.
     * @param order The order to be loaded.
     */
    public void loadOrder(Order order) {
        this.order = order;

        this.cmbPark.setValue(order.getParkName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate date = LocalDate.parse(order.getDate(), formatter);

        this.datepickerDate.setValue(date);
        this.txtTime.setText(order.getTime());
        this.txtVisitors.setText(order.getAmountOfVisitors());
        this.txtEmail.setText(order.getEmail());
        this.txtPhone.setText(order.getTelephone());
    }

    /**
     * Deletes the selected order.
     */
    public void deleteOrder() {
        Message msg = new Message(Message.ActionType.DELETEORDER, order);
        ClientUI.chat.accept(msg);
        lblSave.setText(ChatClient.error);
        msg = new Message(Message.ActionType.ORDERSNUMBERS, user);
        ClientUI.chat.accept(msg);
        refreshComboBox();
    }

    /**
     * Sets the values in the order ComboBox.
     */
    public void setOrderComboBox() {
        this.cmbOrder.setValue("Order number");
        this.cmbOrder.setItems(FXCollections.observableArrayList(ChatClient.Orderslist));
        this.cmbPark.setItems(FXCollections.observableArrayList(ChatClient.parkNames));
        cmbOrder.setOnAction(event -> {
            String selectedValue = cmbOrder.getValue();
            if (!selectedValue.equals("Order number")) {
                Order order = new Order(selectedValue);
                Message msg = new Message(Message.ActionType.ORDERINFO, order);
                ClientUI.chat.accept(msg);
                loadOrder(ChatClient.order);
                lblSave.setText("");
            }
        });
    }

    /**
     * Refreshes the ComboBoxes.
     */
    public void refreshComboBox() {
        this.cmbOrder.setValue("Order number");
        this.cmbPark.setValue("");
        this.cmbOrder.setItems(FXCollections.observableArrayList(ChatClient.Orderslist));
        this.cmbPark.setItems(FXCollections.observableArrayList(ChatClient.parkNames));
        this.datepickerDate.setValue(null);
        this.txtTime.setText("");
        this.txtVisitors.setText("");
        this.txtEmail.setText("");
        this.txtPhone.setText("");
    }

    /**
     * Navigates back to the previous page.
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void goBackBtn(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Pane root = loader.load(getClass().getResource("/gui/VisitorHomePage.fxml").openStream());
        Scene scene = new Scene(root);
        primaryStage.setTitle("Visitor HomePage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Saves the changes made to the order.
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during saving.
     */
    public void getSaveBtn(ActionEvent event) throws Exception {
        String visitorType = "individual";
        String selectedOrder = cmbOrder.getValue();
        String selectedPark = cmbPark.getValue();
        String amountOfVisitors = txtVisitors.getText();
        String date = null;
        if (datepickerDate.getValue() != null)
            date = datepickerDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yy")).toString();
        if (checkInput()) {
            if (Integer.parseInt(amountOfVisitors) > 1)
                if (Integer.parseInt(amountOfVisitors) < 6 && (!user.getUserPermission().equals("GUIDE")))
                    visitorType = "family group";
                else
                    visitorType = "organized group";
            Order order = new Order(selectedPark, selectedOrder, user.getId(), visitorType, date, txtTime.getText(), amountOfVisitors, txtPhone.getText(), txtEmail.getText());
            double arr[] = VisitorHomePageController.calculatePrice(order);
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to reserve?");
            alert.setContentText("The price if you want to reserve now is ₪" + arr[1] + " you will save ₪" + (arr[0] - arr[1]) + " than going without reserving.\n The price if you want to pay now is ₪" + arr[2] + " you will save ₪" + (arr[0] - arr[2]) + " than going without reserving. ");
            ButtonType buttonPayLater = new ButtonType("Pay later");
            ButtonType buttonPayNow = new ButtonType("Pay now");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonPayLater, buttonPayNow, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeCancel) {
            } else {
                if (result.isPresent() && result.get() == buttonPayNow) {
                    order.setPayStatus("paid");
                    order.setTotalCost(String.valueOf(arr[2]));
                } else {
                    order.setPayStatus("not paid");
                    order.setTotalCost(String.valueOf(arr[1]));
                }
                order.setOrderStatus("processing");
                Message msg = new Message(Message.ActionType.UPDATEORDER, order);
                ClientUI.chat.accept(msg);
                if (!ChatClient.error.isEmpty()) {
                    lblError.setText(ChatClient.error);
                    lblError.setTextFill(Color.GREEN);
                } else {
                    ChatClient.openGUI.goToGUI(event, "/gui/WaitingList.fxml", "", "Waiting list");
                }
            }
        }
    }

    /**
     * Checks if the input fields are valid.
     * @return True if the input is valid, false otherwise.
     */
    public Boolean checkInput() {
        lblError.setText("");
        String selectedPark = cmbPark.getValue();
        String amountOfVisitors = txtVisitors.getText();
        String date = null;
        if (datepickerDate.getValue() != null)
            date = datepickerDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yy")).toString();
        if (selectedPark.equals("Select park") || amountOfVisitors.isEmpty() || date == null
                || txtTime.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty())
            lblError.setText("Missing information");
        else if (!amountOfVisitors.matches("[0-9]+") || Integer.parseInt(amountOfVisitors) < 1 || Integer.parseInt(amountOfVisitors) > 15)
            lblError.setText("Amount of visitors should be in the range of 1 to 15");
        else if (!VisitorHomePageController.isValidTime(date, txtTime.getText()))
            lblError.setText("Time must be between 08:00 or higher than current time to 16:00 ");
        else if (!VisitorHomePageController.isValidEmail(txtEmail.getText()))
            lblError.setText("Email is not in a correct format");
        else if (!txtPhone.getText().matches("[0-9]+") || txtPhone.getText().length() != 10)
            lblError.setText("Phone number must contain 10 digits");
        else if (Integer.parseInt(amountOfVisitors) > 5 && (!user.getUserPermission().equals("GUIDE")))
            lblError.setText("You can't make a reservation for more than 5 visitors");
        return lblError.getText().isEmpty();
    }

    /**
     * Initializes the controller and sets up the GUI components.
     * @param arg0 The location of the FXML file.
     * @param arg1 The resources to be used to localize the root object.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setOrderComboBox();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        datepickerDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(tomorrow) < 0 || date.compareTo(tomorrow.plusYears(1)) > 0);
            }
        });
    }
}