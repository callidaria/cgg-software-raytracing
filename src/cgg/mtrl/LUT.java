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
	private static final int SAMPLES = 4096;
	private final double[] lookup;
	private final double[] lut_static;

	private LUT()
	{
		String ls_line = "";
		String lut_line = "";

		// read tables
		try
		{
			ls_line = new BufferedReader(new FileReader("./res/lut/vdc")).readLine();
			lut_line = new BufferedReader(new FileReader("./res/lut/vdcorput")).readLine();
		}
		catch (IOException e) { System.out.println("vdcorput lut could not be read"); }

		// translate lookup data
		this.lookup = Arrays.stream(ls_line.split(",")).mapToDouble(Double::parseDouble).toArray();
		this.lut_static = Arrays.stream(lut_line.split(",")).mapToDouble(Double::parseDouble).toArray();
	}

	public ArrayList<ArrayList<Vec2>> subsets_raster(int smp)
	{
		// initialize
		int __Sets = 64*(int)pow(2,6-(int)(log(smp)/log(2)));
		double __Step = 1./(double)sqrt(__Sets);
		ArrayList<ArrayList<Vec2>> out = new ArrayList<>();
		for (int i=0;i<__Sets;i++) out.add(new ArrayList<>());
		// FIXME misaligned subsets when log(smp)/log(2)%2!=0  !!this is extremely important for image quality!!

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

	// TODO: return by chosen sample amount
	public Vec2 lookup_static(int i) { return vec2(lut_static[i*2],lut_static[i*2+1]); }
	public int lookup_static_range() { return (int)(lut_static.length*.5); }

	public static LUT lookup() { return ref; }
}
