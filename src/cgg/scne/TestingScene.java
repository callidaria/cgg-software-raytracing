package cgg.scne;

import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


public class TestingScene extends Scene
{
	// textures
	private ImageTexture tex_checker;

	// materials
	private ImageTexture mat_plastic;
	private ImageTexture mat_gold;
	private ImageTexture mat_marble;


	public TestingScene()
	{
		super(vec3(0,-1,0),vec3(0,0,0));

		// textures
		this.tex_checker = new ImageTexture("./res/checker_neo.png");

		// materials
		this.mat_plastic = new ImageTexture("./res/plastic/material.png");
		this.mat_gold = new ImageTexture("./res/gold/material.png");
		this.mat_marble = new ImageTexture("./res/marble/material.png");

		// flooring
		groot.register_geometry(new Box(vec3(0,1,-5),10,1,10,new PhysicalMaterial(tex_checker,mat_marble,4)));

		// lighting
		craeveTheVorbiddenLaemp(vec3(0,-1.5,-7),color(1,1,1),.4);

		groot.update_bounds();
	}
}
