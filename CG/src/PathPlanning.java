import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class PathPlanning {
	
	public int[] Dijkstra(int s, int n, long[][] a) {
		int[] path = new int[n];
		long[] d = new long[n];
		boolean[] was = new boolean[n];
		for (int i = 0; i < n; i++) {
			d[i] = Long.MAX_VALUE;
			path[i] = -1;
		}
		d[s] = 0;
		path[s] = s;
		while (true) {
			int u = -1;
			long min = Long.MAX_VALUE;
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
				if ((a[u][i] < Long.MAX_VALUE) && (d[u] < Long.MAX_VALUE)) {
					long relax = d[u] + a[u][i] + (long) (2*Math.sqrt((double)d[u]*a[u][i]));
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
	private long[][] a;
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

		a = new long[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = Long.MAX_VALUE;
			}
		}
		int start = 0;
		for (int i = 1; i < n - 2; i++) {
			if (seg.get(i).GetPlygon() != seg.get(i - 1).GetPlygon()) {
				long tmp = seg.get(i - 1).GetFirstPoint()
						.Dist(seg.get(start).GetFirstPoint());
				a[i - 1][start] = tmp;
				a[start][i - 1] = tmp;
				start = i;
			} else {
				long tmp = seg.get(i - 1).GetFirstPoint()
						.Dist(seg.get(i).GetFirstPoint());
				a[i][i - 1] = tmp;
				a[i - 1][i] = tmp;
			}
		}
		long tmp = seg.get(n - 3).GetFirstPoint()
				.Dist(seg.get(start).GetFirstPoint());
		a[n - 3][start] = tmp;
		a[start][n - 3] = tmp;

		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (seg.get(i).GetPlygon() != seg.get(j).GetPlygon()) {
					for (int l = 0; l < n - 2; l++) {
						tmp = seg.get(l).NotCross(seg.get(i).GetFirstPoint(),
								seg.get(j).GetFirstPoint());
						a[i][j] = tmp;
						a[j][i] = tmp;
						if (tmp == Long.MAX_VALUE)
							break;
					}
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
/*
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File("VisibilityGraph.in"));
			int n = in.nextInt();
			List<Segment> segments = new ArrayList<Segment>();
			for (int i = 0; i < n; i++) {
				int Ax = in.nextInt();
				int Ay = in.nextInt();
				int Bx = in.nextInt();
				int By = in.nextInt();
				int pol = in.nextInt();
				segments.add(new Segment(new Segment.Point(Ax, Ay),
						new Segment.Point(Bx, By), pol));
			}
			PathPlanning res = new PathPlanning(segments);
			List<Segment.Point> point = res.Calculation();
			n = point.size();
			for (int i = 0; i < n; i++) {
				System.out.println(point.get(i).GetX() + " "
						+ point.get(i).GetY());
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	} */
}
