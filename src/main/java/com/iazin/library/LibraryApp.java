package com.iazin.library;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.List;

public class LibraryApp extends Application {

    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private TableView<Book> tableView;
    private Database database;

    @Override
    public void start(Stage primaryStage) {
        database = new Database();
        bookList = FXCollections.observableArrayList();
        bookList = database.loadBooks();
        tableView = new TableView<>();
        tableView.setItems(bookList);

        TableColumn<Book, String> isdnColumn = new TableColumn<>("ISDN");
        isdnColumn.setCellValueFactory(cellData -> cellData.getValue().isdnProperty());

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        TableColumn<Book, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());

        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());

        TableColumn<Book, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Book, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Book, String> detailsColumn = new TableColumn<>("More");
        detailsColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        detailsColumn.setCellFactory(column -> {
            return new TableCell<Book, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Button detailsButton = new Button("More");
                        detailsButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            showBookDetails(book);
                        });
                        setGraphic(detailsButton);
                        setText(null);
                    }
                }
            };
        });
        tableView.getColumns().add(detailsColumn);

        tableView.getColumns().addAll(isdnColumn, authorColumn, yearColumn, genreColumn, nameColumn, descriptionColumn);

        ToolBar toolBar = new ToolBar();
        Button resetSearchButton = new Button("Reset Search");
        resetSearchButton.setOnAction(event -> {
            tableView.setItems(bookList);
        });
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> searchBook());
        Button addBookButton = new Button("Add Book");
        addBookButton.setOnAction(event -> addBook());
        Button deleteBookButton = new Button("Delete Book");
        deleteBookButton.setOnAction(event -> deleteBook());
        Button editBookButton = new Button("Edit Book");
        editBookButton.setOnAction(event -> editBook());
        toolBar.getItems().addAll(resetSearchButton, searchButton, addBookButton, deleteBookButton, editBookButton);
        Button exportButton = new Button("Export JSON");
        exportButton.setOnAction(event -> exportToJson());
        toolBar.getItems().add(exportButton);
        Button importButton = new Button("Import JSON");
        importButton.setOnAction(event -> importFromJson());
        toolBar.getItems().add(importButton);

        VBox root = new VBox(10);
        root.getChildren().addAll(toolBar, tableView);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 700, 450);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void addBook() {
        Dialog<Book> addBookDialog = new Dialog<>();
        addBookDialog.setTitle("Add Book");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        Label isdnLabel = new Label("ISDN:");
        TextField isdnField = new TextField();
        form.addRow(0, isdnLabel, isdnField);

        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();
        form.addRow(1, authorLabel, authorField);

        Label yearLabel = new Label("Year:");
        TextField yearField = new TextField();
        form.addRow(2, yearLabel, yearField);

        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();
        form.addRow(3, genreLabel, genreField);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        form.addRow(4, nameLabel, nameField);

        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();
        form.addRow(5, descriptionLabel, descriptionField);

        addBookDialog.getDialogPane().setContent(form);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.YES);
        addBookDialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        addBookDialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Book book = new Book();
                book.setIsdn(isdnField.getText());
                book.setAuthor(authorField.getText());
                book.setYear(Integer.parseInt(yearField.getText()));
                book.setGenre(genreField.getText());
                book.setName(nameField.getText());
                book.setDescription(descriptionField.getText());
                database.saveBook(book);
                bookList.add(book);
                tableView.setItems(bookList);
            }
            return null;
        });
        addBookDialog.showAndWait();
    }

    private void showBookDetails(Book book) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Detailed information about the book");

        GridPane detailsPane = new GridPane();
        detailsPane.setHgap(10);
        detailsPane.setVgap(10);

        Label isdnLabel = new Label("ISDN:");
        Label isdnValue = new Label(book.getIsdn());
        detailsPane.addRow(0, isdnLabel, isdnValue);

        Label authorLabel = new Label("Author:");
        Label authorValue = new Label(book.getAuthor());
        detailsPane.addRow(1, authorLabel, authorValue);

        Label yearLabel = new Label("Year:");
        Label yearValue = new Label(String.valueOf(book.getYear()));
        detailsPane.addRow(2, yearLabel, yearValue);

        Label genreLabel = new Label("Genre:");
        Label genreValue = new Label(book.getGenre());
        detailsPane.addRow(3, genreLabel, genreValue);

        Label nameLabel = new Label("Name:");
        Label nameValue = new Label(book.getName());
        detailsPane.addRow(4, nameLabel, nameValue);

        Label descriptionLabel = new Label("Description:");
        Label descriptionValue = new Label(book.getDescription());
        detailsPane.addRow(5, descriptionLabel, descriptionValue);

        Scene detailsScene = new Scene(detailsPane, 600, 300);
        detailsStage.setScene(detailsScene);
        detailsStage.show();
    }


    private void deleteBook() {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            database.deleteBook(selectedBook);
            bookList.remove(selectedBook);
            tableView.setItems(bookList);
        }
    }

    private void editBook() {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Dialog<Book> editBookDialog = new Dialog<>();
            editBookDialog.setTitle("Edit Book");

            GridPane form = new GridPane();
            form.setHgap(10);
            form.setVgap(10);

            Label isdnLabel = new Label("ISDN:");
            TextField isdnField = new TextField(selectedBook.getIsdn());
            form.addRow(0, isdnLabel, isdnField);

            Label authorLabel = new Label("Author:");
            TextField authorField = new TextField(selectedBook.getAuthor());
            form.addRow(1, authorLabel, authorField);

            Label yearLabel = new Label("Year:");
            TextField yearField = new TextField(String.valueOf(selectedBook.getYear()));
            form.addRow(2, yearLabel, yearField);

            Label genreLabel = new Label("Genre:");
            TextField genreField = new TextField(selectedBook.getGenre());
            form.addRow(3, genreLabel, genreField);

            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField(selectedBook.getName());
            form.addRow(4, nameLabel, nameField);

            Label descriptionLabel = new Label("Description:");
            TextField descriptionField = new TextField(selectedBook.getDescription());
            form.addRow(5, descriptionLabel, descriptionField);

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.YES);
            editBookDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            editBookDialog.getDialogPane().setContent(form);

            editBookDialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    selectedBook.setIsdn(isdnField.getText());
                    selectedBook.setAuthor(authorField.getText());
                    selectedBook.setYear(Integer.parseInt(yearField.getText()));
                    selectedBook.setGenre(genreField.getText());
                    selectedBook.setName(nameField.getText());
                    selectedBook.setDescription(descriptionField.getText());
                    database.updateBook(selectedBook);
                    tableView.setItems(bookList);
                }
                return null;
            });

            editBookDialog.showAndWait();
        }
    }

    private void searchBook() {
        Dialog<String> searchDialog = new Dialog<>();
        searchDialog.setTitle("Search Book");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        form.addRow(0, searchLabel, searchField);

        Label fieldLabel = new Label("Field:");
        ComboBox<String> fieldComboBox = new ComboBox<>();
        fieldComboBox.getItems().addAll("ISDN", "Author", "Name", "Genre", "Description");
        fieldComboBox.getSelectionModel().select(0);
        form.addRow(1, fieldLabel, fieldComboBox);

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.YES);
        searchDialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        searchDialog.getDialogPane().setContent(form);

        searchDialog.setResultConverter(dialogButton -> {
            if (dialogButton == searchButtonType) {
                String searchQuery = searchField.getText();
                String selectedField = fieldComboBox.getSelectionModel().getSelectedItem();
                ObservableList<Book> searchResults = FXCollections.observableArrayList();
                for (Book book : bookList) {
                    switch (selectedField) {
                        case "ISDN":
                            if (book.getIsdn().contains(searchQuery)) {
                                searchResults.add(book);
                            }
                            break;
                        case "Author":
                            if (book.getAuthor().contains(searchQuery)) {
                                searchResults.add(book);
                            }
                            break;
                        case "Name":
                            if (book.getName().contains(searchQuery)) {
                                searchResults.add(book);
                            }
                            break;
                        case "Genre":
                            if (book.getGenre().contains(searchQuery)) {
                                searchResults.add(book);
                            }
                            break;
                        case "Description":
                            if (book.getDescription().contains(searchQuery)) {
                                searchResults.add(book);
                            }
                            break;
                    }
                }
                tableView.setItems(searchResults);
            }
            return null;
        });

        searchDialog.showAndWait();
    }

    private void exportToJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(bookList);
            File file = new File("books.json");
            mapper.writeValue(file, bookList);
            System.out.println("Data exported to books.json file");
        } catch (Exception e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }

    private void importFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("books.json");
            bookList.clear();
            bookList.addAll(mapper.readValue(file, new TypeReference<List<Book>>() {}));
            database.clearDatabase();
            for (Book book : bookList) {
                database.saveBook(book);
            }
            System.out.println("Data imported from books.json file");
        } catch (Exception e) {
            System.out.println("Error importing data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
