package cgg.mtrl;

import static tools.Functions.*;
import tools.*;
import cgg.a02.Hit;


public class PhysicalMaterial implements Material
{
	private ImageTexture colour;
	private Color fallback;
	private ImageTexture material;
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

	public Color getComponent(MaterialComponent comp,Hit hit)
	{
		switch (comp)
		{
		case COLOUR: return (colour!=null) ? colour.getColor(multiply(hit.uv(),vec2(texel,texel))) : fallback;
		case MATERIAL: return material.getColor(multiply(hit.uv(),vec2(texel,texel)));
		}
		return color(0,0,0);
	}
}
