package it.polito.tdp.flight;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller del turno A --> modificare per turno B

public class FXMLController {
	
	private Model model;
	private boolean flag = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtDistanzaInput;

    @FXML
    private TextField txtPasseggeriInput;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	double distanza;
    	
    	try {
    	    		
    	    distanza = Double.parseDouble(this.txtDistanzaInput.getText());
    	    		
    	    }catch(NumberFormatException ne) {
    	    	this.txtResult.setText("Formato distanza errato");
    	    	return;
    	    }
    	
    	this.model.creaGrafo(distanza);
    	
    	this.txtResult.setText("Grafo creato!\n#Vertici: "+this.model.getNumeroVertici()+"\n#Archi: "+this.model.getNumeroArchi()+"\n");
    	
    	if(this.model.componenteConnessa()==true) {
    		this.txtResult.appendText("Si è possibile\n");
    	}else {
    		this.txtResult.appendText("No non è possibile\n");
    	}
    	
    	Airport lontano = this.model.trovaLontano();
    	
    	if(lontano==null) {
    		this.txtResult.appendText("L'aeroporto di Fiumicino non è presente nel Grafo\n");
    	}else {
    		this.txtResult.appendText(lontano+"\n");
    	}
    	
    	this.flag=true;

    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	int numero;
    	
    	try {
    	    		
    	    numero = Integer.parseInt(this.txtPasseggeriInput.getText());
    	    		
    	    }catch(NumberFormatException ne) {
    	    	this.txtResult.setText("Formato numero passeggeri errato");
    	    	return;
    	    }
    	
    	
    	if(this.flag==false) {
    		this.txtResult.setText("Creare prima il grafo");
    		return;
    	}
    	
    	Map<Airport,Integer> map = new HashMap<>(this.model.simula(numero));
    	
    	this.txtResult.clear();
    	
    	for(Airport a: map.keySet()) {
    		if(map.get(a)>0) {
    			this.txtResult.appendText(a+"   "+map.get(a)+"\n");
    		}
    	}
    	

    }

    @FXML
    void initialize() {
        assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Flight.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
