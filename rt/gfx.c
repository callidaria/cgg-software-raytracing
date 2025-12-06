#include "gfx.h"


// ----------------------------------------------------------------------------------------------------
// Raytracer

/**
 *	definition of shading functions correlating to material type enumerator
 *	\param hit: intersection information with traced ray
 *	\returns vectorial representation of colour in R4 between for mapped value v is 0.0 <= v <= 1.0
 */
#define SHADER_PARAMETERS const Scene* scn,const Intersection* hit
typedef vec3 (*_material_shading)(SHADER_PARAMETERS);
static vec3 _shade_basic(SHADER_PARAMETERS);
static vec3 _shade_phong(SHADER_PARAMETERS);
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
static inline vec3 _process_scene(const Scene* scn,const Ray* ray)
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
			vec3 __PixelColour = _process_scene(scn,&cam->rays[y*BUFFER_RESOLUTION_WIDTH+x]);
			write_pixel(bff,x,y,convertv3rgb(__PixelColour));
		}
	}
}


// ----------------------------------------------------------------------------------------------------
// Shading

// default background colour
static inline vec3 _shade_basic(SHADER_PARAMETERS)
{
	return (vec3){ GFX_BACKGROUND_COLOUR_R,GFX_BACKGROUND_COLOUR_G,GFX_BACKGROUND_COLOUR_B };
}

// shade surface as phong material
static inline vec3 _shade_phong(SHADER_PARAMETERS)
{
	vec3 __Result = (vec3){ .0f,.0f,.0f };

	// iterate light sources
	for (u32 i=0;i<scn->lighting.crr_light;i++)
	{
		const Illumination* p_Light = &scn->lighting.lighting[i];
		LightInfo info;
		compute_lighting_info(p_Light,&info,hit->position);

		// shadow computation
		// TODO

		// diffuse component
		vec3 __Colour = (vec3){ 1.f,1.f,1.f };  // TODO replace with surface colour
		f32 __Attitude = dotv3(hit->normal,info.direction);
		vec3 __Diffuse = mulv3(__Colour,mulv3s(info.intensity,fmax(0,__Attitude)));
		__Result = addv3(__Result,__Diffuse);

		// specular component
		// TODO
	}

	return clampv3(__Result,.0f,1.f);
}
// TODO surface colour read
// TODO implement ambient component
