package it.polito.tdp.crimes.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> percorsoMigliore;

	public Model() {
		this.dao = new EventsDao();
	}

	public void creaGrafo(String categoria, int mese) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// aggiunta vertici
		Graphs.addAllVertices(grafo, this.dao.getVertici(categoria, mese));

		// aggiunta archi
		List<Adiacenza> adiacenze = dao.getAdiacenza(categoria, mese);
		for (Adiacenza a : adiacenze) {
			if (grafo.containsVertex(a.getId1()) && grafo.containsVertex(a.getId2())) {
				if (this.grafo.getEdge(a.getId1(), a.getId2()) == null) {
					Graphs.addEdgeWithVertices(grafo, a.getId1(), a.getId2(), a.getPeso());
				}
			}
		}

		System.out.println("Grafo creato");
		System.out.println("# Vertici: " + grafo.vertexSet().size());
		System.out.println("# Archi: " + grafo.edgeSet().size());

	}

	public List<Adiacenza> getArchi() {

		double pesoMedio = 0.0;

		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoMedio += grafo.getEdgeWeight(e);
		}

		pesoMedio = pesoMedio / this.grafo.edgeSet().size();

		List<Adiacenza> result = new LinkedList<>();

		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e) > pesoMedio) {
				Adiacenza a = new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e),
						this.grafo.getEdgeWeight(e));
				result.add(a);
			}
		}

		return result;
	}

	public List<String> trovaPercorso(String sorgente, String destinazione) {
		this.percorsoMigliore = new LinkedList<>();
		List<String> parziale = new LinkedList<>();
		parziale.add(sorgente);
		cerca(destinazione, parziale);
		return this.percorsoMigliore;
	}

	private void cerca(String destinazione, List<String> parziale) {
		// caso terminale
		if (parziale.get(parziale.size() - 1).equals(destinazione)) {
			if (parziale.size() > this.percorsoMigliore.size()) {
				this.percorsoMigliore = new LinkedList<>(parziale);
			}
			return;
		}

		// altrimenti scorro vicini ultimo inserito e provo ad aggiungerli uno ad uno
		for (String vicino : Graphs.neighborListOf(grafo, parziale.get(parziale.size() - 1))) {
			if (!parziale.contains(vicino)) {
				parziale.add(vicino);
				cerca(destinazione, parziale);
				parziale.remove(parziale.size() - 1);

			}
		}

	}
	
	public List<String> getCategorie(){
		return dao.getCategorie();
	}
}
