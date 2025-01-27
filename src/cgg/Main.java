package cgg;

import java.lang.ProcessBuilder;
import java.lang.InterruptedException;
import java.io.File;
import java.io.IOException;
import static tools.Functions.*;
import tools.*;
import cgg.scne.*;


public class Main
{
	public static void main(String[] args)
	{
		// geometry
		BodhisPoolRepair scene = new BodhisPoolRepair();

		// render loop
		double seconds = 1;
		for (int i=0;i<seconds*30;i++)
		{
			// setup
			scene.render_setup();

			// raytracer
			RayTracer rt = new RayTracer(scene);
			rt.bake();

			// image
			Image image = new Image();
			image.sample(new HaltonSampler(rt));
			image.writePng(String.format("video/frame-%04d",i));

			// update
			scene.update();
		}

		try
		{
			new ProcessBuilder(
					"ffmpeg",
					"-y",
					"-loglevel","panic",
					"-r","30",
					"-start_number","0",
					"-i","video/frame-%04d.png",
					"-pix_fmt","yuv420p",
					"-vcodec","libx264",
					"-crf","16",
					"-preset","veryslow","cgg-competition-ws-24-103717.mp4"
				).directory(new File("./images"))
				.start()
				.waitFor();
		}
		catch (IOException|InterruptedException e) { System.out.println(e); }
	}
}
