module com.check.files.checkfiles {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.check.files.checkfiles to javafx.fxml;
    exports com.check.files.checkfiles;
}