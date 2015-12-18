package ar.manumaanu.renamer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;

import ar.manumaanu.database.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Controller {

	private FileRenamer fileRenamer;
	private Database db;
	private DirectoryChooser directoryChooser;
	private double mouseX;
	private double mouseY;

	/* BEGIN FXML ATTRIBS */

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private GridPane gridPane;

	@FXML
	private Label labelDirectory;

	@FXML
	private Label labelFile;

	@FXML
	private Label labelName;

	@FXML
	private TextField textDirectory;

	@FXML
	private TextField textFile;

	@FXML
	private TextField textName;

	@FXML
	private Button buttonDirectory;

	@FXML
	private Button buttonRename;

	@FXML
	private Button buttonMinus;

	@FXML
	private Button buttonExit;

	@FXML
	private Button buttonHelp;

	@FXML
	private CheckBox checkBoxUseDatabase;

	@FXML
	private StackPane stackPaneCross;

	@FXML
	private StackPane stackPaneMinus;

	@FXML
	private StackPane stackPaneHelp;

	@FXML
	private HBox hBoxWindowButtons;

	/* END FXML ATTRIBS */

	/**
	 * Post: Initializes the controller class. This method is automatically
	 * called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		directoryChooser = new DirectoryChooser();
		AppData appfolder = new AppData(this.getClass().getPackage().getName());
		db = new Database(appfolder.getOurAppDataFolder() + "/rename.db");
		db.createTable();

	}

	/*
	 * 
	 * Begin Component Events
	 * 
	 */

	@FXML
	private void anchorPaneOnDragDetected(MouseEvent mouseEvent) {
		anchorPane.getScene().getWindow().setX(mouseEvent.getScreenX() + mouseX);
		anchorPane.getScene().getWindow().setY(mouseEvent.getScreenY() + mouseY);
	}

	@FXML
	private void anchorPaneOnMousePressed(MouseEvent mouseEvent) {

		mouseX = anchorPane.getScene().getWindow().getX() - mouseEvent.getScreenX();
		mouseY = anchorPane.getScene().getWindow().getY() - mouseEvent.getScreenY();
	}

	@FXML
	private void buttonDirectoryOnAction() {

		File selectedDirectory = directoryChooser.showDialog(new Stage());

		if (selectedDirectory != null) {
			fileRenamer = new FileRenamer(selectedDirectory);
			textDirectory.setText(this.fileRenamer.getDirectory().getAbsolutePath());
		}

	}

	@FXML
	private void buttonRenameOnAction() {

		if (fileRenamer == null || textDirectory.getText().isEmpty()) {
			alertError("ERROR: No directory selected");
		} else if (textFile.getText().trim().isEmpty() && !checkBoxUseDatabase.isSelected()) {
			alertError("ERROR: No current fileName selected");
		} else if (textName.getText().trim().isEmpty() && !checkBoxUseDatabase.isSelected()) {
			alertError("ERROR: No desired name selected");
		} else {

			if (checkBoxUseDatabase.isSelected()) {

				try {
					ResultSet resultSet = this.db.getResultSet();

					while (resultSet.next()) {

						fileRenamer.renameFiles(resultSet.getString("rename"), resultSet.getString("serie"));

					}

					resultSet.close();
					this.db.closeConnection();

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error");
				}

			}

			if (fileRenamer.renameFiles(textFile.getText(), textName.getText()) > 0) {
				this.db.insert(1, textFile.getText(), textName.getText());
				this.db.optimize();
			}
		}
	}

	private void alertError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("fileRENAMER: Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// TODO a better way to open the link without awt
	@FXML
	private void buttonHelpOnAction() {

		try {
			Desktop.getDesktop().browse(new URI("https://github.com/MaanuVazquez/FileRenamer#usage"));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	private void buttonMinusOnAction() {
		Stage stage = (Stage) gridPane.getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	private void buttonExitOnAction() {
		Stage stage = (Stage) gridPane.getScene().getWindow();
		stage.close();

	}

}
