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
		final double SECONDS = 2;
		final int FRAMERATE = 40;
		final double FRAMES = SECONDS*FRAMERATE;
		BodhisPoolRepair scene = new BodhisPoolRepair(FRAMERATE);
		RayTracer rt = new RayTracer(scene);

		// render loop
		StopWatch __Timing = new StopWatch();
		for (int i=0;i<FRAMES;i++)
		{
			// setup
			System.out.println("FRAME: "+i+"/"+FRAMES+" -> "+(int)((i/FRAMES)*100)+"%");
			scene.render_setup();
			rt.bake();

			// image
			Image image = new Image();
			image.sample(new HaltonSampler(rt));
			image.writePng(String.format("video/frame-%04d",i));

			// update
			scene.update();
		}
		__Timing.stop();
		System.out.printf("video render time: %.2fs\n",__Timing.time_seconds());

		try
		{
			new ProcessBuilder(
					"ffmpeg",
					"-y",
					"-loglevel","panic",
					"-r",String.valueOf(FRAMERATE),
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
