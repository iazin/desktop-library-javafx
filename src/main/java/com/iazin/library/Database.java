package com.iazin.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Database {
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:books.db");
            createTable();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "isdn TEXT, " +
                    "author TEXT, " +
                    "year INTEGER, " +
                    "genre TEXT, " +
                    "name TEXT, " +
                    "description TEXT)");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void saveBook(Book book) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO books (isdn, author, year, genre, name, description) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, book.getIsdn());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getYear());
            statement.setString(4, book.getGenre());
            statement.setString(5, book.getName());
            statement.setString(6, book.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving book: " + e.getMessage());
        }
    }

    public ObservableList<Book> loadBooks() {
        ObservableList<Book> TemporarilyBookList = FXCollections.observableArrayList();
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM books")) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setIsdn(resultSet.getString("isdn"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("year"));
                book.setGenre(resultSet.getString("genre"));
                book.setName(resultSet.getString("name"));
                book.setDescription(resultSet.getString("description"));
                TemporarilyBookList.add(book);
            }
            return TemporarilyBookList;
        } catch (SQLException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return TemporarilyBookList;
    }

    public void deleteBook(Book book) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE id = ?")) {
            statement.setInt(1, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    public void updateBook(Book book) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE books SET isdn = ?, author = ?, year = ?, genre = ?, name = ?, description = ? WHERE id = ?")) {
            statement.setString(1, book.getIsdn());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getYear());
            statement.setString(4, book.getGenre());
            statement.setString(5, book.getName());
            statement.setString(6, book.getDescription());
            statement.setInt(7, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Book update error: " + e.getMessage());
        }
    }

    public void clearDatabase() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM books");
        } catch (SQLException e) {
            System.out.println("Error clearing database: " + e.getMessage());
        }
    }
}
