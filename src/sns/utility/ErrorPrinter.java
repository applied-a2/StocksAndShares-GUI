package sns.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class helps showing an error dialog/window (JavaFx version)
 * @author Thai Kha Le
 */
public class ErrorPrinter {
	
	public static void printError(Stage stage, String title, String header, String content)
	{
		Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
	}
}
