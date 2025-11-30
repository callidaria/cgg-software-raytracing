#include "geometry.h"


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
SGNode* create_sphere(SGNode* node,vec3 center,f32 radius)
{
	SGNode* out = node->subsequent[node->crr_child++];
	out->type = NODE_TYPE_SPHERE;

	// write sphere
	Sphere* __Sphere = (Sphere*)malloc(sizeof(Sphere));
	__Sphere->center = center;
	__Sphere->radius = radius;
	__Sphere->radius_sq = radius*radius;
	out->geometry = (f32*)__Sphere;
}

/**
 *	delete scene graph from memory
 *	\param node: root node of graph subtree, that should be deleted
 */
void destroy_graph(SGNode* node)
{
	for (u8 i=0;i<crr_child;i++) destroy_graph(node->subsequent);
	free(node->subsequent);
	free(geometry);
}
