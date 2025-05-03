module com.ieu.ce216.group7.artifactmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.ieu.ce216.group7.artifactmanager to javafx.fxml;
    opens com.ieu.ce216.group7.artifactmanager.model to com.fasterxml.jackson.databind, javafx.base;
    exports com.ieu.ce216.group7.artifactmanager;

}
