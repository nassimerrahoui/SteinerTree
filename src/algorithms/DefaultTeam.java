package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultTeam {

	public ArrayList<Object> calculShortestPaths(ArrayList<Point> points, int edgeThreshold) {
		return Algo.Singleton().FloydWarshall(points, edgeThreshold);
	}

	public Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		ArrayList<Object> path_dist = calculShortestPaths(points, edgeThreshold);
		int[][] paths = (int[][]) path_dist.get(0);
		double[][] dist = (double[][]) path_dist.get(1);
		HashMap<Point, Integer> mapPointIndice = new HashMap<>();

		for (int i = 0; i < points.size(); i++) {
			mapPointIndice.put(points.get(i), i);
		}

		ArrayList<Edge> kruskhaled = Algo.Singleton().Kruskal(hitPoints, dist, mapPointIndice);
		ArrayList<Edge> best_edges = Algo.Singleton().replongeKruskalIntoGraph(kruskhaled, paths, mapPointIndice);

		return Utilitaire.Singleton().edgesToTree(best_edges, best_edges.get(0).p);

	}

	public Tree2D calculSteinerBudget(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		ArrayList<Object> path_dist = calculShortestPaths(points, edgeThreshold);
		int[][] paths = (int[][]) path_dist.get(0);
		double[][] dist = (double[][]) path_dist.get(1);

		HashMap<Point, Integer> mapPointIndice = new HashMap<>();

		/** On creer une map associant un point a son indice  **/
		for (int i = 0; i < points.size(); i++) {
			mapPointIndice.put(points.get(i), i);
		}

		/** On effectue Kruskal sur les hitspoints **/
		ArrayList<Edge> kruskhaled = Algo.Singleton().Kruskal(hitPoints, dist, mapPointIndice);
		
		/** On replonge l arbre (sa liste edges) dans le graph **/
		ArrayList<Edge> best_edges = Algo.Singleton().replongeKruskalIntoGraph(kruskhaled, paths, mapPointIndice);

		/** on enleve les points les plus eloignes **/
		@SuppressWarnings("unchecked")
		ArrayList<Point> hitpointsBudget = (ArrayList<Point>) hitPoints.clone();
		while (Double.compare(Utilitaire.Singleton().distances_total_edges(best_edges), 1664) > 0) {

			ArrayList<Edge> extrem_edges = Utilitaire.Singleton().get_edges_extrems(kruskhaled, hitPoints);

			Edge biggest_edge = extrem_edges.get(0);
			for (Edge e : extrem_edges) {
				if (Double.compare(biggest_edge.distance(), e.distance()) < 0) {
					biggest_edge = e;
				}
			}

			ArrayList<Edge> big = new ArrayList<>();
			big.add(biggest_edge);

			ArrayList<Point> biggest_edge_pts = Utilitaire.Singleton().eclatement_edges(big);
			Point extrem = Utilitaire.Singleton().get_extrem_point(kruskhaled, biggest_edge_pts);

			hitpointsBudget.remove(extrem);

			kruskhaled = Algo.Singleton().Kruskal(hitpointsBudget, dist, mapPointIndice);
			best_edges = Algo.Singleton().replongeKruskalIntoGraph(kruskhaled, paths, mapPointIndice);
		}

		/** on regarde les points qui peuvent etre rajouter **/
		for (Point point : hitPoints) {
			if(!hitpointsBudget.contains(point)) {
				hitpointsBudget.add(point);
				kruskhaled = Algo.Singleton().Kruskal(hitpointsBudget, dist, mapPointIndice);
				best_edges = Algo.Singleton().replongeKruskalIntoGraph(kruskhaled, paths, mapPointIndice);
				if(Double.compare(Utilitaire.Singleton().distances_total_edges(best_edges), Double.valueOf(1664)) > 0) {
					hitpointsBudget.remove(point);
					continue;
				} else {
					break;
				}
			}
		}
		

		/** On construit l arbre final **/
		kruskhaled = Algo.Singleton().Kruskal(hitpointsBudget, dist, mapPointIndice);
		best_edges = Algo.Singleton().replongeKruskalIntoGraph(kruskhaled, paths, mapPointIndice);

		return Utilitaire.Singleton().edgesToTree(best_edges, best_edges.get(0).p);
	}

}