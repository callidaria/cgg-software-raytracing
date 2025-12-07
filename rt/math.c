#include "math.h"


// ----------------------------------------------------------------------------------------------------
// Basic Math

static inline f32 clamp(f32 v,f32 a,f32 b) { return fmaxf(fminf(v,b),a); }


// ----------------------------------------------------------------------------------------------------
// Vector Math

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
vec2 divv2s(vec2 v,f32 s)
{
	f32 __Inv = 1.f/s;
	return (vec2){ v.x*__Inv,v.y*__Inv };
}

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
vec3 divv3s(vec3 v,f32 s)
{
	f32 __Inv = 1.f/s;
	return (vec3){ v.x*__Inv,v.y*__Inv,v.z*__Inv };
}

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
vec4 divv4s(vec4 v,f32 s)
{
	f32 __Inv = 1.f/s;
	return (vec4){ v.x*__Inv,v.y*__Inv,v.z*__Inv,v.w*__Inv };
}


/**
 *	unary operations over a vector in R2
 *	\param v: vector
 */
f32 lengthv2(vec2 v) { return sqrt(v.x*v.x+v.y*v.y); }

/**
 *	normalize a vector in R2
 *	\param v: vector to normalize
 *	\returns normalized version of passed vector
 */
vec2 normalizev2(vec2 v)
{
	f32 __Len = lengthv2(v);
	if (__Len==.0f) return v;
	return divv2s(v,__Len);
}

/**
 *	clamp a vector in R2 into a certain range
 *	\param v: vector to clamp
 *	\param a: minimum of clamping range
 *	\param b: maximum of clamping range
 *	\returns clamped vector
 */
vec2 clampv2(vec2 v,f32 a,f32 b) { return (vec2){ clamp(v.x,a,b),clamp(v.y,a,b) }; }

/**
 *	unary operations over a vector in R3
 *	\param v: vector
 */
f32 lengthv3(vec3 v) { return sqrtf(v.x*v.x+v.y*v.y+v.z*v.z); }

/**
 *	normalize a vector in R3
 *	\param v: vector to normalize
 *	\returns normalized version of passed vector
 */
vec3 normalizev3(vec3 v)
{
	f32 __Len = lengthv3(v);
	if (__Len==.0f) return v;
	return divv3s(v,__Len);
}

/**
 *	clamp a vector in R3 into a certain range
 *	\param v: vector to clamp
 *	\param a: minimum of clamping range
 *	\param b: maximum of clamping range
 *	\returns clamped vector
 */
vec3 clampv3(vec3 v,f32 a,f32 b)
{
	__m128 __A = _mm_set1_ps(a);
	__m128 __B = _mm_set1_ps(b);
	__m128 __Result = _mm_max_ps(_mm_min_ps(v._simd,__B),__A);
	_mm_store_ps(&v.x,__Result);
	return v;
}

/**
 *	unary operations over a vector in R4
 *	\param v: vector
 */
f32 lengthv4(vec4 v) { return sqrt(v.x*v.x+v.y*v.y+v.z*v.z+v.w*v.w); }

/**
 *	normalize a vector in R4
 *	\param v: vector to normalize
 *	\returns normalized version of passed vector
 */
vec4 normalizev4(vec4 v)
{
	f32 __Len = lengthv4(v);
	if (__Len==.0f) return v;
	return divv4s(v,__Len);
}

/**
 *	clamp a vector in R4 into a certain range
 *	\param v: vector to clamp
 *	\param a: minimum of clamping range
 *	\param b: maximum of clamping range
 *	\returns clamped vector
 */
vec4 clampv4(vec4 v,f32 a,f32 b)
{
	__m128 __A = _mm_set1_ps(a);
	__m128 __B = _mm_set1_ps(b);
	__m128 __Result = _mm_max_ps(_mm_min_ps(v._simd,__B),__A);
	_mm_store_ps(&v.x,__Result);
	return v;
}

/**
 *	add two matrices cell by cell
 *	\param r: resulting matrix after addition
 *	\param m0: left hand side matrix
 *	\param m1: right hand side matrix
 */
void addm44(mat4x4* r,const mat4x4* m0,const mat4x4* m1)
{
	f32* __R = (f32*)r;
	f32* __M0 = (f32*)m0;
	f32* __M1 = (f32*)m1;

	// extract rows
	__m128 __Ar0 = _mm_load_ps(&__M0[0]);
	__m128 __Ar1 = _mm_load_ps(&__M0[4]);
	__m128 __Ar2 = _mm_load_ps(&__M0[8]);
	__m128 __Ar3 = _mm_load_ps(&__M0[12]);
	__m128 __Br0 = _mm_load_ps(&__M1[0]);
	__m128 __Br1 = _mm_load_ps(&__M1[4]);
	__m128 __Br2 = _mm_load_ps(&__M1[8]);
	__m128 __Br3 = _mm_load_ps(&__M1[12]);

	// sum & store
	_mm_store_ps(&__R[0],_mm_add_ps(__Ar0,__Br0));
	_mm_store_ps(&__R[4],_mm_add_ps(__Ar1,__Br1));
	_mm_store_ps(&__R[8],_mm_add_ps(__Ar2,__Br2));
	_mm_store_ps(&__R[12],_mm_add_ps(__Ar3,__Br3));
}

/**
 *	subtract two matrices cell by cell
 *	\param r: resulting matrix after subtraction
 *	\param m0: left hand side matrix
 *	\param m1: right hand side matrix
 */
void subm44(mat4x4* r,const mat4x4* m0,const mat4x4* m1)
{
	f32* __R = (f32*)r;
	f32* __M0 = (f32*)m0;
	f32* __M1 = (f32*)m1;

	// extract rows
	__m128 __Ar0 = _mm_load_ps(&__M0[0]);
	__m128 __Ar1 = _mm_load_ps(&__M0[4]);
	__m128 __Ar2 = _mm_load_ps(&__M0[8]);
	__m128 __Ar3 = _mm_load_ps(&__M0[12]);
	__m128 __Br0 = _mm_load_ps(&__M1[0]);
	__m128 __Br1 = _mm_load_ps(&__M1[4]);
	__m128 __Br2 = _mm_load_ps(&__M1[8]);
	__m128 __Br3 = _mm_load_ps(&__M1[12]);

	// subtract & store
	_mm_store_ps(&__R[0],_mm_sub_ps(__Ar0,__Br0));
	_mm_store_ps(&__R[4],_mm_sub_ps(__Ar1,__Br1));
	_mm_store_ps(&__R[8],_mm_sub_ps(__Ar2,__Br2));
	_mm_store_ps(&__R[12],_mm_sub_ps(__Ar3,__Br3));
}

/**
 *	multiply two matrices cell by cell
 *	\param r: resulting matrix after multiplication
 *	\param m0: left hand side matrix
 *	\param m1: right hand side matrix
 */
void mulm44(mat4x4* r,const mat4x4* m0,const mat4x4* m1)
{
	f32* __R = (f32*)r;
	f32* __M0 = (f32*)m0;
	f32* __M1 = (f32*)m1;

	// load left hand side matrix
	__m128 __Rows[4];
	__Rows[0] = _mm_load_ps(&__M1[0]);
	__Rows[1] = _mm_load_ps(&__M1[4]);
	__Rows[2] = _mm_load_ps(&__M1[8]);
	__Rows[3] = _mm_load_ps(&__M1[12]);

	// iterate multiplication
	for (u8 i=0;i<4;++i)
	{
		__m128 __Cols[4];
		__Cols[0] = _mm_set1_ps(__M0[i*4]);
		__Cols[1] = _mm_set1_ps(__M0[i*4+1]);
		__Cols[2] = _mm_set1_ps(__M0[i*4+2]);
		__Cols[3] = _mm_set1_ps(__M0[i*4+3]);
		_mm_store_ps(&__R[i*4],_mm_add_ps(
				_mm_add_ps(_mm_mul_ps(__Rows[0],__Cols[0]),_mm_mul_ps(__Rows[1],__Cols[1])),
				_mm_add_ps(_mm_mul_ps(__Rows[2],__Cols[2]),_mm_mul_ps(__Rows[3],__Cols[3]))
			));
	}
}

/**
 *	divide two matrices cell by cell
 *	\param r: resulting matrix after divide
 *	\param m0: left hand side matrix
 *	\param m1: right hand side matrix
 */
void divm44(mat4x4* r,const mat4x4* m0,const mat4x4* m1)
{
	f32* __R = (f32*)r;
	f32* __M0 = (f32*)m0;
	f32* __M1 = (f32*)m1;

	// extract rows
	__m128 __Ar0 = _mm_load_ps(&__M0[0]);
	__m128 __Ar1 = _mm_load_ps(&__M0[4]);
	__m128 __Ar2 = _mm_load_ps(&__M0[8]);
	__m128 __Ar3 = _mm_load_ps(&__M0[12]);
	__m128 __Br0 = _mm_load_ps(&__M1[0]);
	__m128 __Br1 = _mm_load_ps(&__M1[4]);
	__m128 __Br2 = _mm_load_ps(&__M1[8]);
	__m128 __Br3 = _mm_load_ps(&__M1[12]);

	// subtract & store
	_mm_store_ps(&__R[0],_mm_div_ps(__Ar0,__Br0));
	_mm_store_ps(&__R[4],_mm_div_ps(__Ar1,__Br1));
	_mm_store_ps(&__R[8],_mm_div_ps(__Ar2,__Br2));
	_mm_store_ps(&__R[12],_mm_div_ps(__Ar3,__Br3));
}


// ----------------------------------------------------------------------------------------------------
// Camera

/**
 *	create a camera to cast rays from
 *	\param camera: pointer to camera in memory
 *	\param pos: camera position and origin of all initial rays
 */
void create_camera(Camera* camera,vec3 pos)
{
	camera->position = pos;
	camera->zfac = -(f32)BUFFER_RESOLUTION_HWIDTH/tan(PERSPECTIVE_CLIPPING_FOV*RAD_PI*.5f);
	camera->rays = (Ray*)malloc(sizeof(Ray)*BUFFER_RESOLUTION_PIXELS);
}

/**
 *	regenerate all camera rays
 *	\param cam: camera to update
 */
void update_camera(Camera* cam)
{
	for (s32 y=0;y<BUFFER_RESOLUTION_HEIGHT;++y)
	{
		for (s32 x=0;x<BUFFER_RESOLUTION_WIDTH;++x)
			cast_ray(cam,x,y);
	}
}

/**
 *	free memory for created camera and all it's rays
 *	\param cam: camera to be free'd
 */
void destroy_camera(Camera* cam)
{
	free(cam->rays);
}

/**
 *	regenerate ray at given coordinate
 *	\param cam: camera to update
 *	\param x: x-axis pixel coordinate
 *	\param y: y-axis pixel coordinate
 */
void cast_ray(Camera* cam,s32 x,s32 y)
{
	cam->rays[y*BUFFER_RESOLUTION_WIDTH+x] = (Ray){
		.origin = cam->position,
		.direction = normalizev3((vec3){
				(f32)x-BUFFER_RESOLUTION_HWIDTH,(f32)y-BUFFER_RESOLUTION_HHEIGHT,cam->zfac
			})
	};
}

/**
 *	calculate position on ray by factor
 *	\param ray: casted ray
 *	\param x: factor x for point of intersection
 *	\returns vector of resulting position
 */
vec3 ray_calculate_position(const Ray* ray,f32 x)
{
	return addv3(ray->origin,(mulv3s(ray->direction,x)));
}
