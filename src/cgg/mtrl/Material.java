package cgg.mtrl;

import tools.*;
import cgg.a02.Hit;


public interface Material
{
	Color getComponent(MaterialComponent comp,Hit hit);
}
