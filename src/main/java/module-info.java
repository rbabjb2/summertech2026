module com.summertech2026 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;

    opens com.summertech2026 to javafx.fxml;
    exports com.summertech2026;
}
