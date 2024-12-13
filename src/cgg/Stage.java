package cgg;

import java.util.ArrayList;
import cgg.a02.*;
import cgg.geom.*;
import cgg.lght.*;


public interface Stage
{
	public Camera camera();
	public Geometry groot();
	public ArrayList<Geometry> emitter();
	public ArrayList<Illumination> lights();
};
