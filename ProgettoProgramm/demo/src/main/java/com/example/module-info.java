module progetto.programmazione {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens progetto.programmazione to javafx.fxml;
    exports progetto.programmazione;
}
