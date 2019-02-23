package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Utilitaire {

	private static Utilitaire single_instance = null;

	private Utilitaire() {
	}

	public static Utilitaire Singleton() {
		if (single_instance == null)
			single_instance = new Utilitaire();
		return single_instance;
	}

	public Double distances_total_edges(ArrayList<Edge> best_edges) {
		Double res = 0.0;
		for (Edge edge : best_edges) {
			res += edge.distance();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Tree2D edgesToTree(ArrayList<Edge> edges, Point root) {
		ArrayList<Edge> remainder = new ArrayList<Edge>();
		ArrayList<Point> subTreeRoots = new ArrayList<Point>();
		Edge current;
		while (edges.size() != 0) {
			current = edges.remove(0);
			if (current.p.equals(root)) {
				subTreeRoots.add(current.q);
			} else {
				if (current.q.equals(root)) {
					subTreeRoots.add(current.p);
				} else {
					remainder.add(current);
				}
			}
		}

		ArrayList<Tree2D> subTrees = new ArrayList<Tree2D>();
		for (Point subTreeRoot : subTreeRoots)
			subTrees.add(edgesToTree((ArrayList<Edge>) remainder.clone(), subTreeRoot));

		return new Tree2D(root, subTrees);
	}

	public Point getKeyFromValue(HashMap<Point, Integer> map, int value) {
		for (Entry<Point, Integer> entry : map.entrySet()) {
			if (entry.getValue() == value)
				return entry.getKey();
		}
		return new Point(-1, -1);
	}

	public ArrayList<Edge> get_edges_extrems(ArrayList<Edge> edges, ArrayList<Point> hitPoints) {
		ArrayList<Edge> edgesAlgo = new ArrayList<>();
		ArrayList<Point> points = eclatement_edges(edges);
		for (Edge edge1 : edges) {

			int cp1 = compter(points, edge1.p);
			int cp2 = compter(points, edge1.q);

			if (hitPoints.get(0) != edge1.p && hitPoints.get(0) != edge1.q) {
				if (cp1 <= 1 || cp2 <= 1) {
					edgesAlgo.add(edge1);
				}
			}

		}

		return edgesAlgo;
	}

	public Point get_extrem_point(ArrayList<Edge> edges, ArrayList<Point> biggest_edge_pts) {
		Point extrem = null;
		ArrayList<Point> points = eclatement_edges(edges);
		for (Edge edge1 : edges) {

			int cp1 = compter(points, edge1.p);
			int cp2 = compter(points, edge1.q);

			if ((cp1 <= 1) && (biggest_edge_pts.contains(edge1.p))) {
				extrem = edge1.p;
			} else if ((cp2 <= 1) && (biggest_edge_pts.contains(edge1.q))) {
				extrem = edge1.q;
			}
		}

		return extrem;
	}

	public ArrayList<Point> eclatement_edges(ArrayList<Edge> edgesAlgo) {
		ArrayList<Point> pts = new ArrayList<>();
		for (Edge edge : edgesAlgo) {
			pts.add(edge.p);
			pts.add(edge.q);
		}
		return pts;
	}

	public int compter(ArrayList<Point> points, Point p) {
		int cpt = 0;
		for (Point point : points) {
			if (point.x == p.x && point.y == p.y) {
				cpt++;
			}
		}
		return cpt;
	}

}
