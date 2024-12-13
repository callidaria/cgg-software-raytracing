package cgg.lght;

import static tools.Functions.*;
import tools.*;


public class DirectionalLight implements Illumination
{
	private Vec3 direction;
	private Color colour;

	public DirectionalLight(Vec3 direction,Color colour,double intensity)
	{
		this.direction = normalize(direction);
		this.colour = multiply(colour,intensity);
	}

	public Vec3 direction(Vec3 position)
	{
		return direction;
	}

	public double distance(Vec3 position)
	{
		return 10000;
	}

	public Color intensity(Vec3 position)
	{
		return colour;
	}

	public Color physicalInfluence(Vec3 position)
	{
		return colour;
	}
}
