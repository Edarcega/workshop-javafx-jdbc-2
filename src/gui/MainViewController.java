package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {

	// Itens de controle de tela que correspende aos itens em tela

	@FXML
	private MenuItem menuItemVendendor;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemSobre;

	// Metodos para tratar ações de menu

	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("onMenuItemVendedorAction");
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println("onMenuItemDepartamentoAction");
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		System.out.println("onMenuItemDepartamentoAction");
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

}
