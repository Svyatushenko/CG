import java.io.*;
import java.util.*;

public class VisibilityGraph implements Runnable {

	public static void main(String[] args) {
		new Thread(new VisibilityGraph()).start();
	}

	BufferedReader in;
	PrintWriter out;
	StringTokenizer st;

	public void run() {
		try {
			in = new BufferedReader(new FileReader(new File(
					"VisibilityGraph.in")));
			out = new PrintWriter("VisibilityGraph.out");
			solve();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(111);
		} finally {
			out.close();
		}
	}

	String nextToken() {
		try {
			while (st == null || !st.hasMoreElements()) {
				st = new StringTokenizer(in.readLine());
			}
		} catch (Exception e) {
			return "";
		}
		return st.nextToken();
	}

	int nextInt() {
		return Integer.parseInt(nextToken());
	}


	public class Segment {

		public class Point {
			private int x;
			private int y;

			Point(int a, int b) {
				x = a;
				y = b;
			}

			public Point() {
				x = 0;
				y = 0;
			}
			
			public double Dist(Point V) {
				return (double) (V.x - x) * (double) (V.x - x) + (double) (V.y - y)
				* (double) (V.y - y);
			//	return Math.sqrt((V.x - x)*(V.x - x) + (V.y - y)*(V.y - y));
			}
		}
		
		private Point A;
		private Point B;
		private int polygon;
		
		Segment(Point a, Point b, int c) {
			A = a;
			B = b;
			polygon = c;
		}
		
		public Segment() {
			A = new Point();
			B = new Point();
			polygon = 0;
		}

		public void SetFirstPoint (int a, int b) {
			A = new Point(a, b);
		}
		
		public void SetFirstPoint (Point a) {
			A = a;
		}
		
		public void SetSecondPoint (int a, int b) {
			B = new Point(a, b);
		}
		
		public void SetSecondPoint (Point b) {
			B = b;
		}
		
		public void SetPolygon (int a) {
			polygon = a;
		}
		
		public Point GetFirstPoint () {
			return A;
		}
		
		public Point GetSecondPoint () {
			return B;
		}
		
		public int GetPlygon () {
			return polygon;
		}
		
		
		public double NotCross(Point V, Point W) {
			if (((double) (A.x - B.x) * (double) (A.y - W.y) - (double) (A.x - W.x)
					* (double) (A.y - B.y))
					* ((double) (A.x - B.x) * (double) (A.y - V.y) - (double) (A.x - V.x)
							* (double) (A.y - B.y)) < 0
					&& ((double) (V.x - W.x) * (double) (V.y - B.y) - (double) (V.x - B.x)
							* (double) (V.y - W.y))
							* ((double) (V.x - W.x) * (double) (V.y - A.y) - (double) (V.x - A.x)
									* (double) (V.y - W.y)) < 0) {
				return Double.POSITIVE_INFINITY;
			}
			
		/*	if (((A.x - B.x)*(A.y - W.y) - (A.x - W.x)*(A.y - B.y))*((A.x - B.x)*(A.y - V.y) - (A.x - V.x)*(A.y - B.y)) < 0 
					&& ((V.x - W.x)*(V.y - B.y) - (V.x - B.x)*(V.y - W.y))*((V.x - W.x)*(V.y - A.y) - (V.x - A.x)*(V.y - W.y)) < 0) {
				return Double.POSITIVE_INFINITY;
			} */
			return V.Dist(W);
		}
	}
	
	public int[] Dijkstra (int s, int n, double[][] a) {
		int [] path = new int[n];
		double [] d = new double[n];
		boolean [] was = new boolean[n];
		for (int i = 0; i < n; i++) {
			d[i] = Double.POSITIVE_INFINITY;
			path[i] = -1;
		}
		d[s] = 0;
		path[s] = s;
		while (true) {
			int u = -1; 
			double min = Double.POSITIVE_INFINITY;
			for (int i = 0; i < n; i++) {
				if ((!was[i]) && (d[i] < min)) {
					min = d[i];
					u = i;
				}
			}
			if (u == -1) break;
			was[u] = true;
			for (int i = 0; i < n; i++) {
				if (a[u][i] < Double.POSITIVE_INFINITY && a[u][i] > 0) {
					double relax = d[u] + a[u][i];
					if (d[i] > relax) {
						d[i] = relax;
						path[i] = u;
					}
				} 
			}
		}
		
		return path;
	}
	
	void solve() {
		int n = nextInt();
		Segment[] seg = new Segment[n];
		for (int i = 0; i < n; i++) {
			seg[i] = new Segment();
		}
		double a[][] = new double [n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		int k = 0;
		int start = 0;
		seg[k].SetFirstPoint(nextInt(), nextInt());
		seg[k].SetPolygon(nextInt());
		for (int i = 1; i < n; i++) {
			seg[k].SetSecondPoint(nextInt(), nextInt());
			int c = nextInt();
			if (c != seg[k].GetPlygon()) {
				seg[k].SetFirstPoint(seg[k].GetSecondPoint());
				seg[k].SetPolygon(c);
				//a[k][k-1] = Double.POSITIVE_INFINITY; a[k-1][k] = Double.POSITIVE_INFINITY;
				double tmp = seg[k-1].GetFirstPoint().Dist(seg[start].GetFirstPoint());
				a[k-1][start] = tmp; a[start][k-1] = tmp;
				start = k;
			} else {
				k++;
				seg[k].SetFirstPoint(seg[k - 1].GetSecondPoint());
				seg[k].SetPolygon(seg[k - 1].GetPlygon());
				double tmp = seg[k-1].GetFirstPoint().Dist(seg[k].GetFirstPoint());
				a[k][k-1] = tmp; a[k-1][k] = tmp;
			}
		}
		//a[k][k-1] = Double.POSITIVE_INFINITY; a[k-1][k] = Double.POSITIVE_INFINITY;
		double tmp = seg[k-1].GetFirstPoint().Dist(seg[start].GetFirstPoint());
		a[k-1][start] = tmp; a[start][k-1] = tmp;
		seg[k].SetFirstPoint(nextInt(), nextInt());
		seg[k].SetPolygon(-1);
		seg[k + 1].SetFirstPoint(nextInt(), nextInt());
		seg[k + 1].SetPolygon(-2);
		for (int i = 0; i < k + 2; i++) {
			for (int j = 0; j < k + 2 ; j++) {
				if (seg[i].GetPlygon() != seg[j].GetPlygon()) {
					for (int l = 0; l < k; l++) {
						double tmp1 = seg[l].NotCross(seg[i].GetFirstPoint(), seg[j].GetFirstPoint());  
						a[i][j] = tmp1;
						a[j][i] = tmp1;
						if (tmp1 == Double.POSITIVE_INFINITY) break;
					}
				}
			}
		}
		int[] p = Dijkstra(k, k + 2, a);
		int[] path = new int[k + 2];
		int q = k + 1;
		int j = 0;
		while (q != k) {
			path[j] = q;
			q = p[q];
			j++;
		}
		path[j] = k;
		for (int i = 0; i <= j; i++) {
			out.print(path[i] + " ");
		}
	}
}
