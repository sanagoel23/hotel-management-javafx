package projectsg;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Optional;

public class project extends Application {

    // ---------------- ROOM ----------------
    public static class Room {
        private final IntegerProperty roomNumber;
        private final StringProperty roomType;
        private final DoubleProperty pricePerDay;
        private final BooleanProperty available;

        public Room(int num, String type, double price, boolean avail) {
            roomNumber = new SimpleIntegerProperty(num);
            roomType = new SimpleStringProperty(type);
            pricePerDay = new SimpleDoubleProperty(price);
            available = new SimpleBooleanProperty(avail);
        }

        public int getRoomNumber() { return roomNumber.get(); }
        public String getRoomType() { return roomType.get(); }
        public double getPricePerDay() { return pricePerDay.get(); }
        public boolean isAvailable() { return available.get(); }

        public void setAvailable(boolean val) { available.set(val); }

        public IntegerProperty roomNumberProperty() { return roomNumber; }
        public StringProperty roomTypeProperty() { return roomType; }
        public DoubleProperty pricePerDayProperty() { return pricePerDay; }
        public BooleanProperty availableProperty() { return available; }
    }

    // ---------------- CUSTOMER ----------------
    public static class Customer {
        private final StringProperty name;
        private final StringProperty contact;
        private final IntegerProperty roomNumber;

        public Customer(String n, String c, int r) {
            name = new SimpleStringProperty(n);
            contact = new SimpleStringProperty(c);
            roomNumber = new SimpleIntegerProperty(r);
        }

        public String getName() { return name.get(); }
        public String getContact() { return contact.get(); }
        public int getRoomNumber() { return roomNumber.get(); }

        public StringProperty nameProperty() { return name; }
        public StringProperty contactProperty() { return contact; }
        public IntegerProperty roomNumberProperty() { return roomNumber; }
    }

    private final ObservableList<Room> rooms = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    private Label dashboard = new Label();

    @Override
    public void start(Stage stage) {

        // ---------------- DASHBOARD ----------------
        updateDashboard();

        // ---------------- TABLE ----------------
        TableView<Room> roomTable = new TableView<>(rooms);

        TableColumn<Room, Number> colNum = new TableColumn<>("Room No");
        colNum.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        TableColumn<Room, Number> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        TableColumn<Room, Boolean> colAvail = new TableColumn<>("Available");
        colAvail.setCellValueFactory(new PropertyValueFactory<>("available"));

        roomTable.getColumns().addAll(colNum, colType, colPrice, colAvail);

        // ---------------- SEARCH ----------------
        TextField search = new TextField();
        search.setPromptText("Search Room No");

        Button searchBtn = new Button("Search");

        searchBtn.setOnAction(e -> {
            String text = search.getText();
            if (text.isEmpty()) {
                roomTable.setItems(rooms);
                return;
            }

            ObservableList<Room> filtered = FXCollections.observableArrayList();
            for (Room r : rooms) {
                if (String.valueOf(r.getRoomNumber()).contains(text))
                    filtered.add(r);
            }
            roomTable.setItems(filtered);
        });

        // ---------------- SORT ----------------
        Button sortBtn = new Button("Sort by Price");
        sortBtn.setOnAction(e ->
                FXCollections.sort(rooms, Comparator.comparingDouble(Room::getPricePerDay))
        );

        // ---------------- ROOM FORM ----------------
        TextField rNum = new TextField();
        TextField rType = new TextField();
        TextField rPrice = new TextField();
        Button addRoom = new Button("Add Room");

        addRoom.setOnAction(e -> {
            try {
                int num = Integer.parseInt(rNum.getText());
                double price = Double.parseDouble(rPrice.getText());

                if (price <= 0) throw new Exception();

                rooms.add(new Room(num, rType.getText(), price, true));
                updateDashboard();

                rNum.clear(); rType.clear(); rPrice.clear();
            } catch (Exception ex) {
                show("Invalid Room Input");
            }
        });

        GridPane roomForm = new GridPane();
        roomForm.setHgap(10); roomForm.setVgap(10);

        roomForm.add(new Label("Room No:"), 0, 0);
        roomForm.add(rNum, 1, 0);

        roomForm.add(new Label("Type:"), 0, 1);
        roomForm.add(rType, 1, 1);

        roomForm.add(new Label("Price:"), 0, 2);
        roomForm.add(rPrice, 1, 2);

        roomForm.add(addRoom, 1, 3);

        // ---------------- CUSTOMER FORM ----------------
        TextField cName = new TextField();
        TextField cContact = new TextField();
        TextField cRoom = new TextField();

        Button book = new Button("Book");
        Button checkout = new Button("Checkout");

        book.setOnAction(e -> {
            try {
                int roomNum = Integer.parseInt(cRoom.getText());

                if (!cContact.getText().matches("\\d+"))
                    throw new Exception();

                Room room = rooms.stream()
                        .filter(r -> r.getRoomNumber() == roomNum)
                        .findFirst().orElse(null);

                if (room == null) show("Room not found");
                else if (!room.isAvailable()) show("Already booked");
                else {
                    room.setAvailable(false);
                    customers.add(new Customer(cName.getText(), cContact.getText(), roomNum));
                    updateDashboard();
                    show("Booked!");
                }

            } catch (Exception ex) {
                show("Invalid Customer Input");
            }
        });

        checkout.setOnAction(e -> {
            try {
                int roomNum = Integer.parseInt(cRoom.getText());

                Room room = rooms.stream()
                        .filter(r -> r.getRoomNumber() == roomNum)
                        .findFirst().orElse(null);

                if (room != null && !room.isAvailable()) {

                    TextInputDialog d = new TextInputDialog();
                    d.setHeaderText("Enter days");

                    Optional<String> res = d.showAndWait();
                    if (res.isPresent()) {
                        int days = Integer.parseInt(res.get());
                        double total = days * room.getPricePerDay();

                        show("Receipt\nRoom: " + roomNum +
                                "\nDays: " + days +
                                "\nTotal: ₹" + total);
                    }

                    room.setAvailable(true);
                    customers.removeIf(c -> c.getRoomNumber() == roomNum);
                    updateDashboard();

                } else show("Room not occupied");

            } catch (Exception ex) {
                show("Invalid Checkout");
            }
        });

        GridPane custForm = new GridPane();
        custForm.setHgap(10); custForm.setVgap(10);

        custForm.add(new Label("Name:"), 0, 0);
        custForm.add(cName, 1, 0);

        custForm.add(new Label("Contact:"), 0, 1);
        custForm.add(cContact, 1, 1);

        custForm.add(new Label("Room No:"), 0, 2);
        custForm.add(cRoom, 1, 2);

        custForm.add(book, 0, 3);
        custForm.add(checkout, 1, 3);

        // ---------------- DARK MODE ----------------
        Button dark = new Button("Toggle Dark Mode");

        VBox root = new VBox(10);
        dark.setOnAction(e -> {
            if (root.getStyle().isEmpty())
                root.setStyle("-fx-background-color:#2b2b2b; -fx-text-fill:white;");
            else root.setStyle("");
        });

        root.setPadding(new Insets(10));

        root.getChildren().addAll(
                dashboard,
                search, searchBtn, sortBtn,
                new Label("Room Management"),
                roomForm,
                new Label("Customer Booking"),
                custForm,
                dark,
                roomTable
        );

        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Hotel Management System");
        stage.show();
    }

    private void updateDashboard() {
        long available = rooms.stream().filter(Room::isAvailable).count();
        dashboard.setText("Total: " + rooms.size() +
                " | Available: " + available +
                " | Occupied: " + (rooms.size() - available));
    }

    private void show(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}