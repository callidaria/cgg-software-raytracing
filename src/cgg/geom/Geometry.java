package cgg.geom;

import java.util.ArrayList;
import cgg.a02.Ray;
import cgg.a02.Hit;

public interface Geometry
{
	public ArrayList<HitTuple> intersect(Ray ray);
}
