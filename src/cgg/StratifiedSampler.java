package cgg;

import static java.lang.Math.*;
import java.util.stream.Stream;
import static tools.Functions.*;
import tools.*;


public class StratifiedSampler implements Sampler
{
	private Sampler sampler;
	private double dvsamples;

	public StratifiedSampler(Sampler sampler)
	{
		this.sampler = sampler;
		this.dvsamples = 1./(double)Config.SUBPIXEL_STRATIFIED;
	}

	public Color getColor(Vec2 coord)
	{
		Color __Result = color(0,0,0);
		for (int y=0;y<Config.SUBPIXEL_STRATIFIED;y++)
		{
			for (int x=0;x<Config.SUBPIXEL_STRATIFIED;x++)
			{
				Vec2 __Subpixel = add(coord,vec2(x*dvsamples+Functions.random()*dvsamples,
												 y*dvsamples+Functions.random()*dvsamples));
				__Result = add(__Result,sampler.getColor(__Subpixel));
			}
		}
		return divide(__Result,pow(Config.SUBPIXEL_STRATIFIED,2.));
	}
}
