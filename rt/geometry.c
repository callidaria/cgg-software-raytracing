#include "geometry.h"


// ----------------------------------------------------------------------------------------------------
// Scene Graph

/**
 *	start scene graph root
 *	\returns neutral root node of scene graph
 */
SGNode* create_graph()
{
	SGNode* root = (SGNode*)malloc(sizeof(SGNode));
	root->type = NODE_TYPE_ROOT;
	root->geometry = NULL;
	root->crr_child = 0;
	return root;
}

/**
 *	reserve memory for subsequent geometry in graph based on selected node
 *	\param node: selected node, receiving offspring
 *	\param count: amount of subsequent geometry the node will receive
 */
void reserve_subsequent(SGNode* node,u8 count)
{
	node->subsequent = (SGNode*)malloc(sizeof(SGNode)*count);
}

/**
 *	define sphere as child of given node
 *	\param node: selected node to define geometry in relation to
 *	\param center: vector to center of the sphere
 *	\param radius: radius of the sphere
 *	\param material: surface material of the wphere
 *	\returns pointer to overwritten child node
 */
SGNode* define_sphere(SGNode* node,vec3 center,f32 radius,Material material)
{
	SGNode* out = &node->subsequent[node->crr_child++];
	out->type = NODE_TYPE_SPHERE;
	out->subsequent = NULL;
	out->crr_child = 0;

	// write sphere
	Sphere* __Sphere = (Sphere*)malloc(sizeof(Sphere));
	__Sphere->center = center;
	__Sphere->radius = radius;
	__Sphere->radius_sq = radius*radius;
	__Sphere->material = material;
	out->geometry = (f32*)__Sphere;
}

// recursive helper function that deletes all subtrees of the rootnode
static inline void _destroy_graph(SGNode* node)
{
	for (u8 i=0;i<node->crr_child;i++) _destroy_graph(node->subsequent);
	free(node->subsequent);
	free(node->geometry);
}

/**
 *	delete scene graph from memory
 *	\param node: root node of graph subtree, that should be deleted
 */
void destroy_graph(SGNode* node)
{
	_destroy_graph(node);
	free(node);
}


// ----------------------------------------------------------------------------------------------------
// Scene

/**
 *	initialize a scene
 *	\param cpos: camera position
 *	\returns pointer to the created scene
 */
Scene* create_scene(vec3 cpos)
{
	Scene* out = (Scene*)malloc(sizeof(Scene));

	// setup camera
	create_camera(&out->camera,cpos);
	update_camera(&out->camera);

	// setup rootnode of scene graph
	out->graph.type = NODE_TYPE_ROOT;
	out->graph.geometry = NULL;
	out->graph.crr_child = 0;

	// setup lighting defaults
	out->lighting.crr_sunlight = 0;
	out->lighting.crr_pointlight = 0;
	out->lighting.crr_light = 0;

	return out;
}

/**
 *	remove scene from memory
 *	\param scene: pointer to scene, that will be deleted
 */
void destroy_scene(Scene* scene)
{
	_destroy_graph(&scene->graph);
	destroy_camera(&scene->camera);
	free(scene);
}


// ----------------------------------------------------------------------------------------------------
// Intersection

/**
 *	definition of intersection functions correlating to NodeType geometry enumerator
 *	\param geom: pointer to geometry in memory
 *	\param ray: ray that may be intersecting with geometry stored in node
 *	\param hit: structure holding information about intersection
 */
typedef void (*_intersection_test)(const f32*,const Ray*,Intersection*);
void _root_intersection(const f32* geom,const Ray* ray,Intersection* hit) {  }
void _sphere_intersection(const f32*,const Ray*,Intersection*);
_intersection_test _test_intersection[NODE_TYPE_COUNT] = {
	_root_intersection,
	_sphere_intersection
};

void _sphere_intersection(const f32* geom,const Ray* ray,Intersection* hit)
{
	Sphere* __Sphere = (Sphere*)geom;

	// components
	vec3 __Center = subv3(ray->origin,__Sphere->center);
	f32 a = dotv3(ray->direction,ray->direction);  // TODO precompute?
	f32 b = 2.f*dotv3(__Center,ray->direction);
	f32 c = dotv3(__Center,__Center)-__Sphere->radius_sq;

	// discriminant & early exit
	f32 __SqComp = b*b-4.f*a*c;
	if (__SqComp<0) return;

	// calculate param
	f32 __AFac = 1.f/(2.f*a);
	f32 __SqCompSq = sqrtf(__SqComp)*__AFac;
	f32 __BFac = -b*__AFac;
	f32 t0 = __BFac+__SqCompSq;
	f32 t1 = __BFac-__SqCompSq;
	f32 ts0 = fmin(t0,t1);
	f32 ts1 = fmax(t0,t1);
	hit->position = ray_calculate_position(ray,ts0);  // FIXME clipped
	hit->normal = normalizev3(subv3(hit->position,__Sphere->center));
	hit->material = __Sphere->material;
	// FIXME elegance & optimization
	// TODO depthtesting & detailed intersection store
}

/**
 *	recursively check intersection of nodes
 *	\param node: root node of testing graph subtree
 *	\param ray: possibly intersecting ray
 *	\param hit: pointer to intersection status to update based on test result
 */
void test_intersection(const SGNode* node,const Ray* ray,Intersection* hit)
{
	for (u8 i=0;i<node->crr_child;i++) test_intersection(&node->subsequent[i],ray,hit);
	_test_intersection[node->type](node->geometry,ray,hit);
}


// ----------------------------------------------------------------------------------------------------
// Lighting

/**
 *	setup directional sunlight & enqueue for light processing
 *	\param lighting: lighting definition that will store the sunlight information
 *	\param direction: direction of the sunlight
 *	\param colour: colour & intensity of the sunlight as a vector
 */
void create_light_sun(Lighting* lighting,vec3 direction,vec3 colour)
{
	// setup light
	SunLight* p_Light = &lighting->sunlights[lighting->crr_sunlight++];
	p_Light->direction = normalizev3(direction);
	p_Light->colour = colour;

	// enqueue
	Illumination* p_Illumination = &lighting->lighting[lighting->crr_light++];
	p_Illumination->type = LIGHT_TYPE_SUN;
	p_Illumination->light = &p_Light->direction.x;
}

/**
 *	setup pointlight with default gradient & enqueue for light processing
 *	\param lighting: lighting definition that will store the pointlight information
 *	\param position: origin of the pointlight
 *	\param colour: colour & intensity of the pointlight as a vector
 */
void create_light_point_default(Lighting* lighting,vec3 position,vec3 colour)
{
	// setup light
	PointLight* p_Light = &lighting->pointlights[lighting->crr_pointlight++];
	p_Light->position = position;
	p_Light->colour = colour;
	p_Light->constant = 1.f;
	p_Light->linear = .045f;
	p_Light->quadratic = .0075f;

	// enqueue
	Illumination* p_Illumination = &lighting->lighting[lighting->crr_light++];
	p_Illumination->type = LIGHT_TYPE_POINT;
	p_Illumination->light = &p_Light->position.x;
}

/**
 *	setup pointlight with custom gradient & enqueue for light processing
 *	\param lighting: lighting definition that will store the pointlight information
 *	\param position: origin of the pointlight
 *	\param colour: colour & intensity of the pointlight as a vector
 *	\param constant: constant part of lighting gradient
 *	\param linear: linear part of lighting gradient
 *	\param quadratic: quadratic part of lighting gradient
 */
void create_light_point(Lighting* lighting,vec3 position,vec3 colour,f32 constant,f32 linear,f32 quadratic)
{
	// setup light
	PointLight* p_Light = &lighting->pointlights[lighting->crr_pointlight++];
	p_Light->position = position;
	p_Light->colour = colour;
	p_Light->constant = constant;
	p_Light->linear = linear;
	p_Light->quadratic = quadratic;

	// enqueue
	Illumination* p_Illumination = &lighting->lighting[lighting->crr_light++];
	p_Illumination->type = LIGHT_TYPE_POINT;
	p_Illumination->light = &p_Light->position.x;
}

/**
 *	definition of illumination info cases
 *	\param light: pointer to light in stack memory
 *	\param info: resulting light information at given position
 *	\param position: position in question
 */
#define ILLUMINATION_INFO_PARAMETERS const f32* light,LightInfo* info,vec3 position
typedef void (*_illumination_info_procedure)(ILLUMINATION_INFO_PARAMETERS);

// definition sunlight information
void _compute_sunlight_info(ILLUMINATION_INFO_PARAMETERS)
{
	SunLight* p_Light = (SunLight*)light;
	info->direction = p_Light->direction;
	info->distance = 10000;
	info->intensity = p_Light->colour;
	info->influence = p_Light->colour;
}

// definition pointlight information
void _compute_pointlight_info(ILLUMINATION_INFO_PARAMETERS)
{
	PointLight* p_Light = (PointLight*)light;
	vec3 __NPos = subv3(p_Light->position,position);

	// fill info
	info->direction = normalizev3(__NPos);
	info->distance = lengthv3(__NPos);

	// physical attenuation
	f32 __DistSq = info->distance*info->distance;
	info->intensity = divv3s(p_Light->colour,__DistSq);
	f32 __Attenuation = 1.f/(p_Light->constant+p_Light->linear*info->distance+p_Light->quadratic*__DistSq);
	info->influence = mulv3s(p_Light->colour,__Attenuation);
}

_illumination_info_procedure _compute_illumination_info[LIGHT_TYPE_COUNT] = {
	_compute_sunlight_info,
	_compute_pointlight_info
};

/**
 *	gather lighting information at given position
 *	\param illumination: emitting illumination source
 *	\param info: pointer to the information struct, that will be filled with resulting data
 *	\param position: current position in question
 */
void compute_lighting_info(const Illumination* illumination,LightInfo* info,vec3 position)
{
	_compute_illumination_info[illumination->type](illumination->light,info,position);
}
