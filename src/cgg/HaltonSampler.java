package cgg;

import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.stream.Stream;
import static tools.Functions.*;
import tools.*;
import cgg.*;
import cgg.mtrl.LUT;


public class HaltonSampler implements Sampler
{
	private Sampler sampler;
	ArrayList<ArrayList<Vec2>> samples;
	private double dvsamples;

	public HaltonSampler(Sampler sampler)
	{
		this.sampler = sampler;
		this.samples = LUT.lookup().subsets_raster(Config.SUBPIXEL_SAMPLES);
		this.dvsamples = 1./(double)Config.SUBPIXEL_SAMPLES;
	}

	public Color getColor(Vec2 coord)
	{
		Color __Result = color(0,0,0);
		ArrayList<Vec2> pxs = LUT.map_subset(samples,coord,Config.SUBPIXEL_SAMPLES);
		for (Vec2 v : pxs) __Result = add(__Result,sampler.getColor(add(coord,v)));
		return divide(__Result,pxs.size());
	}
}
