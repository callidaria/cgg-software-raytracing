package cgg;

import static java.lang.Math.*;
import java.util.stream.Stream;
import static tools.Functions.*;
import tools.*;


public class StratifiedSampler implements Sampler
{
	private Sampler sampler;
	private int samples;
	private double dvsamples;

	public StratifiedSampler(Sampler sampler,int samples)
	{
		this.sampler = sampler;
		this.samples = samples;
		this.dvsamples = 1./(double)samples;
	}

	public Color getColor(Vec2 coord)
	{
		Color __Result = color(0,0,0);
		for (int y=0;y<samples;y++)
		{
			for (int x=0;x<samples;x++)
			{
				Vec2 __Subpixel = add(coord,vec2(x*dvsamples+Functions.random()*dvsamples,
												 y*dvsamples+Functions.random()*dvsamples));
				__Result = add(__Result,sampler.getColor(__Subpixel));
			}
		}
		return divide(__Result,pow(samples,2.));
	}
}
