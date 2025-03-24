module com.ieu.ce216.group7.artifactmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ieu.ce216.group7.artifactmanager to javafx.fxml;
    exports com.ieu.ce216.group7.artifactmanager;
}