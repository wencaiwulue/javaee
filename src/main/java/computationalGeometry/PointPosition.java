package computationalGeometry;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * @author fengcaiwen
 * @since 7/15/2019
 */
public class PointPosition {

    /**
     * step:
     * 1, find the start point
     * 2, from s to each elements, find the next point t, make t's  right have no point
     */
    public static void run(Point[] s, int n) {
        int start = LTL(s, n);
        for (int i = 0; i < s.length; i++) {
            int counter = 0;
            for (int j = 0; j < s.length; j++)
                if (start != i && start != j && i != j)
                    if (isLeft(s[start], s[i], s[j])) counter++;

            if (counter == s.length - 1 - 1)
                s[i].isEdge = true;
        }

    }

    public static void grahamScan(Point[] s, int n) {
        int start = LTL(s, n);
        Point startPoint = s[start];
        Point[] points = sort(s, n);

        Stack<Point> ordered = new Stack<>();
        Stack<Point> unordered = new Stack<>();
        ordered.push(startPoint);
        ordered.push(points[0]);
        for (int i = 1; i < s.length; i++) {
            unordered.push(points[i]);
        }

        while (!unordered.empty()) {
            if (isLeft(ordered.get(1), ordered.get(0), ordered.get(0))) {
                ordered.push(unordered.pop());
            } else {
                ordered.pop();
            }
        }

    }

    /**
     * cosφ=A1A2+B1B2/[√(A1^2+B1^2)√(A2^2+B2^2)]
     */
    private static Point[] sort(Point[] s, int n) {
        // cos = a*
        return Arrays.stream(s).sorted(Comparator.comparing(o -> BigDecimal.valueOf(calDegree(o, Point.X)))).toArray(Point[]::new);
    }

    /**
     * ->  ->
     * a   b
     * cosα=ab/|a||b|=（x1y1+x2,y2）/(根号（x1^2+y1^2）根号（x2^2+y1^2）)
     */
    private static double calDegree(Point a, Point b) {
//        Math.cos();
        return (a.x * b.y + b.x * b.y) / Math.pow(Math.pow(Math.pow(a.x, 2) + Math.pow(a.y, 2), 0.5) * Math.pow(Math.pow(b.x, 2) * Math.pow(b.y, 2), 0.5), 0.5);
    }

    /**
     * find min y from s, if is a list, return min x of list
     */
    private static int LTL(Point[] s, int n) {
        int rank = 0;
        for (int i = 0; i < s.length; i++)
            if (s[rank].y > s[i].y || (s[rank].y == s[i].y && s[rank].x > s[i].x))
                rank = i;

        return rank;
    }

    /**
     * ->
     * a：(x1, y1), point A:(x2, y2)
     */
    private static boolean isLeft(double x1, double y1, double x2, double y2) {
        // a'=(-y1, x1) or (y1, -x1), return a' x A = (-y1, x1) x (x2, y2)
        return (-y1 * x2 + x1 * y2) > 0;
    }

    private static double area2(Point p, Point q, Point s) {
        return p.x * q.y - p.y * q.x + q.x * s.y - q.y * s.x + s.x * p.y - s.y * p.x;
    }

    /**
     * ->
     * pq and point s
     */
    public static boolean isLeft(Point p, Point q, Point s) {
        return isLeft(q.x - p.x, q.y - p.y, s.x - p.x, s.y - p.y);
    }

}
