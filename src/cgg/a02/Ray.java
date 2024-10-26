package cgg.a02;

import static tools.Functions.*;
import tools.*;

public record Ray(Vec3 origin,Vec3 direction,double near,double far)
{
	public Vec3 calculatePosition(double t)
	{
		return add(origin,multiply(direction,t));
	}

	public boolean paramInRange(double t)
	{
		return (t>=near)&&(t<=far);
	}

	public String toString()
	{
		return "Ray[origin="+origin.toString()+", direction="+direction.toString()+"]";
	}
}
