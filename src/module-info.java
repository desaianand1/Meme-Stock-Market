module CSSE333MemeStockMarket{
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.web;
    requires java.sql;
    requires java.sql.rowset;
    requires java.desktop;
    requires sqljdbc4;
    requires jasypt;
    requires com.jfoenix;
    exports project to javafx.graphics, javafx.fxml,javafx.controls;
    opens project to javafx.graphics, javafx.fxml,javafx.base,javafx.controls;
}