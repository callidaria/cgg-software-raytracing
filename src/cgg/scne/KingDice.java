package cgg.scne;

import static tools.Functions.*;
import tools.*;
import cgg.geom.*;


public class KingDice extends Scene
{
	private int dice_count = 0;

	public KingDice()
	{
		super(vec3(0,-10,0),vec3(35,0,0));

		// flooring
		Structures.floor(groot,vec3(0,1,-50),100,30);

		// geometry
		GraphNode midline = new GraphNode();
		for (int z=0;z>-100;z-=10) midline.register_geometry(_diceGroup(vec3(0,0,z)));
		midline.update_bounds();
		GraphNode leftline = new GraphNode();
		for (int z=0;z>-100;z-=10) leftline.register_geometry(_diceGroup(vec3(-10,0,z)));
		leftline.update_bounds();
		GraphNode rightline = new GraphNode();
		for (int z=0;z>-100;z-=10) rightline.register_geometry(_diceGroup(vec3(10,0,z)));
		rightline.update_bounds();
		System.out.println(dice_count);

		// lighting
		craeveTheVorbiddenLaemp(vec3(0,-4,-7),color(1,1,1),.4);

		// assemble
		groot.register_geometry(midline);
		groot.register_geometry(leftline);
		groot.register_geometry(rightline);
		groot.update_bounds();
	}

	private Geometry _diceGroup(Vec3 chunk)
	{
		GraphNode out = new GraphNode(chunk,vec3(1),vec3(0));
		for (int x=-5;x<5;x++)
		{
			for (int z=0;z>-10;z--)
				out.register_geometry(_diceStack(vec3(x,0,z)));
		}
		out.update_bounds();
		return out;
	}

	private Geometry _diceStack(Vec3 position)
	{
		GraphNode out = new GraphNode(add(position,multiply(randomDirection(),.15)),vec3(1),vec3(0));
		for (int i=0;i<random()*6+1;i++)
		{
			out.register_geometry(
					Structures.die(vec3(0,-.5*i,0),vec3(0,20*random(),0),randomHue(),.5,random(),random())
				);
			dice_count++;
		}
		out.update_bounds();
		return out;
	}
}
