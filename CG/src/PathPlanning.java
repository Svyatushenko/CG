import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PathPlanning {

	public int[] Dijkstra(int s, int n, double[][] a) {
		int[] path = new int[n];
		double[] d = new double[n];
		boolean[] was = new boolean[n];
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
			if (u == -1)
				break;
			was[u] = true;
			for (int i = 0; i < n; i++) {
				if (a[u][i] < Double.POSITIVE_INFINITY) {
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

	private int n;
	private double[][] a;
	private List<Segment> seg = new ArrayList<Segment>();

	public PathPlanning(List<Segment> segment) {
		seg = segment;
		n = seg.size();

		for (int i = 0; i < n; i++) {
			System.out.println(seg.get(i).GetFirstPoint().GetX() + " "
					+ seg.get(i).GetFirstPoint().GetY() + " "
					+ seg.get(i).GetSecondPoint().GetX() + " "
					+ seg.get(i).GetSecondPoint().GetY() + " "
					+ seg.get(i).GetPlygon());
		}

		a = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		int start = 0;
		for (int i = 1; i < n - 2; i++) {
			if (seg.get(i).GetPlygon() != seg.get(i - 1).GetPlygon()) {
				double tmp = seg.get(i - 1).GetFirstPoint()
						.Dist(seg.get(start).GetFirstPoint());
				a[i - 1][start] = tmp;
				a[start][i - 1] = tmp;
				start = i;
			} else {
				double tmp = seg.get(i - 1).GetFirstPoint()
						.Dist(seg.get(i).GetFirstPoint());
				a[i][i - 1] = tmp;
				a[i - 1][i] = tmp;
			}
		}
		double tmp = seg.get(n - 3).GetFirstPoint()
				.Dist(seg.get(start).GetFirstPoint());
		a[n - 3][start] = tmp;
		a[start][n - 3] = tmp;

		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				for (int l = 0; l < n - 2; l++) {
					tmp = seg.get(l).NotCross(seg.get(i).GetFirstPoint(),
							seg.get(j).GetFirstPoint());
					a[i][j] = tmp;
					a[j][i] = tmp;
					if (tmp == Double.POSITIVE_INFINITY)
						break;
					boolean checkPoint = Segment.CheckPoint(seg.get(i).GetFirstPoint(),
							seg.get(j).GetFirstPoint(), seg.get(l));
					if (checkPoint) {
						a[i][j] = Double.POSITIVE_INFINITY;
						a[j][i] = Double.POSITIVE_INFINITY;
						break;
					}
				}
			}
		}
		start = 0;
		boolean inside;
		for (int i = 0; i < n - 2; i++) {
				for (int j = i + 1; j < n; j++) {
					inside = Segment.Inside(seg.get(i).GetFirstPoint().GetIn(), seg.get(i), seg
							.get(j).GetFirstPoint());
					if (inside) {
						a[i][j] = Double.POSITIVE_INFINITY;
						a[j][i] = Double.POSITIVE_INFINITY;
					} 
					
				}
		}
	}

	public List<Segment.Point> Calculation() {
		int[] p = Dijkstra(n - 2, n, a);
		int[] path = new int[n];
		int q = n - 1;
		int j = 0;
		while (q != n - 2) {
			path[j] = q;
			q = p[q];
			j++;
		}
		path[j] = n - 2;
		List<Segment.Point> ans = new ArrayList<Segment.Point>();
		for (int i = 0; i <= j; i++) {
			ans.add(new Segment.Point(seg.get(path[i]).GetFirstPoint()));
		}
		return ans;
	}

	static PrintWriter out;

	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File("VisibilityGraph.in"));
			List<Segment> segments = new ArrayList<Segment>();
			List<Segment> polygon = new ArrayList<Segment>();
			int k = 0;
			while (in.hasNext()) {
				int Ax = in.nextInt();
				int Ay = in.nextInt();
				int Bx = in.nextInt();
				int By = in.nextInt();
				int pol = in.nextInt();
				if (polygon.size() > 0) {
					if (pol != polygon.get(k - 1).GetPlygon()) {
						polygon = Segment.SQ(polygon);
						segments.addAll(polygon);
						polygon.clear();
						k = 0;
					}
				}
				polygon.add(new Segment(new Segment.Point(Ax, Ay),
						new Segment.Point(Bx, By), pol));
				k++;
				// segments.add(new Segment(new Segment.Point(Ax, Ay),
				// new Segment.Point(Bx, By), pol));
			}
//			polygon = Segment.SQ(polygon);
			segments.addAll(polygon);
			PathPlanning res = new PathPlanning(segments);
			List<Segment.Point> point = res.Calculation();
			int n = point.size();
			out = new PrintWriter("VisibilityGraph.out");
			for (int j = 0; j < n; j++) {
				out.println(point.get(j).GetX() + " " + point.get(j).GetY());
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			out.close();
		}
	}
}
