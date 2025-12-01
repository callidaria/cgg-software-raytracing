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
 *	\returns pointer to overwritten child node
 */
SGNode* define_sphere(SGNode* node,vec3 center,f32 radius)
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
	out->geometry = (f32*)__Sphere;
}

// recursive helper function that deletes all subtrees of the rootnode
void _destroy_graph(SGNode* node)
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
	hit->hit = 1||hit->hit;
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
