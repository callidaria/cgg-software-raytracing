#ifndef RT_MATH_HEADER
#define RT_MATH_HEADER

#include "config.h"

// ----------------------------------------------------------------------------------------------------
// Constants

static const f64 RAD_PI = M_PI/180.;
static const f64 DEG_PI = 180./M_PI;


// ----------------------------------------------------------------------------------------------------
// Data

// basic types
typedef struct { f32 x,y; } vec2;
typedef struct { f32 x,y,z; } vec3;
typedef struct { f32 x,y,z,w; } vec4;
typedef struct { f32 w,x,y,z; } quat;
typedef struct { f32 v[16]; } mat4x4;
typedef struct { u8 r,g,b; } crgb;
typedef struct { u8 r,g,b,a; } crgba;

typedef struct {
	vec3 origin;
	vec3 direction;
} Ray;

typedef enum {
	MATERIAL_PHONG,
	MATERIAL_COUNT
} Material;

typedef struct {
	u8 hit;
	vec3 position;
	vec3 normal;
	Material material;
} Intersection;

// camera
typedef struct {
	vec3 position;
	// TODO rotation
	f32 zfac;
	Ray* rays;
} Camera;


// ----------------------------------------------------------------------------------------------------
// Utility

// vector math
// vec2
vec2 addv2(vec2,vec2);
vec2 subv2(vec2,vec2);
vec2 mulv2(vec2,vec2);
f32 dotv2(vec2,vec2);
f32 crossv2(vec2,vec2);
vec2 divv2(vec2,vec2);
vec2 addv2s(vec2,f32);
vec2 subv2s(vec2,f32);
vec2 mulv2s(vec2,f32);
vec2 divv2s(vec2,f32);

// vec3
vec3 addv3(vec3,vec3);
vec3 subv3(vec3,vec3);
vec3 mulv3(vec3,vec3);
f32 dotv3(vec3,vec3);
vec3 crossv3(vec3,vec3);
vec3 divv3(vec3,vec3);
vec3 addv3s(vec3,f32);
vec3 subv3s(vec3,f32);
vec3 mulv3s(vec3,f32);
vec3 divv3s(vec3,f32);

// vec4
vec4 addv4(vec4,vec4);
vec4 subv4(vec4,vec4);
vec4 mulv4(vec4,vec4);
f32 dotv4(vec4,vec4);
vec4 divv4(vec4,vec4);
vec4 addv4s(vec4,f32);
vec4 subv4s(vec4,f32);
vec4 mulv4s(vec4,f32);
vec4 divv4s(vec4,f32);

// quat
// TODO

// unary vector operations
// vec2
f32 lengthv2(vec2);
vec2 normalizev2(vec2);

// vec3
f32 lengthv3(vec3);
vec3 normalizev3(vec3);

// vec4
f32 lengthv4(vec4);
vec4 normalizev4(vec4);

// matrixmath
// mat4
void addm44(mat4x4*,const mat4x4*,const mat4x4*);
void subm44(mat4x4*,const mat4x4*,const mat4x4*);
void mulm44(mat4x4*,const mat4x4*,const mat4x4*);
void divm44(mat4x4*,const mat4x4*,const mat4x4*);

// camera
Camera* create_camera(vec3);
void update_camera(Camera*);
void destroy_camera(Camera*);

// rays
void cast_ray(Camera*,s32,s32);
vec3 ray_calculate_position(const Ray*,f32);


#endif
