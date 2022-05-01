package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

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
	TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	TableColumn<Department, Department> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Department> obslist;

	@FXML
	private void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);// pegar refenencia para o Stage atual
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);// para para criar a janela
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
		initEditButtons();
		initRemoveButtons();
	}

	// Janela de diálogo (modal)
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		// logica para abrir a janela de formulário
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));// Pegar a view
			Pane pane = loader.load();// Carregar a view

			DepartmentFormController controller = loader.getController();// Pegar a referência do controller do
																			// formulario
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this);// Inscreve o objeto para receber evento
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entrada de dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); // Trava dimensionamento da janela
			dialogStage.initOwner(parentStage); // Stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		// optional é um objto que carrega outro objeto dentro dele, podendo estar presente ou não
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	
	}

}
