package cgg.a01;

import java.lang.Math.*;
import tools.*;

public class Circle {

	private Vec2 position;
	private double radius;
	private double sq_radius;
	private Color colour;

	public Circle(Vec2 position,float radius,Color colour) {
		this.position = position;
		this.radius = radius;
		this.sq_radius = Math.pow(radius,2);
		this.colour = colour;
	}

	public boolean coversPoint(Vec2 coordinate) {
		double __SqVectorDistance = Math.pow(coordinate.x()-position.x(),2)+Math.pow(coordinate.y()-position.y(),2);
		return __SqVectorDistance<=sq_radius;
	}

	public Color colour() {
		return colour;
	}
}
