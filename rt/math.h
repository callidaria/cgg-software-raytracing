#ifndef RT_MATH_HEADER
#define RT_MATH_HEADER

#include "basic.h"


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

// camera
// TODO


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

// matrixmath
// mat4
void addm44(mat4x4*,const mat4x4*,const mat4x4*);
void subm44(mat4x4*,const mat4x4*,const mat4x4*);
void mulm44(mat4x4*,const mat4x4*,const mat4x4*);
void divm44(mat4x4*,const mat4x4*,const mat4x4*);


#endif
