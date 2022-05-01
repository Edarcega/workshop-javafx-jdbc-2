package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	// Itens de controle de tela que correspende aos itens em tela

	@FXML
	private MenuItem menuItemVendendor;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemAbout;

	// Metodos para tratar ações de menu

	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		// aqui tem uma expressão lambda que vai receber parametro do tipo
		// departamentList controller
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/Sobre.fxml", x -> {
		});
	}

	/*
	 * Interface de inicialização do controlador
	 * https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/Initializable.html
	 * Chamado para inicializar um controlador depois que seu elemento raiz foi
	 * completamente processado
	 * 
	 * Parâmetros: location- O local usado para resolver caminhos relativos para o
	 * objeto raiz ou nulo se o local não for conhecido.
	 * 
	 * resources- Os recursos usados ​​para localizar o objeto raiz ou null se o
	 * objeto raiz não foi localizado.
	 * 
	 */
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}

	// Função para abrir outra tela
	// o synchronized garante que o processo não será interrompido
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene(); // pega a cena principal da classe main
			// O metodo getRoot pega o primeiro elemento da view
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // Pega a referência para o Vbox que
																					// está na janela principal

			Node mainMenu = mainVbox.getChildren().get(0); // Primeiro filho do Vbox da janela principal o Main Menu.
			mainVbox.getChildren().clear(); // Limpa todos os fihos do main Vbox
			mainVbox.getChildren().add(mainMenu); // Adiciona o main menu
			mainVbox.getChildren().addAll(newVBox.getChildren()); // Adiciona os filhos do new VOX (a janela que estiver
																	// abrindo que veio na referencia)
		
			// Comando para ativar a expresão lambda
			T controller = loader.getController(); // T Vai retornar o controlador do tipo que chamou o metodo/função
			initializingAction.accept(controller); // Estas duas linhas executam a função
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro loading view", e.getMessage(), AlertType.ERROR);
		} catch (IllegalStateException e) {
			Alerts.showAlert("IO Exception", "Erro loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
