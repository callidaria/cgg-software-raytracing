package cgg.mtrl;

import tools.*;
import cgg.a02.Hit;


public class SurfaceColour implements Material
{
	private Color colour;

	public SurfaceColour(Color colour)
	{
		this.colour = colour;
	}

	public Color getComponent(MaterialComponent comp,Hit hit)
	{
		return colour;
	}
}
