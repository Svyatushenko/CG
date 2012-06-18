import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;

public class Graphic extends Canvas {

	private static final long serialVersionUID = 1L;
	private int lastX, lastY, firstX, firstY;
	private int ex, ey, c, q;
	private boolean first = true;
	private boolean newPolygon = false;
	private boolean finalDrow = false;
	private boolean ready = false;
	public List<Integer> polNum = new ArrayList<Integer>();
	public List<Segment> segment = new ArrayList<Segment>();

	public Graphic() {
		super();
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (first) {
					lastX = e.getX();
					lastY = e.getY();
					firstX = lastX;
					firstY = lastY;
					first = false;
				} else {
					ex = lastX;
					ey = lastY;
					lastX = e.getX();
					lastY = e.getY();
					repaint();
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					newPolygon = true;
					repaint();
				}
				if (e.getKeyChar() == '\n') {
					finalDrow = true;
					first = false;
					// repaint();
				}
			}
		});
	}

	public void update(Graphics g) {
		if (finalDrow) {
			if (ready) {
				g.clearRect(0, 0, getWidth(), getHeight());
				segment.remove(segment.size() - 1);
				segment.remove(segment.size() - 1);
				int n = segment.size();
				g.setColor(Color.BLACK);
				for (int i = 0; i < n; i++) {
					g.drawLine(segment.get(i).GetFirstPoint().GetX(),
							segment.get(i).GetFirstPoint().GetY(), segment
									.get(i).GetSecondPoint().GetX(), segment
									.get(i).GetSecondPoint().GetY());
				}
				ready = false;
				c = 0;
			}
			g.drawOval(lastX, lastY, 2, 2);
			c++;
			segment.add(new Segment(new Segment.Point(lastX, lastY),
					new Segment.Point(), -c));
			if (c == 2) {
				PathPlanning res = new PathPlanning(segment);
				List<Segment.Point> point = res.Calculation();
				int n = point.size();
				g.setColor(Color.RED);
				for (int i = 1; i < n; i++) {
					g.drawLine(point.get(i - 1).GetX(),
							point.get(i - 1).GetY(), point.get(i).GetX(), point
									.get(i).GetY());
				}
				ready = true;
			}
		} else {
			if (newPolygon) {
				g.drawLine(firstX, firstY, lastX, lastY);
				newPolygon = false;
				first = true;
				segment.add(new Segment(new Segment.Point(lastX, lastY),
						new Segment.Point(firstX, firstY), q));
				q++;
			} else {
				g.drawLine(lastX, lastY, ex, ey);
				segment.add(new Segment(new Segment.Point(ex, ey),
						new Segment.Point(lastX, lastY), q));
			}
		}
	}

	public static void main(String s[]) {
		final Frame f = new Frame("Draw");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				f.dispose();
			}
		});
		f.setSize(1000, 700);

		final Canvas c = new Graphic();
		f.add(c);

		f.setVisible(true);
	} 
}
