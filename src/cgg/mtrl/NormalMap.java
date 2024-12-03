package cgg.mtrl;

import static tools.Functions.*;
import tools.*;


public class NormalMap
{
	private ImageTexture map;

	public NormalMap(String map_path)
	{
		this.map = new ImageTexture(map_path);
	}

	public Vec3 produceNormal(Vec2 uv,Vec3 normal,Vec3 tangent)
	{
		Vec3 __Tangent = normalize(subtract(tangent,multiply(normal,dot(tangent,normal))));
		Mat44 __TBN = matrix(__Tangent,cross(normal,__Tangent),normal);
		Vec3 __MapData = subtract(multiply(vec3(map.getColor(uv)),2.),1.);
		return normalize(multiplyDirection(__TBN,__MapData));
	}
}
