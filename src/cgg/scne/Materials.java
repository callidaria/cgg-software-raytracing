package cgg.scne;

import tools.*;
import cgg.mtrl.*;


public class Materials
{
	// textures
	public static final ImageTexture tex_checker = new ImageTexture("./res/checker_neo.png");

	// materials
	public static final ImageTexture mat_plastic = new ImageTexture("./res/plastic/material.png");
	public static final ImageTexture mat_gold = new ImageTexture("./res/gold/material.png");
	public static final ImageTexture mat_marble = new ImageTexture("./res/marble/material.png");

	// normal maps
	public static final NormalMap nrm_plastic = new NormalMap("./res/plastic/normals.png");
	public static final NormalMap nrm_gold = new NormalMap("./res/gold/normals.png");
	public static final NormalMap nrm_marble = new NormalMap("./res/marble/normals.png");

	private Materials() {  }
}
