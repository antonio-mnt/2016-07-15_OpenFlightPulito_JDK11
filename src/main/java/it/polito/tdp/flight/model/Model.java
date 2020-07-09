package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;


public class Model {
	
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private FlightDAO dao;
	private List<Airport> aeroporti;
	private Map<Integer,Airport> idMap;
	private List<Route> rotte;
	private List<Airport> connessi;
	private Simulator sim;
	
	
	public Model() {
		this.dao = new FlightDAO();
	}
	
	public void creaGrafo(double distanza) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.aeroporti = new ArrayList<>(this.dao.getAllAirports());
		
		this.idMap = new HashMap<>();
		
		for(Airport a: this.aeroporti) {
			this.idMap.put(a.getAirportId(), a);
		}
		
		Graphs.addAllVertices(this.grafo, this.aeroporti);
		
		this.rotte = new ArrayList<>(this.dao.getAllRoutes());
		
		for(Route r: this.rotte) {
			
			Airport a1 = this.idMap.get(r.getSourceAirportId());
			Airport a2 = this.idMap.get(r.getDestinationAirportId());
			
			if(a1!=null && a2!=null && !a1.equals(a2)) {
				
				double distanzaReale = this.getDistanza(a1, a2);
				
				if(distanzaReale<distanza) {
					double peso = (distanzaReale/800);
					
					Graphs.addEdge(this.grafo, a1, a2, peso);
					
				}
			}
			
		}
		
		
	}
	
	public double getDistanza(Airport a1, Airport a2) {
		
		double longitudine1 = a1.getLongitude();
		double latitudine1 = a1.getLatitude();
		LatLng posizione1 = new LatLng(latitudine1,longitudine1);
		
		double longitudine2 = a2.getLongitude();
		double latitudine2 = a2.getLatitude();
		LatLng posizione2 = new LatLng(latitudine2,longitudine2);
		
		double distanzaReale  = LatLngTool.distance(posizione1, posizione2, LengthUnit.KILOMETER);
		
		return distanzaReale;
		
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
		
		
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public boolean componenteConnessa() {
		
		List<Airport> aer = new ArrayList<>();
		this.connessi = new ArrayList<>();
	
		for(Airport a: this.aeroporti) {
			if(Graphs.neighborListOf(this.grafo, a).size()==0) {
				aer.add(a);
			}else {
				this.connessi.add(a);
			}
		}
		
		ConnectivityInspector<Airport,DefaultWeightedEdge> graf = new ConnectivityInspector<>(this.grafo);
		
		if(graf.connectedSets().size()==aer.size()+1) {
			return true;
		}else {
			return false;
		}
	
		
	}
	
	public Airport trovaLontano() {
		
		Airport tempA=null;
		Airport tempV=null;
		double distanza = 0;
		
		for(Airport a: this.grafo.vertexSet()) {
			if(a.getName().equals("Fiumicino")) {
				tempA = a;
			}
		}
		
		if (tempA==null) {
			return null;
		}
		
		List<Airport> vicini = new ArrayList<>(Graphs.successorListOf(this.grafo, tempA));
		
		for(Airport v: vicini) {
			
			double distanzaReale = this.getDistanza(tempA, v);
			
			if(distanzaReale>distanza) {
				distanza = distanzaReale;
				tempV = v;
			}
			
		}
		
		
		return tempV;
		
	}
	
	public Map<Airport,Integer> simula(int numeroPasseggeri) {
		
		this.sim = new Simulator();
		
		this.sim.run(this.grafo, numeroPasseggeri, this.connessi);
		
		return this.sim.getPasseggeri();
	}
	

}
