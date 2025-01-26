package cgg.mtrl;

import static tools.Functions.*;
import tools.*;
import cgg.a02.Hit;


public class PhysicalMaterial implements Material
{
	private ImageTexture colour;
	private Color fallback;
	private ImageTexture material;
	private Color material_fallback;
	private double texel;

	public PhysicalMaterial(ImageTexture colour,ImageTexture material,double texel)
	{
		this.colour = colour;
		this.material = material;
		this.texel = texel;
	}

	public PhysicalMaterial(Color colour,ImageTexture material,double texel)
	{
		this.fallback = colour;
		this.material = material;
		this.texel = texel;
	}

	public PhysicalMaterial(Color colour,Color material)
	{
		this.fallback = colour;
		this.material_fallback = material;
	}

	public Color getComponent(MaterialComponent comp,Hit hit)
	{
		switch (comp)
		{
		case COLOUR: return (colour!=null) ? colour.getColor(multiply(hit.uv(),vec2(texel,texel))) : fallback;
		case MATERIAL:
			return (material!=null) ? material.getColor(multiply(hit.uv(),vec2(texel,texel))) : material_fallback;
		}
		return color(0);
	}
}
