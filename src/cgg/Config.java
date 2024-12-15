package cgg;


public class Config
{
	// view
	public static final int WIDTH = 100;
	public static final int HEIGHT = 100;
	public static final int FOV = 60;
	public static final double EXPOSURE = 1.;
	public static final double NEAR = .0001;
	public static final double FAR = 1000;
	public static final double GAMMA = 2.2;

	// sampling
	public static final int SUBPIXEL_STRATIFIED = 4;
	public static final int SUBPIXEL_SAMPLES = 4;
	public static final int DIFFUSE_SAMPLES = 16;
	public static final int SPECULAR_SAMPLES = 16;

	private Config() {  }
}
