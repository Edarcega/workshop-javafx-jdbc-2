package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	// dependencia para o serviço que busca os departamentos
	private DepartmentService service;

	// referencias para os componentes da tela

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId; // Tipo da entidade e tipo da coluna

	@FXML
	private TableColumn<Department, String> tableColumnNome;

	@FXML
	private Button btNew;

	private ObservableList<Department> obslist;

	@FXML
	private void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);// pegar refenencia para o Stage atual
		createDialogForm("/gui/DepartmentForm.fxml", parentStage);// para para criar a janela
	}

	// principio solid de inversão de controle
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	// Metodo para iniciar componentes na tela
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));

		// macete para a tablema acompanhar a janela

		Stage stage = (Stage) Main.getMainScene().getWindow(); // pegar a janela
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // bind = ligar
	}

	// Metodo responsavel por acessar o serviço
	// Carregar os departamentos e enviar para observablelist
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.fidAll();
		obslist = FXCollections.observableArrayList(list); // Instancia o observable list pegando os dados da lista
		tableViewDepartment.setItems(obslist);
	}
	
	// Janela de diálogo (modal)
	private void createDialogForm(String absoluteName,Stage parentStage) {
		// logica para abrir a janela de formulário
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));// Pegar a view
			Pane pane = loader.load();// Carregar a view
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entrada de dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); // Trava dimensionamento da janela
			dialogStage.initOwner(parentStage); // Stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
