module de.unistuttgart.racoon.core {
    requires transitive mpw.framework.core;
    requires mpw.framework.utils;
    requires javafx.base;

    exports de.unistuttgart.racoon.facade;
    exports de.unistuttgart.racoon.racoon to de.unistuttgart.racoon.main;
    exports de.unistuttgart.racoon.viewmodel.impl to de.unistuttgart.racoon.ui;

    opens de.unistuttgart.racoon.racoon;
}