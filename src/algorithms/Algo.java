package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Algo {

	private static Algo single_instance = null;

	private Algo() {
	}

	public static Algo Singleton() {
		if (single_instance == null)
			single_instance = new Algo();
		return single_instance;
	}

	public ArrayList<Object> FloydWarshall(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Object> res = new ArrayList<Object>();
		int[][] paths = new int[points.size()][points.size()];
		double[][] dist = new double[points.size()][points.size()];
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist.length; j++) {
				if (points.get(i).distance(points.get(j)) < edgeThreshold) {
					dist[i][j] = points.get(i).distance(points.get(j));
					paths[i][j] = j;
				} else {
					dist[i][j] = Double.POSITIVE_INFINITY;
					paths[i][j] = -1;
				}
			}
		}
		for (int k = 0; k < dist.length; k++) {
			for (int i = 0; i < dist.length; i++) {
				for (int j = 0; j < dist.length; j++) {
					if (dist[i][j] > dist[i][k] + dist[k][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
						paths[i][j] = paths[i][k];
					}
				}
			}
		}
		res.add(paths);
		res.add(dist);
		return res;
	}

	public ArrayList<Edge> Kruskal(ArrayList<Point> hitPoints, double[][] dist,
			HashMap<Point, Integer> mapPointIndice) {

		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Point p : hitPoints) {
			for (Point q : hitPoints) {
				if (p.equals(q) || contains(edges, p, q))
					continue;
				edges.add(new Edge(p, q));
			}
		}
		edges = sort(edges, dist, mapPointIndice);
		ArrayList<Edge> kruskal = new ArrayList<Edge>();
		Edge current;
		NameTag forest = new NameTag(hitPoints);
		while (edges.size() != 0) {
			current = edges.remove(0);
			if (forest.tag(current.p) != forest.tag(current.q)) {
				kruskal.add(current);
				forest.reTag(forest.tag(current.p), forest.tag(current.q));
			}
		}

		return kruskal;
	}

	private boolean contains(ArrayList<Edge> edges, Point p, Point q) {
		for (Edge e : edges) {
			if (e.p.equals(p) && e.q.equals(q) || e.p.equals(q) && e.q.equals(p))
				return true;
		}
		return false;
	}

	private ArrayList<Edge> sort(ArrayList<Edge> edges, double[][] dist, HashMap<Point, Integer> map) {

		if (edges.size() == 1) {

			return edges;
		}

		ArrayList<Edge> left = new ArrayList<Edge>();
		ArrayList<Edge> right = new ArrayList<Edge>();
		int n = edges.size();
		for (int i = 0; i < n / 2; i++) {
			left.add(edges.remove(0));
		}
		while (edges.size() != 0) {
			right.add(edges.remove(0));
		}
		left = sort(left, dist, map);
		right = sort(right, dist, map);

		ArrayList<Edge> result = new ArrayList<Edge>();
		while (left.size() != 0 || right.size() != 0) {
			if (left.size() == 0) {
				result.add(right.remove(0));
				continue;
			}
			if (right.size() == 0) {
				result.add(left.remove(0));
				continue;
			}
			if (dist[map.get(left.get(0).p)][map.get(left.get(0).q)] < dist[map.get(right.get(0).p)][map
					.get(right.get(0).q)])
				result.add(left.remove(0));
			else
				result.add(right.remove(0));
		}
		return result;
	}

	public ArrayList<Edge> replongeKruskalIntoGraph(ArrayList<Edge> kruskhaled, int[][] paths,
			HashMap<Point, Integer> mapPointIndice) {

		ArrayList<Edge> best_edges = new ArrayList<Edge>();

		for (Edge edge : kruskhaled) {
			Point p = edge.p;
			Point q = edge.q;

			ArrayList<Edge> edges_intermediaires = new ArrayList<>();

			int i = mapPointIndice.get(p);
			int j = mapPointIndice.get(q);

			if (paths[i][j] != j) {

				int k = paths[i][j];
				edges_intermediaires.add(new Edge(Utilitaire.Singleton().getKeyFromValue(mapPointIndice, i),
						Utilitaire.Singleton().getKeyFromValue(mapPointIndice, k)));
				while (k != j) {
					int k_moins = k;
					k = paths[k][j];
					edges_intermediaires.add(new Edge(Utilitaire.Singleton().getKeyFromValue(mapPointIndice, k_moins),
							Utilitaire.Singleton().getKeyFromValue(mapPointIndice, k)));

				}
				edges_intermediaires.add(new Edge(Utilitaire.Singleton().getKeyFromValue(mapPointIndice, k),
						Utilitaire.Singleton().getKeyFromValue(mapPointIndice, j)));
				best_edges.addAll(edges_intermediaires);

			} else {
				best_edges.add(edge);
			}
		}

		return best_edges;
	}

}