package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;
	private boolean flag = false;
	private boolean flag2 = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	
    	String squadra = this.boxSquadra.getValue();
    	
    	if(squadra==null) {
    		this.txtResult.setText("Selezionare una squadra");
    		return;
    	}
    	
    	this.model.calcolaCampionati(squadra);
    	this.txtResult.clear();
    	for(Season s: this.model.getPuntiPerStagione().keySet()) {
    		this.txtResult.appendText(s+" punti: "+this.model.getPuntiPerStagione().get(s)+"\n");
    	}
    	
    	this.flag = true;
    	this.flag2 = false;

    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	
    	if(flag==false) {
    		this.txtResult.setText("Premi prima il bottone Seleziona Squadra");
    		return;
    	}
    	
    	this.model.creaGrafo();
    	
    	this.txtResult.setText("Grafo creato!\nVertici: "+this.model.getNumeroVertici()+" Archi: "+this.model.getNumeroArchi()+"\n");
    	
    	this.model.calcolaAnnata();
    	
    	this.txtResult.appendText("Annata d'oro: "+this.model.getAnnataVincente()+"\nSomma pesi: "+this.model.getSommaPesi());
    	
    	this.flag2 = true;

    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    	
    	if(flag2==false) {
    		this.txtResult.setText("Premi prima il bottone Annata d'oro");
    		return;
    	}
    	
    	this.model.trovaPercorso();
    	this.model.riempiPercorso();
    	
    	this.txtResult.clear();
    	for(Season s: this.model.getPercorso().keySet()) {
    		this.txtResult.appendText(s+" peso: "+this.model.getPercorso().get(s)+"\n");
    	}

    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSquadra.getItems().addAll(this.model.getSquadre());
		this.flag = false;
		this.flag2 = false;
	}
}
