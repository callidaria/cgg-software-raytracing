#include "gfx.h"


/**
 *	raytracer
 *	\param bff: resulting image buffer
 *	\param cam: camera
 *	\param scn: root node of scene graph
 */
void rt(Image* bff,const Camera* cam,const SGNode* scn)
{
	for (u32 y=0;y<BUFFER_RESOLUTION_HEIGHT;++y)
	{
		for (u32 x=0;x<BUFFER_RESOLUTION_WIDTH;++x)
		{
			Intersection __Hit = (Intersection){ 0 };
			test_intersection(scn,&cam->rays[y*BUFFER_RESOLUTION_WIDTH+x],&__Hit);
			if (__Hit.hit)
				write_pixel(bff,x,y,convertv3rgb(&__Hit.normal));
		}
	}
}
