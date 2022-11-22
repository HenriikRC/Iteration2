module com.example.semesterproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.semesterproject to javafx.fxml;
    exports com.example.semesterproject;
}