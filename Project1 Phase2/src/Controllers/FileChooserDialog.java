package Controllers;

import java.io.File;
import java.util.Optional;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileChooserDialog {

    private String title;
    private String extensionFilter;

    public FileChooserDialog(String title, String extensionFilter) {
        this.title = title;
        this.extensionFilter = extensionFilter;
    }

    /**
     * Shows a save dialog and returns the selected file path.
     * 
     * @return The file path as a string, or null if canceled.
     */
    public Optional<String> showSaveDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (extensionFilter != null && !extensionFilter.isEmpty()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup Files", extensionFilter));
        }
        File file = fileChooser.showSaveDialog(Main.getStage());
        if (file != null) {
            return Optional.of(file.getAbsolutePath());
        }
        return Optional.empty();
    }

    /**
     * Shows an open dialog and returns the selected file path.
     * 
     * @return The file path as a string, or null if canceled.
     */
    public Optional<String> showOpenDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (extensionFilter != null && !extensionFilter.isEmpty()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup Files", extensionFilter));
        }
        File file = fileChooser.showOpenDialog(Main.getStage());
        if (file != null) {
            return Optional.of(file.getAbsolutePath());
        }
        return Optional.empty();
    }
}
