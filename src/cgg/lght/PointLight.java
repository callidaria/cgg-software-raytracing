package cgg.lght;

import static java.lang.Math.*;
import static tools.Functions.*;
import tools.*;


public class PointLight implements PhongIllumination
{
	private Vec3 position;
	private Color colour;
	private double intensity;

	public PointLight(Vec3 position,Color colour,double intensity)
	{
		this.position = position;
		this.colour = colour;
		this.intensity = intensity;
	}

	public Vec3 direction(Vec3 position)
	{
		return normalize(subtract(this.position,position));
	}

	public Color intensity(Vec3 position)
	{
		return divide(multiply(colour,intensity),pow(length(subtract(this.position,position)),2));
	}
}
