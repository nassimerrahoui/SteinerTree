package algorithms;

import java.awt.Point;

class Edge {
	protected Point p, q;

	protected Edge(Point p, Point q) {
		this.p = p;
		this.q = q;
	}

	protected double distance() {
		return p.distance(q);
	}
}
