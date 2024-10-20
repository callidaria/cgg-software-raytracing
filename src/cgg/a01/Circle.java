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
		return sqCenterDistance(coordinate)<=sq_radius;
	}

	public double fadingInfluence(Vec2 coordinate) {
		return Math.max((coordinate.sub(position).sqDistance()-sq_radius)*-1,.0)/sq_radius;
	}

	public double orientation(Vec2 target,Vec2 coordinate) {
		Vec2 ori = target.sub(position);
		Vec2 dir = coordinate.sub(position);
		return Math.acos(ori.dot(dir)/(ori.distance()*dir.distance()));
	}

	public Color waveEmission(Vec2 coordinate) {
		double range = Math.max(Math.sin(coordinate.sub(position).sqDistance()/sq_radius),.0);
		return new Color(
				range/.3,
				Math.max(range-.3,.0)/.3,
				Math.max(range-.6,.0)/.3
			);
	}

	public Color colour() {
		return colour;
	}

	private double sqCenterDistance(Vec2 coordinate) {
		return coordinate.sub(position).sqDistance();
	}
}
