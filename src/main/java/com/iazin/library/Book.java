package com.iazin.library;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class Book {
    private StringProperty isdn;
    private StringProperty author;
    private IntegerProperty year;
    private StringProperty genre;
    private StringProperty name;
    private StringProperty description;
    private int id;

    public Book() {
        isdn = new SimpleStringProperty();
        author = new SimpleStringProperty();
        year = new SimpleIntegerProperty();
        genre = new SimpleStringProperty();
        name = new SimpleStringProperty();
        description = new SimpleStringProperty();
    }

    public String getIsdn() {
        return isdn.get();
    }

    public void setIsdn(String isdn) {
        this.isdn.set(isdn);
    }

    public StringProperty isdnProperty() {
        return isdn;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public int getYear() {
        return year.get();
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}