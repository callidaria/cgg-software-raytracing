package cgg.lght;

import static java.lang.Math.*;
import static tools.Functions.*;
import tools.*;


public class PointLight implements PhongIllumination
{
	private Vec3 position;
	private Color colour;

	public PointLight(Vec3 position,Color colour,double intensity)
	{
		this.position = position;
		this.colour = multiply(colour,intensity);
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
		return divide(colour,pow(length(subtract(this.position,position)),2));
	}
}
