package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// Instanciar o loader pegando o caminho da view
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load(); // Trocar para o maior item da hierarquia da view
			
			// deixar o Scrool pane ajustado a janela
			scrollPane.setFitToHeight(true);// altura
			scrollPane.setFitToWidth(true);// largura
			
			Scene mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene); // Palco da cena
			primaryStage.setTitle("Sample JavaFX application");// Título da cena
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
