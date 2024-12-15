package cgg.mtrl;

import static java.lang.Math.*;
import java.util.Arrays;
import java.util.ArrayList;
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

	public Vec2[][] subsets_sequence(int len)
	{
		int __Max = (int)(lookup.length*.5)/len;
		Vec2[][] out = new Vec2[__Max][len];
		for (int i=0;i<__Max;i++)
		{
			for(int j=0;j<len;j++)
				out[i][j] = point(i*len+j);
		}
		return out;
	}

	public ArrayList<ArrayList<Vec2>> subsets_raster(int smp)
	{
		// initialize
		double __Step = 1./(double)smp;
		ArrayList<ArrayList<Vec2>> out = new ArrayList<>(smp*smp);
		for (int i=0;i<smp*smp;i++) out.add(new ArrayList<>());

		// iterate
		for (int i=0;i<lookup.length;i+=2)
		{
			Vec2 coord = vec2(lookup[i],lookup[i+1]);
			int x = (int)(coord.x()/__Step);
			int y = (int)(coord.y()/__Step);
			out.get(y*smp+x).add(vec2((coord.x()%__Step)*smp,(coord.y()%__Step)*smp));
		}
		return out;
	}

	public static ArrayList<Vec2> map_subset(ArrayList<ArrayList<Vec2>> samples,Vec2 coord,int count)
	{
		return samples.get(((int)coord.y()%count)*count+(int)coord.x()%count);
	}
}
