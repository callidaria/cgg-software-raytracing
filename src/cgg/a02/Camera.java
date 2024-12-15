package cgg.a02;

import static java.lang.Math.*;
import static tools.Functions.*;
import cgg.*;
import tools.*;

public class Camera
{
	private Vec3 position;
	private double rad_thfov;
	private Mat44 rotation;
	private double hwidth;
	private double hheight;

	public Camera(Vec3 position)
	{
		_init(position);
		this.rotation = identity();
	}

	public Camera(Vec3 position,Vec3 rotation)
	{
		_init(position);
		this.rotation = multiply(
			rotate(vec3(0,0,1),rotation.z()),
			multiply(rotate(vec3(0,1,0),rotation.y()),rotate(vec3(1,0,0),rotation.x())));
		// FIXME: faster method, maybe normalize rotation vector and apply maximum degree?
	}

	private void _init(Vec3 position)
	{
		this.position = position;
		this.rad_thfov = tan(toRadians(Config.FOV)*.5);
		this.hwidth = Config.WIDTH*.5;
		this.hheight = Config.HEIGHT*.5;
	}

	public Ray generateRay(Vec2 coord)
	{
		Vec3 __RayDirection = normalize(vec3(coord.x()-hwidth,coord.y()-hheight,-hwidth/rad_thfov));
		__RayDirection = multiplyDirection(rotation,__RayDirection);
		return new Ray(position,__RayDirection);
	}

	public Vec3 position() { return position; }
}
