package sns.controller;

import sns.driver.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HistoryPanelController {

	private MainApp snsApp;
	private Stage historyStage;
	@FXML
	private Label historyLabel;
	
	public HistoryPanelController()
	{ }
	
	@FXML
	private void initialize()
	{ }
	
	public void setApp(MainApp app)
	{
		snsApp = app;
	}
	
	public void setStage(Stage stage)
	{
		historyStage = stage;
	}
	
	public void secondInitialize()
	{
		historyLabel.setText(snsApp.getHistory());
	}
	
	@FXML
	private void handleCloseButton()
	{
		historyStage.close();
	}
}
