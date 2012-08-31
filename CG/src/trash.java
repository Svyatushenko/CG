import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class trash {

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
			
			
			out = new PrintWriter("VisibilityGraph.out");
			int n = segments.size();
			for (int i = 0; i < n; i++) {
				out.println(segments.get(i).GetFirstPoint().GetX() + " "
						+ segments.get(i).GetFirstPoint().GetY() + " "
						+ segments.get(i).GetSecondPoint().GetX() + " "
						+ segments.get(i).GetSecondPoint().GetY() + " "
						+ segments.get(i).GetPlygon());
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			out.close();
		}
	}
}
