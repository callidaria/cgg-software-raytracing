package cgg.a02;

import tools.*;

public class Hit
{
	private double param;
	private Vec3 position;
	private Vec2 uv;
	private Vec3 normal;
	private Color colour;
	private int type;

	public Hit(double param,Vec3 position,Vec2 uv,Vec3 normal,Color colour)
	{
		this.param = param;
		this.position = position;
		this.uv = uv;
		this.normal = normal;
		this.colour = colour;
		this.type = 0;
	}

	public String toString()
	{
		return "Hit[t="+param+", point="+position+", normal="+normal+"]";
	}

	public double param() { return param; }
	public Vec3 position() { return position; }
	public Vec2 uv() { return uv; }
	public Vec3 normal() { return normal; }
	public Color colour() { return colour; }
	public int type() { return type; }
	public void setType(int type) { this.type = type; }
	public void overwritePosition(Vec3 position) { this.position = position; }
	public void overwriteNormal(Vec3 normal) { this.normal = normal; }
	public void overwriteColour(Color colour) { this.colour = colour; }
}
