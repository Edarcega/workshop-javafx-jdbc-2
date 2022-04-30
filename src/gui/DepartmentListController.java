package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	// referencias para os componentes da tela

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId; // Tipo da entidade e tipo da coluna

	@FXML
	private TableColumn<Department, String> tableColumnNome;

	@FXML
	private Button btNew;

	@FXML
	private void onBtNewAction() {
		System.out.println("onBtNewAction");
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

}
