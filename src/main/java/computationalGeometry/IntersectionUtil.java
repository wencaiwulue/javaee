package computationalGeometry;

/**
 * @author fengcaiwen
 * @since 8/6/2019
 */
public class IntersectionUtil {

    // 两点式直线公式：(x - x1) / (x2 - x1) = (y - y1) / (y2 - y1)
    // --> x(y2 - y1) + y(x1 - x2) + x1(y1 - y2) + y1(x2 - x1) = 0
    // A = y2-y1, B = x1-x2, C = x1(y1 - y2) + y1(x2 - x1)
    // (x,y) = d2/(d1+d2) * (x3,y3) + d1/(d1+d2) * (x4,y4)
    // d2/(d1+d2) = |A*x4 + B*y4 + C|/(|A*x4 + B*y4 + C|+|A*x3 + B*y3 + C|)
    // d1/(d1+d2) = |A*x3 + B*y3 + C|/(|A*x4 + B*y4 + C|+|A*x3 + B*y3 + C|)
    public static Point detectIntersection(Point a, Point b, Point c, Point d) {

        // whether intersect ?
        boolean intersect = (PointPosition.isLeft(a, b, c) ^ PointPosition.isLeft(a, b, d)) && (PointPosition.isLeft(c, d, a) ^ PointPosition.isLeft(c, d, b));
        if (!intersect) return null;

        double d2 = Math.abs((b.y - a.y) * d.x + (a.x - b.x) * d.y + a.x * (a.y - b.y) + a.y * (b.x - a.x));
        double d1 = Math.abs((b.y - a.y) * c.x + (a.x - b.x) * c.y + a.x * (a.y - b.y) + a.y * (b.x - a.x));
        double x = d2 / (d1 + d2) * c.x + d1 / (d1 + d2) * d.x;
        double y = d2 / (d1 + d2) * c.y + d1 / (d1 + d2) * d.y;
        return new Point(x, y);
    }
}