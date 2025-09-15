package ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's
 * face and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    public enum DialogType {
        USER, BOT, ERROR, WELCOME
    }

    private DialogBox(String text, Image img, DialogType type) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class
                    .getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        setupAvatar(img, type);
        applyStyle(type);
    }

    private void setupAvatar(Image img, DialogType type) {
        if (img != null && (type == DialogType.USER
                || type == DialogType.BOT)) {
            displayPicture.setImage(img);
            displayPicture.setPreserveRatio(true);
            displayPicture.setSmooth(true);

            // Create circular clip for avatar
            Circle clip = new Circle();
            clip.setCenterX(14);
            clip.setCenterY(14);
            clip.setRadius(14);
            displayPicture.setClip(clip);

            displayPicture.setVisible(true);
            displayPicture.setManaged(true);
        } else {
            // Hide avatar for error and welcome messages
            displayPicture.setVisible(false);
            displayPicture.setManaged(false);
        }
    }

    private void applyStyle(DialogType type) {
        // Clear existing style classes
        this.getStyleClass().clear();

        switch (type) {
            case USER:
                this.getStyleClass().add("user-dialog");
                break;
            case BOT:
                this.getStyleClass().add("bot-dialog");
                flip(); // Keep the flip for bot messages
                break;
            case ERROR:
                this.getStyleClass().add("error-dialog");
                flip(); // Errors appear from bot side
                break;
            case WELCOME:
                this.getStyleClass().add("welcome-dialog");
                break;
        }
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on
     * the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections
                .observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img, DialogType.USER);
    }

    public static DialogBox getDumpyDialog(String text, Image img) {
        return new DialogBox(text, img, DialogType.BOT);
    }

    public static DialogBox getErrorDialog(String text) {
        return new DialogBox(text, null, DialogType.ERROR);
    }

    public static DialogBox getWelcomeDialog(String text, Image img) {
        return new DialogBox(text, img, DialogType.WELCOME);
    }
}
