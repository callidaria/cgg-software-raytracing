package cgg.mtrl;

import tools.*;
import static tools.Functions.*;
import cgg.a02.Hit;


public class UnknownMaterial implements Material
{
	public UnknownMaterial() {  }

	public Color getComponent(MaterialComponent mc,Hit hit)
	{
		return color(0);
	}
}
