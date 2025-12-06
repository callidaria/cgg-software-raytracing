#include "gfx.h"


// ----------------------------------------------------------------------------------------------------
// Raytracer

/**
 *	definition of shading functions correlating to material type enumerator
 *	\param hit: intersection information with traced ray
 *	\returns vectorial representation of colour in R4 between for mapped value v is 0.0 <= v <= 1.0
 */
#define SHADER_PARAMETERS const Scene* scn,const Intersection* hit
typedef vec4 (*_material_shading)(SHADER_PARAMETERS);
static vec4 _shade_basic(SHADER_PARAMETERS);
static vec4 _shade_phong(SHADER_PARAMETERS);
static _material_shading _shade[MATERIAL_COUNT] = {
	_shade_basic,
	_shade_phong,
};

/**
 *	process scene around ray
 *	\param scn: scene intersecting with cast ray
 *	\param ray: processing ray
 *	\returns resulting colour as R4 vector
 */
static inline vec4 _process_scene(const Scene* scn,const Ray* ray)
{
	Intersection __Hit = (Intersection){ .material = MATERIAL_NONE };
	test_intersection(&scn->graph,ray,&__Hit);
	return _shade[__Hit.material](scn,&__Hit);
}

/**
 *	raytracer
 *	\param bff: resulting image buffer
 *	\param cam: camera
 *	\param scn: root node of scene graph
 */
void rt(Image* bff,const Camera* cam,const Scene* scn)
{
	for (u32 y=0;y<BUFFER_RESOLUTION_HEIGHT;++y)
	{
		for (u32 x=0;x<BUFFER_RESOLUTION_WIDTH;++x)
		{
			vec4 __PixelColour = _process_scene(scn,&cam->rays[y*BUFFER_RESOLUTION_WIDTH+x]);
			write_pixel(bff,x,y,convertv4rgb(__PixelColour));
		}
	}
}


// ----------------------------------------------------------------------------------------------------
// Shading

// default background colour
static inline vec4 _shade_basic(SHADER_PARAMETERS)
{
	return (vec4){ GFX_BACKGROUND_COLOUR_R,GFX_BACKGROUND_COLOUR_G,GFX_BACKGROUND_COLOUR_B,1.f };
}

// shade surface as phong material
static inline vec4 _shade_phong(SHADER_PARAMETERS)
{
	// TODO implement phong material
	vec4 __Output = (vec4){ hit->normal.x,hit->normal.y,hit->normal.z,1.f };
	return clampv4(__Output,.0f,1.f);
}
