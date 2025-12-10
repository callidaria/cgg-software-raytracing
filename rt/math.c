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
 *	unary operations over a vector in R2
 *	\param v: vector
 */
f32 lengthv2(vec2 v) { return sqrt(v.x*v.x+v.y*v.y); }

vec2 normalizev2(vec2 v)
{
	f32 __Len = lengthv2(v);
	if (__Len==.0f) return v;
	return divv2s(v,__Len);
}

/**
 *	operations over two vectors in R3
 *	\param v0: left hand side vector
 *	\param v1: right hand side vector
 */
vec3 addv3(vec3 v0,vec3 v1) { return (vec3)_mm_add_ps(v0._simd,v1._simd); }

vec3 subv3(vec3 v0,vec3 v1) { return (vec3)_mm_sub_ps(v0._simd,v1._simd); }

vec3 mulv3(vec3 v0,vec3 v1) { return (vec3)_mm_mul_ps(v0._simd,v1._simd); }

f32 dotv3(vec3 v0,vec3 v1)
{
	v0._simd = _mm_mul_ps(v0._simd,v1._simd);
	__m128 __Addr = _mm_movehl_ps(v0._simd,v0._simd);
	v0._simd = _mm_add_ps(v0._simd,__Addr);
	__Addr = _mm_shuffle_ps(v0._simd,v0._simd,0x55);
	v0._simd = _mm_add_ps(v0._simd,__Addr);
	return _mm_cvtss_f32(v0._simd);
}

vec3 crossv3(vec3 v0,vec3 v1)
{
	__m128 __Addr0 = _mm_shuffle_ps(v0._simd,v0._simd,0x09);
	__m128 __Addr1 = _mm_shuffle_ps(v1._simd,v1._simd,0x12);
	__m128 __Subr0 = _mm_shuffle_ps(v0._simd,v0._simd,0x12);
	__m128 __Subr1 = _mm_shuffle_ps(v1._simd,v1._simd,0x09);
	return (vec3)_mm_sub_ps(_mm_mul_ps(__Addr0,__Addr1),_mm_mul_ps(__Subr0,__Subr1));
}

vec3 divv3(vec3 v0,vec3 v1) { return (vec3)_mm_div_ps(v0._simd,v1._simd); }

/**
 *	operations over a vector in R3 and a scalar
 *	\param v: variable side vector
 *	\param s: variable side scalar
 */
vec3 addv3s(vec3 v,f32 s)
{
	__m128 __S = _mm_set1_ps(s);
	return (vec3)_mm_add_ps(v._simd,__S);
}

vec3 subv3s(vec3 v,f32 s)
{
	__m128 __S = _mm_set1_ps(s);
	return (vec3)_mm_sub_ps(v._simd,__S);
}

vec3 mulv3s(vec3 v,f32 s)
{
	__m128 __S = _mm_set1_ps(s);
	return (vec3)_mm_mul_ps(v._simd,__S);
}

vec3 divv3s(vec3 v,f32 s)
{
	__m128 __S = _mm_set1_ps(1.f/s);
	return (vec3)_mm_mul_ps(v._simd,__S);
}

/**
 *	unary operations over a vector in R3
 *	\param v: vector
 */
f32 lengthv3(vec3 v)
{
	v._simd = _mm_mul_ps(v._simd,v._simd);
	__m128 __Addr = _mm_movehl_ps(v._simd,v._simd);
	v._simd = _mm_add_ps(v._simd,__Addr);
	__Addr = _mm_shuffle_ps(v._simd,v._simd,0x55);
	v._simd = _mm_add_ps(v._simd,__Addr);
	return _mm_cvtss_f32(_mm_sqrt_ps(v._simd));
}

vec3 normalizev3(vec3 v)
{
	f32 __Len = lengthv3(v);
	if (!__Len) return v;
	return divv3s(v,__Len);
}

/**
 *	operations over two vectors in R4
 *	\param v0: left hand side vector
 *	\param v1: right hand side vector
 */
vec4 addv4(vec4 v0,vec4 v1) { return (vec4)_mm_add_ps(v0._simd,v1._simd); }

vec4 subv4(vec4 v0,vec4 v1) { return (vec4)_mm_sub_ps(v0._simd,v1._simd); }

vec4 mulv4(vec4 v0,vec4 v1) { return (vec4)_mm_mul_ps(v0._simd,v1._simd); }

f32 dotv4(vec4 v0,vec4 v1)
{
	v0._simd = _mm_mul_ps(v0._simd,v1._simd);
	__m128 __Addr = _mm_movehl_ps(v0._simd,v0._simd);
	v0._simd = _mm_add_ps(v0._simd,__Addr);
	__Addr = _mm_shuffle_ps(v0._simd,v0._simd,0x55);
	v0._simd = _mm_add_ps(v0._simd,__Addr);
	return _mm_cvtss_f32(v0._simd);
}

vec4 divv4(vec4 v0,vec4 v1) { return (vec4)_mm_div_ps(v0._simd,v1._simd); }

/**
 *	operations over a vector in R4 and a scalar
 *	\param v: variable side vector
 *	\param s: variable side scalar
 */
vec4 addv4s(vec4 v,f32 s)
{
	__m128 __S = _mm_set1_ps(s);
	return (vec4)_mm_add_ps(v._simd,__S);
}

vec4 subv4s(vec4 v,f32 s)
{
	__m128 __S = _mm_set1_ps(s);
	return (vec4)_mm_sub_ps(v._simd,__S);
}

vec4 mulv4s(vec4 v,f32 s)
{
	__m128 __S = _mm_set1_ps(s);
	return (vec4)_mm_mul_ps(v._simd,__S);
}

vec4 divv4s(vec4 v,f32 s)
{
	__m128 __S = _mm_set1_ps(1.f/s);
	return (vec4)_mm_mul_ps(v._simd,__S);
}

/**
 *	unary operations over a vector in R4
 *	\param v: vector
 */
f32 lengthv4(vec4 v)
{
	v._simd = _mm_mul_ps(v._simd,v._simd);
	__m128 __Addr = _mm_movehl_ps(v._simd,v._simd);
	v._simd = _mm_add_ps(v._simd,__Addr);
	__Addr = _mm_shuffle_ps(v._simd,v._simd,0x55);
	v._simd = _mm_add_ps(v._simd,__Addr);
	return _mm_cvtss_f32(_mm_sqrt_ps(v._simd));
}

vec4 normalizev4(vec4 v)
{
	f32 __Len = lengthv4(v);
	if (!__Len) return v;
	return divv4s(v,__Len);
}

/**
 *	clamp a vector into a certain range
 *	\param v: vector to clamp
 *	\param a: minimum of clamping range
 *	\param b: maximum of clamping range
 *	\returns clamped vector
 */
vec2 clampv2(vec2 v,f32 a,f32 b) { return (vec2){ clamp(v.x,a,b),clamp(v.y,a,b) }; }

vec3 clampv3(vec3 v,f32 a,f32 b)
{
	__m128 __A = _mm_set1_ps(a);
	__m128 __B = _mm_set1_ps(b);
	v._simd = _mm_max_ps(_mm_min_ps(v._simd,__B),__A);
	return v;
}

vec4 clampv4(vec4 v,f32 a,f32 b)
{
	__m128 __A = _mm_set1_ps(a);
	__m128 __B = _mm_set1_ps(b);
	v._simd = _mm_max_ps(_mm_min_ps(v._simd,__B),__A);
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
	r->_simd[0] = _mm_add_ps(m0->_simd[0],m1->_simd[0]);
	r->_simd[1] = _mm_add_ps(m0->_simd[1],m1->_simd[1]);
	r->_simd[2] = _mm_add_ps(m0->_simd[2],m1->_simd[2]);
	r->_simd[3] = _mm_add_ps(m0->_simd[3],m1->_simd[3]);
}

/**
 *	subtract two matrices cell by cell
 *	\param r: resulting matrix after subtraction
 *	\param m0: left hand side matrix
 *	\param m1: right hand side matrix
 */
void subm44(mat4x4* r,const mat4x4* m0,const mat4x4* m1)
{
	r->_simd[0] = _mm_sub_ps(m0->_simd[0],m1->_simd[0]);
	r->_simd[1] = _mm_sub_ps(m0->_simd[1],m1->_simd[1]);
	r->_simd[2] = _mm_sub_ps(m0->_simd[2],m1->_simd[2]);
	r->_simd[3] = _mm_sub_ps(m0->_simd[3],m1->_simd[3]);
}

/**
 *	multiply two matrices cell by cell
 *	\param r: resulting matrix after multiplication
 *	\param m0: left hand side matrix
 *	\param m1: right hand side matrix
 */
void mulm44(mat4x4* r,const mat4x4* m0,const mat4x4* m1)
{
	for (u8 i=0;i<4;++i)
	{
		__m128 __Cols[4];
		__Cols[0] = _mm_set1_ps(m0->v[i*4]);
		__Cols[1] = _mm_set1_ps(m0->v[i*4+1]);
		__Cols[2] = _mm_set1_ps(m0->v[i*4+2]);
		__Cols[3] = _mm_set1_ps(m0->v[i*4+3]);
		r->_simd[i] = _mm_add_ps(
				_mm_add_ps(_mm_mul_ps(r->_simd[0],__Cols[0]),_mm_mul_ps(r->_simd[1],__Cols[1])),
				_mm_add_ps(_mm_mul_ps(r->_simd[2],__Cols[2]),_mm_mul_ps(r->_simd[3],__Cols[3]))
			);
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
	r->_simd[0] = _mm_div_ps(m0->_simd[0],m1->_simd[0]);
	r->_simd[1] = _mm_div_ps(m0->_simd[1],m1->_simd[1]);
	r->_simd[2] = _mm_div_ps(m0->_simd[2],m1->_simd[2]);
	r->_simd[3] = _mm_div_ps(m0->_simd[3],m1->_simd[3]);
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
