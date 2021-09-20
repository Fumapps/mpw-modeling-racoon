module de.unistuttgart.racoon.ui {
    requires transitive de.unistuttgart.racoon.core;
    requires mpw.framework.utils;

    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;

    exports de.unistuttgart.racoon.ui;
    opens de.unistuttgart.racoon.ui;
    opens fxml;
    opens css;
    opens images;
}