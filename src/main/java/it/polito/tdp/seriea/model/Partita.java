package it.polito.tdp.seriea.model;

public class Partita {
	
	private int anno;
	private String squadraDiCasa;
	private String squadraFuoriCasa;
	private String risultato;
	public Partita(int anno, String squadraDiCasa, String squadraFuoriCasa, String risultato) {
		super();
		this.anno = anno;
		this.squadraDiCasa = squadraDiCasa;
		this.squadraFuoriCasa = squadraFuoriCasa;
		this.risultato = risultato;
	}
	public int getAnno() {
		return anno;
	}
	public void setAnno(int anno) {
		this.anno = anno;
	}
	public String getSquadraDiCasa() {
		return squadraDiCasa;
	}
	public void setSquadraDiCasa(String squadraDiCasa) {
		this.squadraDiCasa = squadraDiCasa;
	}
	public String getSquadraFuoriCasa() {
		return squadraFuoriCasa;
	}
	public void setSquadraFuoriCasa(String squadraFuoriCasa) {
		this.squadraFuoriCasa = squadraFuoriCasa;
	}
	public String getRisultato() {
		return risultato;
	}
	public void setRisultato(String risultato) {
		this.risultato = risultato;
	}
	@Override
	public String toString() {
		return "Partita [anno=" + anno + ", squadraDiCasa=" + squadraDiCasa + ", squadraFuoriCasa=" + squadraFuoriCasa
				+ ", risultato=" + risultato + "]";
	}
	
	

}
