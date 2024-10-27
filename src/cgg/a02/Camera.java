package cgg.a02;

import static java.lang.Math.*;
import static tools.Functions.*;
import tools.*;

public class Camera
{
	private Vec3 position;
	private double deg_fov;
	private double rad_thfov;
	private double width;
	private double hwidth;
	private double height;
	private double hheight;
	private final double near = 0;
	private final double far = 1000;

	public Camera(Vec3 position,double fov,double width,double height)
	{
		this.position = position;
		this.deg_fov = fov;
		this.rad_thfov = tan(toRadians(fov)*.5);
		this.width = width;
		this.hwidth = width*.5;
		this.height = height;
		this.hheight = height*.5;
	}

	public Ray generateRay(Vec2 coord)
	{
		Vec3 ray_direction = normalize(vec3(coord.x()-hwidth,coord.y()-hheight,-hwidth/rad_thfov));
		return new Ray(position,ray_direction,near,far);
	}

	public Vec3 position() { return position; }
}
