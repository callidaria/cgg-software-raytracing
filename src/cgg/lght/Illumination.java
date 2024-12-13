package cgg.lght;

import tools.*;


public interface Illumination
{
	Vec3 direction(Vec3 position);
	double distance(Vec3 position);
	Color intensity(Vec3 position);
	Color physicalInfluence(Vec3 position);
}
