package cgg.a02;

import static tools.Functions.*;
import tools.*;
import static cgg.Math.*;
import cgg.mtrl.*;


public class Hit
{
	private double param;
	private Vec3 position;
	private Vec2 uv;
	private Vec3 normal;
	private Material material;

	public Hit(double param,Vec3 position,Vec2 uv,Vec3 normal,Material material)
	{
		this.param = param;
		this.position = position;
		this.uv = uv;
		this.normal = normal;
		this.material = material;
	}

	public String toString()
	{
		return "Hit[t="+param+", point="+position+", normal="+normal+", uv="+uv+"]";
	}

	public double param() { return param; }
	public Vec3 position() { return position; }
	public Vec2 uv() { return uv; }
	public Vec3 normal() { return normal; }
	public Color colour() { return material.getComponent(MaterialComponent.COLOUR,hit_pointblank(vec3(0,0,0),material)); }
	public Material material() { return material; }
	public void overwritePosition(Vec3 position) { this.position = position; }
	public void overwriteUV(Vec2 uv) { this.uv = uv; }
	public void overwriteNormal(Vec3 normal) { this.normal = normal; }
	public void overwriteColour(Color colour) { this.material = new SurfaceMaterial(colour); }
	public void overwriteMaterial(Material material) { this.material = material; }
}
