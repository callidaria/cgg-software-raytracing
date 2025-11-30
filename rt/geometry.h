#ifndef RT_GEOMETRY_HEADER
#define RT_GEOMETRY_HEADER

#include "math.h"


// ----------------------------------------------------------------------------------------------------
// Data

typedef enum {
	NODE_TYPE_ROOT,
	NODE_TYPE_SPHERE,
} NodeType;

typedef struct {
	vec3 center;
	f32 radius;
	f32 radius_sq;
} Sphere;

typedef struct SGNode {
	NodeType type;
	f32* geometry;
	struct _SGNode* subsequent;
	u8 crr_child;
} SGNode;


// ----------------------------------------------------------------------------------------------------
// Utility

// graph
SGNode* create_graph();
void reserve_subsequent(SGNode*,u8);
SGNode* define_sphere(SGNode*,vec3,f32);
void destroy_graph(SGNode*);


#endif
