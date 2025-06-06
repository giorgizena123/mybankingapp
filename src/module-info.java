module com.yourpackage.mybankingapp
{
    requires javafx.controls;
    requires javafx.media;
    opens com.yourpackage.controller to javafx.fxml;
    exports com.yourpackage;
}