package cgg.lght;

import static java.lang.Math.*;
import static tools.Functions.*;
import tools.*;


public class PointLight implements Illumination
{
	private Vec3 position;
	private Color colour;
	private double constant;
	private double linear;
	private double quadratic;

	public PointLight(Vec3 position,Color colour,double intensity)
	{
		this.position = position;
		this.colour = multiply(colour,intensity);
		this.constant = 1.;
		this.linear = .045;
		this.quadratic = .0075;
	}

	public PointLight(Vec3 position,Color colour,double intensity,double constant,double linear,double quadratic)
	{
		this.position = position;
		this.colour = multiply(colour,intensity);
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
	}

	public Vec3 direction(Vec3 position)
	{
		return normalize(subtract(this.position,position));
	}

	public double distance(Vec3 position)
	{
		return length(subtract(this.position,position));
	}

	public Color intensity(Vec3 position)
	{
		return divide(colour,pow(distance(position),2));
	}

	public Color physicalInfluence(Vec3 position)
	{
		double d = distance(position);
		double __Attenuation = 1/(constant+linear*d+quadratic*pow(d,2.));
		return multiply(colour,__Attenuation);
	}
}
