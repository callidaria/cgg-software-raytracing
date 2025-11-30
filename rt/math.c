#include "math.h"


/**
 *	operations over two vectors in R2
 *	\param v0: left hand side vector
 *	\param v1: right hand side vector
 */
vec2 addv2(vec2 v0,vec2 v1) { return (vec2){ v0.x+v1.x,v0.y+v1.y }; }
vec2 subv2(vec2 v0,vec2 v1) { return (vec2){ v0.x-v1.x,v0.y-v1.y }; }
vec2 mulv2(vec2 v0,vec2 v1) { return (vec2){ v0.x*v1.x,v0.y*v1.y }; }
f32 dotv2(vec2 v0,vec2 v1) { return v0.x*v1.x+v0.y*v1.y; }
f32 crossv2(vec2 v0,vec2 v1) { return v0.x*v1.y-v0.y*v1.x; }
vec2 divv2(vec2 v0,vec2 v1) { return (vec2){ v0.x/v1.x,v0.y/v1.y }; }

/**
 *	operations over a vector in R2 and a scalar
 *	\param v: variable side vector
 *	\param s: variable side scalar
 */
vec2 addv2s(vec2 v,f32 s) { return (vec2){ v.x+s,v.y+s }; }
vec2 subv2s(vec2 v,f32 s) { return (vec2){ v.x-s,v.y-s }; }
vec2 mulv2s(vec2 v,f32 s) { return (vec2){ v.x*s,v.y*s }; }
vec2 divv2s(vec2 v,f32 s) { return (vec2){ v.x/s,v.y/s }; }

/**
 *	operations over two vectors in R3
 *	\param v0: left hand side vector
 *	\param v1: right hand side vector
 */
vec3 addv3(vec3 v0,vec3 v1) { return (vec3){ v0.x+v1.x,v0.y+v1.y,v0.z+v1.z }; }
vec3 subv3(vec3 v0,vec3 v1) { return (vec3){ v0.x-v1.x,v0.y-v1.y,v0.z-v1.z }; }
vec3 mulv3(vec3 v0,vec3 v1) { return (vec3){ v0.x*v1.x,v0.y*v1.y,v0.z*v1.z }; }
f32 dotv3(vec3 v0,vec3 v1) { return v0.x*v1.x+v0.y*v1.y+v0.z*v1.z; }
vec3 crossv3(vec3 v0,vec3 v1) { return (vec3){ v0.x*v1.y,v0.y+v1.x }; }
vec3 divv3(vec3 v0,vec3 v1) { return (vec3){ v0.x/v1.x,v0.y/v1.y,v0.z/v1.z }; }

/**
 *	operations over a vector in R3 and a scalar
 *	\param v: variable side vector
 *	\param s: variable side scalar
 */
vec3 addv3s(vec3 v,f32 s) { return (vec3){ v.x+s,v.y+s,v.z+s }; }
vec3 subv3s(vec3 v,f32 s) { return (vec3){ v.x-s,v.y-s,v.z-s }; }
vec3 mulv3s(vec3 v,f32 s) { return (vec3){ v.x*s,v.y*s,v.z*s }; }
vec3 divv3s(vec3 v,f32 s) { return (vec3){ v.x/s,v.y/s,v.z/s }; }

/**
 *	operations over two vectors in R4
 *	\param v0: left hand side vector
 *	\param v1: right hand side vector
 */
vec4 addv4(vec4 v0,vec4 v1) { return (vec4){ v0.x+v1.x,v0.y+v1.y,v0.z+v1.z,v0.w+v1.w }; }
vec4 subv4(vec4 v0,vec4 v1) { return (vec4){ v0.x-v1.x,v0.y-v1.y,v0.z-v1.z,v0.w-v1.w }; }
vec4 mulv4(vec4 v0,vec4 v1) { return (vec4){ v0.x*v1.x,v0.y*v1.y,v0.z*v1.z,v0.w*v1.w }; }
f32 dotv4(vec4 v0,vec4 v1) { return v0.x*v1.x+v0.y*v1.y+v0.z*v1.z+v0.w*v1.w; }
vec4 divv4(vec4 v0,vec4 v1) { return (vec4){ v0.x/v1.x,v0.y/v1.y,v0.z/v1.z,v0.w/v1.w }; }

/**
 *	operations over a vector in R4 and a scalar
 *	\param v: variable side vector
 *	\param s: variable side scalar
 */
vec4 addv4s(vec4 v,f32 s) { return (vec4){ v.x+s,v.y+s,v.z+s,v.w+s }; }
vec4 subv4s(vec4 v,f32 s) { return (vec4){ v.x-s,v.y-s,v.z-s,v.w-s }; }
vec4 mulv4s(vec4 v,f32 s) { return (vec4){ v.x*s,v.y*s,v.z*s,v.w*s }; }
vec4 divv4s(vec4 v,f32 s) { return (vec4){ v.x/s,v.y/s,v.z/s,v.w/s }; }

// matrixmath
// mat4
/*
void addm44(mat4x4* m0,mat4x4* m1);
void subm44(mat4x4* m0,mat4x4* m1);
void mulm44(mat4x4* m0,mat4x4* m1);
void divm44(mat4x4* m0,mat4x4* m1);
*/
