#include "gfx.h"


/**
 *	raytracer
 *	\param bff: resulting image buffer
 *	\param cam: camera
 */
void rt(Image* bff,Camera* cam)
{
	for (u32 y=0;y<BUFFER_RESOLUTION_HEIGHT;++y)
	{
		for (u32 x=0;x<BUFFER_RESOLUTION_WIDTH;++x)
			write_pixel(bff,x,y,(crgb){ 120,0,0 });
	}
}
