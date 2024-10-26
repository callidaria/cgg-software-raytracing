package cgg.a02;

import tools.*;

public record Hit(double param,Vec3 position,Vec3 normal,Color colour)
{
	public String toString()
	{
		return "Hit[t="+param+", point="+position+", normal="+normal+"]";
	}
}
