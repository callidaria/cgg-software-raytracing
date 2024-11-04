package cgg.lght;

import static tools.Functions.*;
import tools.*;


public class DirectionalLight implements PhongIllumination
{
	private Vec3 direction;
	private Color colour;
	private double intensity;

	public DirectionalLight(Vec3 direction,Color colour,double intensity)
	{
		this.direction = normalize(direction);
		this.colour = colour;
		this.intensity = intensity;
	}

	public Vec3 direction(Vec3 position)
	{
		return direction;
	}

	public Color intensity(Vec3 position)
	{
		return multiply(colour,intensity);
	}
}
