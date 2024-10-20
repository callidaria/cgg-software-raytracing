package cgg.a01;

import java.util.ArrayList;
import tools.*;

public record Orbz(ArrayList<Circle> circles) implements Sampler {

	public Color getColor(Vec2 point) {
		Color result = new Color(0,0,0);
		for (Circle circle : circles) {
			if (circle.coversPoint(point))
				result = result.add(circle.colour().mul(circle.fadingInfluence(point)));
		}
		return result;
	}
}
