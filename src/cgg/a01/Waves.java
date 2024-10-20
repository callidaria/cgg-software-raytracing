package cgg.a01;

import java.util.ArrayList;
import tools.*;

public record Waves(ArrayList<Circle> circles) implements Sampler {

	public Color getColor(Vec2 point) {
		Color result = new Color(0,0,0);
		for (Circle circle : circles) result = result.mix(circle.waveEmission(point));
		return result;
	}
}
