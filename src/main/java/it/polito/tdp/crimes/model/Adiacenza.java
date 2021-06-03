package it.polito.tdp.crimes.model;

public class Adiacenza {

	private String id1;
	private String id2;
	private double peso;

	public Adiacenza(String id1, String id2, double peso) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso = peso;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return id1 + " - " + id2 + " = " + peso+"\n";
	}

}
