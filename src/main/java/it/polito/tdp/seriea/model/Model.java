package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	
	private SerieADAO dao;
	private List<String> squadre;
	private Map<Season,Integer> puntiPerStagione;
	private List<Season> season;
	private Map<Integer, Season> idMap;
	private List<Partita> partite;
	private List<Season> vertici;
	private Season ultimaStagione;
	
	private SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge> grafo;
	
	private Season annataVincente;
	private int sommaPesi;
	
	private Map<Season,Season> stagioniConsecutive;
	private Map<Season, Integer> percorso;
	private List<Season> best;
	
	
	public Model() {
		this.dao = new SerieADAO();
		this.squadre = new ArrayList<>(this.dao.listTeams());
		this.season = new ArrayList<>(this.dao.listAllSeasons());
		this.idMap = new TreeMap<Integer, Season>();
		for(Season s: this.season) {
			this.idMap.put(s.getSeason(), s);
		}
	}
	
	
	public void calcolaCampionati(String squadra) {
		
		this.puntiPerStagione = new TreeMap<>();
		this.partite = new ArrayList<>(this.dao.listPartite(squadra));
		this.vertici = new ArrayList<>(this.dao.listVertici(squadra));
		
		
		Integer puntiIniziali = 0;
		for(Season s: this.vertici) {
			this.puntiPerStagione.put(s, puntiIniziali);
		}
		
		for(Season s: this.vertici) {
			for(Partita p: this.partite) {
				if(s.getSeason()==p.getAnno()) {
				if(p.getRisultato().equals("D")) {
					this.puntiPerStagione.put(s, this.puntiPerStagione.get(s)+1);
				}else {
					
					if(p.getSquadraDiCasa().equals(squadra)) {
						if(p.getRisultato().equals("H")) {
							this.puntiPerStagione.put(s, this.puntiPerStagione.get(s)+3);
					}
					
				}else {
					if(p.getRisultato().equals("A")) {
						this.puntiPerStagione.put(s, this.puntiPerStagione.get(s)+3);
					}
				}
				}
			}
			
		}
		}
		
	}


	public List<String> getSquadre() {
		return squadre;
	}


	public Map<Season, Integer> getPuntiPerStagione() {
		return puntiPerStagione;
	}
	
	
	public void creaGrafo() {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		for(Season s1: this.vertici) {
			for(Season s2: this.vertici) {
				if(!s1.equals(s2)) {
					int peso = this.puntiPerStagione.get(s1)-this.puntiPerStagione.get(s2);
					if(peso>0) {
						Graphs.addEdgeWithVertices(this.grafo, s2, s1, peso);
					}
				}
			}
		}
		
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public void calcolaAnnata() {
		
		this.annataVincente = this.vertici.get(0);
		this.sommaPesi = 0;
		List<Season> uscenti;
		List<Season> entranti;
		
		for(Season s: this.grafo.vertexSet()) {
			uscenti = new ArrayList<>(Graphs.successorListOf(this.grafo, s));
			entranti = new ArrayList<>(Graphs.predecessorListOf(this.grafo, s));
			int somma = calcolaSomma(s,uscenti,entranti);
			
			if(somma>this.sommaPesi) {
				this.sommaPesi = somma;
				this.annataVincente = s;
			}
		}
		
	}


	private int calcolaSomma(Season s, List<Season> uscenti, List<Season> entranti) {
		
		int sommaEntranti = 0;
		int sommaUscenti = 0;
		
		for(Season sea: uscenti) {
			sommaUscenti+= this.grafo.getEdgeWeight(this.grafo.getEdge(s, sea));
		}
		
		for(Season sea: entranti) {
			sommaEntranti+= this.grafo.getEdgeWeight(this.grafo.getEdge(sea, s));
		}
		
		
		
		return sommaEntranti-sommaUscenti;
	}


	public Season getAnnataVincente() {
		return annataVincente;
	}


	public int getSommaPesi() {
		return sommaPesi;
	}
	
	
	public void trovaPercorso() {
		
		this.stagioniConsecutive = new TreeMap<>();
		
		this.best = new ArrayList<>();
		
		if(this.vertici.size()==1) {
			this.best = new ArrayList<>(this.vertici);
			return;
		}
		
		for(int i  = 1; i<this.vertici.size(); i++) {
			this.stagioniConsecutive.put(this.vertici.get(i-1), this.vertici.get(i));
			
		}
		
		System.out.println(this.stagioniConsecutive+"\n");
		
		this.percorso = new TreeMap<>();
		this.ultimaStagione = this.vertici.get(this.vertici.size()-1);
		
		for(Season s: this.vertici) {
			List<Season> parziale = new ArrayList<>();
			parziale.add(s);
			ricorsione(parziale);
		}
		
	}


	private void ricorsione(List<Season> parziale) {
		
		System.out.println(parziale+"\n");
		
		Season ultimo = parziale.get(parziale.size()-1);
		
		if(ultimo.equals(this.ultimaStagione)){
			if(parziale.size()>this.best.size()) {
				this.best = new ArrayList<>(parziale);
			}
			return;
		}
		
		if(parziale.size()>1) {
			if(!this.stagioniConsecutive.get(parziale.get(parziale.size()-2)).equals(ultimo)) {
				return;
			}
		}
		
		if(parziale.size()>this.best.size()) {
			this.best = new ArrayList<>(parziale);
		}
		
		
		
		List<Season> uscenti = new ArrayList<>(Graphs.successorListOf(this.grafo, ultimo));
		
		for(Season s: uscenti) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				ricorsione(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
		
	}
	
	public void riempiPercorso(){
		
		for(Season s: this.best) {
			this.percorso.put(s, this.puntiPerStagione.get(s));
		}
		
	}
	
	public Map<Season,Integer> getPercorso(){
		return this.percorso;
	}
	
	
	

}
