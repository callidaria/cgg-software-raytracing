package cgg.geom;

import java.util.ArrayList;
import static cgg.Math.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class Complex implements Geometry
{
	Geometry g0;
	Geometry g1;
	JoinOperation jop;

	public Complex(Geometry g0,Geometry g1,JoinOperation jop)
	{
		this.g0 = g0;
		this.g1 = g1;
		this.jop = jop;
	}

	public ArrayList<HitTuple> intersect(Ray ray)
	{
		switch(jop)
		{
			case UNION: return _union(ray);
			case INTERSECT: return _intersect(ray);
			case DIFFERENCE: return _difference(ray);
		}
		return null;
	}

	private ArrayList<HitTuple> _union(Ray ray)
	{
		ArrayList<HitTuple> __Hits0 = g0.intersect(ray);
		// TODO
		/*
		Hit __Hit1 = g1.intersect(ray).back();
		*/
		//return { new HitTuple(secondHitRecent(__Hit0,__Hit1) ? __Hit1 : __Hit0,null) };
		return __Hits0;
		// TODO: back intersection
	}

	private ArrayList<HitTuple> _intersect(Ray ray)
	{
		// TODO
		return g0.intersect(ray);
	}

	private ArrayList<HitTuple> _difference(Ray ray)
	{
		// TODO
		return g0.intersect(ray);
	}
}
