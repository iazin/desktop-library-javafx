module com.iazin.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;

    opens com.iazin.library to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.iazin.library;
}