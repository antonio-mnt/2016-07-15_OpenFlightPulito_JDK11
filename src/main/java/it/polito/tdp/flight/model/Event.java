package it.polito.tdp.flight.model;

public class Event implements Comparable<Event> {
	
	
	enum EventType{
		PARTENZA,
		ARRIVO,
		TIME_OUT
	}
	
	private EventType type;
	private Integer persona;
	private Airport aeroporto;
	private Double tempo;
	
	public Event(EventType type, Integer persona, Airport aeroporto, Double tempo) {
		super();
		this.type = type;
		this.persona = persona;
		this.aeroporto = aeroporto;
		this.tempo = tempo;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public Integer getPersona() {
		return persona;
	}
	public void setPersona(Integer persona) {
		this.persona = persona;
	}
	public Airport getAeroporto() {
		return aeroporto;
	}
	public void setAeroporto(Airport aeroporto) {
		this.aeroporto = aeroporto;
	}
	public Double getTempo() {
		return tempo;
	}
	public void setTempo(Double tempo) {
		this.tempo = tempo;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((persona == null) ? 0 : persona.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (persona == null) {
			if (other.persona != null)
				return false;
		} else if (!persona.equals(other.persona))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Event [type=" + type + ", persona=" + persona + ", aeroporto=" + aeroporto + ", tempo=" + tempo + "]";
	}
	@Override
	public int compareTo(Event o) {
		return this.tempo.compareTo(o.tempo);
	}
	
	

}
