import java.util.List;

public class Segment {
	static public class Point {
		private int x;
		private int y;
		private Segment in;

		public Point(int a, int b) {
			x = a;
			y = b;
		}

		public Point() {
			x = 0;
			y = 0;
		}

		public Point(Point p) {
			x = p.x;
			y = p.y;
		}

		public int GetX() {
			return x;
		}

		public int GetY() {
			return y;
		}
		
		public Segment GetIn() {
			return in;
		}

		public double Dist(Point V) {
			return Math.sqrt(((double) V.x - x) * ((double) V.x - x)
					+ ((double) V.y - y) * ((double) V.y - y));
		}
	}

	private Point A;
	private Point B;
	private int polygon;

	public Segment(Point a, Point b, int c) {
		A = a;
		B = b;
		polygon = c;
	}

	public Segment() {
		A = new Point();
		B = new Point();
		polygon = 0;
	}

	public void SetFirstPoint(int a, int b) {
		A = new Point(a, b);
	}

	public void SetFirstPoint(Point a) {
		A = a;
	}

	public void SetSecondPoint(int a, int b) {
		B = new Point(a, b);
	}

	public void SetSecondPoint(Point b) {
		B = b;
	}

	public void SetPolygon(int a) {
		polygon = a;
	}

	public Point GetFirstPoint() {
		return A;
	}

	public Point GetSecondPoint() {
		return B;
	}

	public int GetPlygon() {
		return polygon;
	}

	public static boolean Inside(Segment a, Segment b, Point V) {
		long vm = VectMult(a.B, a.A, b.A, b.B);
		if (vm > 0) {
			if (VectMult(a.B, a.A, a.B, V) > 0
					&& VectMult(a.B, V, b.A, b.B) > 0)
				return true;
		} else {
			if (!(VectMult(a.B, a.A, a.B, V) < 0 && VectMult(a.B, V, b.A, b.B) < 0)
					&& !((VectMult(a.B, a.A, a.B, V) == 0 && VectMult(a.B, V,
							b.A, b.B) < 0))
					&& !((VectMult(a.B, a.A, a.B, V) < 0 && VectMult(a.B, V,
							b.A, b.B) == 0)))
				return true;
		}
		return false;
	}

	public double NotCross(Point V, Point W) {
		if (VectMult(A, B, A, W) * VectMult(A, B, A, V) < 0
				&& VectMult(V, W, V, A) * VectMult(V, W, V, B) < 0) {
			return Double.POSITIVE_INFINITY;
		}
		return V.Dist(W);
	}
	
	public static boolean CheckPoint (Point A, Point B, Segment c) {
		if (VectMult(A, c.A, c.A, B) == 0) {
			if (Inside(c.A.in, c, B)) {
				return true;
			}
		}
		return false;
	}

	private static long VectMult(Point A, Point B, Point V, Point W) {
		long x1 = B.x - A.x;
		long x2 = W.x - V.x;
		long y1 = B.y - A.y;
		long y2 = W.y - V.y;
		return x1 * y2 - x2 * y1;
	}

	public static List<Segment> SQ(List<Segment> a) {
		long s = 0;
		int n = a.size();
		Point zero = new Point();
		for (int i = 0; i < n; i++) {
			s += VectMult(zero, a.get(i).A, zero, a.get(i).B);
		}
		if (s > 0) {
			for (int i = 0; i < n; i++) {
				Point tmp = new Point();
				tmp = a.get(i).A;
				a.get(i).A = a.get(i).B;
				a.get(i).B = tmp;
			}
			for (int i = 0; i < n/2; i++) {
				Segment tmp = a.get(i);
				a.set(i, a.get(n - i - 1));
				a.set(n - i - 1, tmp);
			}
		}
		for (int i = 1; i < n; i++) {
			a.get(i).A.in = a.get(i - 1);
		}
		a.get(0).A.in = a.get(n - 1);
		return a;
	}
}
