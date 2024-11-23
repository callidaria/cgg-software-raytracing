package cgg.a02;

import static java.lang.Math.*;
import static tools.Functions.*;
import tools.*;

public class Camera
{
	private Vec3 position;
	private double deg_fov;
	private double rad_thfov;
	private Mat44 rotation;
	private double width;
	private double hwidth;
	private double height;
	private double hheight;
	private double exposure;
	private final double near = 0;
	private final double far = 1000;

	public Camera(Vec3 position,double fov,double width,double height,double exposure)
	{
		this.position = position;
		this.deg_fov = fov;
		this.rad_thfov = tan(toRadians(fov)*.5);
		this.rotation = identity();
		this.width = width;
		this.hwidth = width*.5;
		this.height = height;
		this.hheight = height*.5;
		this.exposure = exposure;
	}

	public Camera(Vec3 position,double fov,Vec3 rotation,double width,double height,double exposure)
	{
		this.position = position;
		this.deg_fov = fov;
		this.rad_thfov = tan(toRadians(fov)*.5);
		this.rotation = multiply(
			rotate(vec3(0,0,1),rotation.z()),
			multiply(rotate(vec3(0,1,0),rotation.y()),rotate(vec3(1,0,0),rotation.x())));
		// FIXME: faster method, maybe normalize rotation vector and apply maximum degree?
		this.width = width;
		this.hwidth = width*.5;
		this.height = height;
		this.hheight = height*.5;
		this.exposure = exposure;
	}

	public Ray generateRay(Vec2 coord)
	{
		Vec3 __RayDirection = normalize(vec3(coord.x()-hwidth,coord.y()-hheight,-hwidth/rad_thfov));
		__RayDirection = multiplyDirection(rotation,__RayDirection);
		return new Ray(position,__RayDirection,near,far);
	}

	public Vec3 position() { return position; }
	public double exposure() { return exposure; }
}
