package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.flight.model.Event.EventType;

public class Simulator {
	
	private PriorityQueue<Event> queue = new PriorityQueue<>();
	
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	
	private List<Event> attesa;
	
	private Map<Airport,Integer> passeggeri;

	public Map<Airport, Integer> getPasseggeri() {
		return passeggeri;
	}
	
	
	public void run(SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> g, int numeroPasseggeri, List<Airport> connessi) {
		
		this.passeggeri = new HashMap<>();
		
		this.grafo = g;
		
		this.attesa = new ArrayList<>();
		
		for(Airport a: connessi) {
			this.passeggeri.put(a, 0);
		}
		
		for(int i = 0; i< numeroPasseggeri; i++) {
			Airport a = this.estraiAeroporto(connessi);
			Event ev = new Event (EventType.PARTENZA,i,a,6.0);
			this.attesa.add(ev);
			this.passeggeri.put(a, this.passeggeri.get(a)+1);
		}
		
		
		this.queue.clear();
		
		this.queue.add(new Event (EventType.TIME_OUT,null,null,7.0));
		
		while(!this.queue.isEmpty()) {  
		    Event e = this.queue.poll();
		    processEvent(e);
		}
		
		
		
	}
	
	
	private void processEvent(Event e) {
		switch(e.getType()) {
		
		case TIME_OUT:
			
			for(Event ev: this.attesa) {
				ev.setTempo(e.getTempo());
				this.queue.add(ev);
			}
			
			this.attesa.clear();
			
			double tempo = e.getTempo();
			
			
			if(tempo>46) {
				
			}else {
				if(tempo==23) {
					this.queue.add(new Event(EventType.TIME_OUT,null,null,tempo+8));
				}else {
					this.queue.add(new Event(EventType.TIME_OUT,null,null,tempo+2));
				}
			}
			
			
			break;
		
		case PARTENZA:
			
			Airport partenza = e.getAeroporto();
			
			List<Airport> vicini = new ArrayList<>(Graphs.successorListOf(this.grafo, partenza));
			
			if(vicini.size()!=0) {
				
				Airport destinazione = this.estraiAeroporto(vicini);
				
				double tempistica  = this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, destinazione));
				
				this.queue.add(new Event (EventType.ARRIVO,e.getPersona(),destinazione,e.getTempo()+tempistica));
				
				this.passeggeri.put(partenza, this.passeggeri.get(partenza)-1);
			}
			
			
			
			break;
			
		case ARRIVO:
			
			Event event = new Event(EventType.PARTENZA,e.getPersona(),e.getAeroporto(),e.getTempo());
			this.attesa.add(event);
			
			this.passeggeri.put(e.getAeroporto(), this.passeggeri.get(e.getAeroporto())+1);
			
			
			break;
		
		}
		
	}


	public Airport estraiAeroporto(List<Airport> connessi) {
		
		int i = (int) (Math.random()*connessi.size());
		
		return connessi.get(i);
		
	}
	
	
	

}
