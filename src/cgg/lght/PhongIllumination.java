package cgg.lght;

import tools.*;


public interface PhongIllumination
{
	Vec3 direction(Vec3 position);
	Color intensity(Vec3 position);
}
