package cgg;

import tools.*;
import static tools.Functions.*;


public class Config
{
	// view
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final int FOV = 60;
	public static final double EXPOSURE = 1.;
	public static final double NEAR = .0001;
	public static final double FAR = 1000;
	public static final double GAMMA = 2.2;

	// colours
	public static final Color CLEARCOLOUR = color(0,0,0);
	public static final Color ERRORCOLOUR = color(0,.9,1.);

	// sampling
	public static final int RECURSION_DEPTH = 2;
	public static final int SUBPIXEL_STRATIFIED = 1;
	public static final int SUBPIXEL_SAMPLES = 1;
	public static final int DIFFUSE_SAMPLES = 16;
	public static final int SPECULAR_SAMPLES = 16;

	// corrections
	public static final int BF_DIAMETER = 16;
	public static final double BF_SIGMA_D = .45;
	public static final double BF_SIGMA_R = .0001;

	// threshold
	public static final double E_FRESNEL_DIFFUSE = .99;
	public static final double E_METALLIC_DIFFUSE = .99;

	private Config() {  }
}
