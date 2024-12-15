package cgg.a02;

import static tools.Functions.*;
import tools.*;
import cgg.*;


public record Ray(Vec3 origin,Vec3 direction)
{
	public Vec3 calculatePosition(double t)
	{
		return add(origin,multiply(direction,t));
	}

	public boolean paramInRange(double t)
	{
		return (t>=Config.NEAR)&&(t<=Config.FAR);
	}

	public String toString()
	{
		return "Ray[origin="+origin.toString()+", direction="+direction.toString()+"]";
	}
}
