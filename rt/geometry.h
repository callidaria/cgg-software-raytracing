#ifndef RT_GEOMETRY_HEADER
#define RT_GEOMETRY_HEADER

#include "math.h"


// ----------------------------------------------------------------------------------------------------
// Data

typedef enum {
	NODE_TYPE_ROOT,
	NODE_TYPE_SPHERE,
	NODE_TYPE_COUNT
} NodeType;

typedef enum {
	LIGHT_TYPE_SUN,
	LIGHT_TYPE_POINT,
	LIGHT_TYPE_COUNT
} LightType;

typedef struct {
	vec3 center;
	f32 radius;
	f32 radius_sq;
	Material material;
} Sphere;

typedef struct SGNode {
	NodeType type;
	f32* geometry;
	struct SGNode* subsequent;
	u8 crr_child;
} SGNode;

typedef struct {
	vec3 direction;
	vec3 colour;
} SunLight;

typedef struct {
	vec3 position;
	vec3 colour;
	f32 constant;
	f32 linear;
	f32 quadratic;
} PointLight;

typedef struct {
	LightType type;
	f32* light;
} Illumination;

typedef struct {
	SunLight sunlights[SCN_SUNLIGHT_MCOUNT];
	PointLight pointlights[SCN_POINTLIGHT_MCOUNT];
	Illumination lighting[SCN_SUNLIGHT_MCOUNT+SCN_POINTLIGHT_MCOUNT];
	u32 crr_sunlight;
	u32 crr_pointlight;
	u32 crr_light;
} Lighting;

typedef struct {
	vec3 direction;
	f32 distance;
	vec3 intensity;
	vec3 influence;
} LightInfo;

typedef struct Scene {
	SGNode graph;
	Lighting lighting;
} Scene;


// ----------------------------------------------------------------------------------------------------
// Utility

// graph
void reserve_subsequent(SGNode*,u8);
SGNode* define_sphere(SGNode*,vec3,f32,Material);
void destroy_graph(SGNode*);

// scene
Scene* create_scene();
void destroy_scene(Scene*);
void test_intersection(const SGNode*,const Ray*,Intersection*);

// lighting
void create_light_sun(Lighting*,vec3,vec3);
void create_light_point_default(Lighting*,vec3,vec3);
void create_light_point(Lighting*,vec3,vec3,f32,f32,f32);
void compute_lighting_info(const Illumination*,LightInfo*,vec3);


#endif
