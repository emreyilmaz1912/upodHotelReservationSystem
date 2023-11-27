module com.emreyilmaz.upodhotelreservationsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;

    opens com.emreyilmaz.upodhotelreservationsystem to javafx.fxml;
    exports com.emreyilmaz.upodhotelreservationsystem;
    exports com.emreyilmaz.upodhotelreservationsystem.controllers.deletedItemsController;

    opens com.emreyilmaz.upodhotelreservationsystem.controllers.deletedItemsController to javafx.fxml;

    exports com.emreyilmaz.upodhotelreservationsystem.rooms;
    opens com.emreyilmaz.upodhotelreservationsystem.rooms to javafx.fxml;
    exports com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController;
    opens com.emreyilmaz.upodhotelreservationsystem.controllers.editItemsController to javafx.fxml;
    exports com.emreyilmaz.upodhotelreservationsystem.controllers.newItemsController;
    opens com.emreyilmaz.upodhotelreservationsystem.controllers.newItemsController to javafx.fxml;
    opens com.emreyilmaz.upodhotelreservationsystem.controllers to javafx.fxml;
    exports com.emreyilmaz.upodhotelreservationsystem.controllers;


}

