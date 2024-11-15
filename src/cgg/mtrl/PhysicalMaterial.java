package cgg.mtrl;

import static tools.Functions.*;
import tools.*;
import cgg.a02.Hit;


public class PhysicalMaterial implements Material
{
	private ImageTexture colour;
	private Color fallback;
	private ImageTexture material;

	public PhysicalMaterial(String colour_path,String material_path)
	{
		colour = new ImageTexture(colour_path);
		material = new ImageTexture(material_path);
	}

	public PhysicalMaterial(Color colour,String material_path)
	{
		fallback = colour;
		material = new ImageTexture(material_path);
	}

	public Color getComponent(MaterialComponent comp,Hit hit)
	{
		switch (comp)
		{
		case COLOUR: return (colour!=null) ? colour.getColor(hit.uv()) : fallback;
		case MATERIAL: return material.getColor(hit.uv());
		}
		return color(0,0,0);
	}
}
