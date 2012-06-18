public class Segment {
	static public class Point {
		private int x;
		private int y;

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

		public long Dist(Point V) {
			return (long) (V.x - x) * (long) (V.x - x) + (long) (V.y - y)
					* (long) (V.y - y); 
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

	public long NotCross(Point V, Point W) {
		if (((long) (A.x - B.x) * (long) (A.y - W.y) - (long) (A.x - W.x)
				* (long) (A.y - B.y))
				* ((long) (A.x - B.x) * (long) (A.y - V.y) - (long) (A.x - V.x)
						* (long) (A.y - B.y)) < 0
				&& ((long) (V.x - W.x) * (long) (V.y - B.y) - (long) (V.x - B.x)
						* (long) (V.y - W.y))
						* ((long) (V.x - W.x) * (long) (V.y - A.y) - (long) (V.x - A.x)
								* (long) (V.y - W.y)) < 0) {
			return Long.MAX_VALUE;
		} 
		return V.Dist(W);
	}
}
