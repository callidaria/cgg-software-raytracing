package cgg.mtrl;

import tools.*;
import static tools.Functions.*;
import cgg.a02.Hit;


public class TransmittingMaterial implements Material
{
	private Color colour;
	private double mcoeff;

	public TransmittingMaterial(Color colour,double mcoeff)
	{
		this.colour = colour;
		this.mcoeff = mcoeff;
	}

	public Color getComponent(MaterialComponent comp,Hit hit)
	{
		switch (comp)
		{
		case COLOUR: return colour;
		case MASS: return color(mcoeff,0,0);
		}
		return color(0);
	}
}
