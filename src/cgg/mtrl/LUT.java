package cgg.mtrl;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import tools.*;
import static tools.Functions.*;


public class LUT
{
	private static LUT ref = new LUT();
	private final double[] lookup;

	private LUT()
	{
		String line = "";
		try { line = new BufferedReader(new FileReader("./res/lut/vdcorput")).readLine(); }
		catch (IOException e) { System.out.println("vdcorput lut could not be read"); }
		lookup = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
	}

	public static LUT lookup() { return ref; }

	public Vec2 point(int i)
	{
		int rst = i*2;
		return vec2(lookup[rst],lookup[rst+1]);
	}

	public Vec2[][] subsets(int len)
	{
		int max = (int)(lookup.length*.5)/len;
		Vec2[][] out = new Vec2[max][len];
		for (int i=0;i<max;i++)
		{
			for(int j=0;j<len;j++)
				out[i][j] = point(i*len+j);
		}
		return out;
	}

	// TODO geometry subsets in contrast to sequential subsets as above
}
