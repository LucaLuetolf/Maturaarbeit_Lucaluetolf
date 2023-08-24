package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class schriftlicherTeil {

    public static void main(String[] args) {


        String test = "abc123";

        test.replaceAll("[A-Za-z]", "");


        int i = 4;

        if (i == 4) {
            i++;
        } else {
            System.out.println(i);
        }


        String email = "Max.Mustermann@gmail.com";

        if (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9._%+-]+\\.[A-Za-z]{2,}$") == true) {
            System.out.println("Folgende E-Mail ist gültig: " + email);
        } else {
            System.out.println("Folgende E-Mail ist ungültig: " + email);
        }

    }


    @FXML
    private TextField textfieldTest;
    String test;

    @FXML
    protected void onTestButtonClicked() {
        test = textfieldTest.getText();
    }


}