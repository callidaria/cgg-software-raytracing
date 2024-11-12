package cgg.mtrl;

import static tools.Functions.*;
import tools.*;
import cgg.a02.Hit;


public class SurfaceMaterial implements Material
{
	private ImageTexture colour;
	private Color fallback;

	public SurfaceMaterial(String colour_path)
	{
		colour = new ImageTexture(colour_path);
	}

	public SurfaceMaterial(Color colour)
	{
		fallback = colour;
	}

	public Color getComponent(MaterialComponent comp,Hit hit)
	{
		switch(comp)
		{
		case COLOUR: return (colour!=null) ? colour.getColor(hit.uv()) : fallback;
		}
		return color(0,0,0);
	}
}
