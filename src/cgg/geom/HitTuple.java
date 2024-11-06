package cgg.geom;

import cgg.a02.Hit;

public record HitTuple(Hit front,Hit back)
{
	public Hit relevantHit()
	{
		return (front!=null) ? front : back;
	}
}
