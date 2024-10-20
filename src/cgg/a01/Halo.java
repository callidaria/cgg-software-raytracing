package cgg.a01;

import java.util.ArrayList;
import tools.*;

public record Halo(ArrayList<Circle> circles,Vec2 center) implements Sampler {

	public Color getColor(Vec2 point) {
		Color result = new Color(0,0,0);
		for (Circle circle : circles) {
			if (!circle.coversPoint(point)) continue;

			double rot = circle.orientation(center,point);
			double influence = Math.max(Math.cos(rot),.0);
			result = result.add(circle.colour().mul(Math.cos(rot)));
		}
		return result;
	}
}
// TODO protodistance + colour addition doesnt work for some reason
