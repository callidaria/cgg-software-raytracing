package cgg.a01;

import java.util.ArrayList;
import tools.*;

public record NinetiesFlair(ArrayList<Circle> circles,Vec2 center) implements Sampler {

	public Color getColor(Vec2 point) {
		Color result = new Color(0,0,0);
		for (Circle circle : circles) {
			if (!circle.coversPoint(point)) continue;
			double rot = circle.orientation(center,point);
			result = result.mix(
					new Color(Math.abs(Math.tan(rot)),Math.abs(Math.tan(rot)),Math.abs(Math.cos(rot)))
					.mul(circle.colour())
				);
		}
		return result;
	}
	// FIXME: prettify colour spectrum
}
