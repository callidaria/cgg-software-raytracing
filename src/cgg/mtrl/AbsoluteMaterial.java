package cgg.mtrl;

import tools.*;
import static tools.Functions.*;
import cgg.a02.Hit;


public class AbsoluteMaterial implements Material
{
	private Color colour;

	public AbsoluteMaterial(Color colour)
	{
		this.colour = colour;
	}

	public Color getComponent(MaterialComponent comp,Hit hit)
	{
		return colour;
	}
}
