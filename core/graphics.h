#ifndef DEF_GRAPHICS
#define DEF_GRAPHICS

#include "base.h"


typedef struct Buffer_
{
	unsigned int vao,vbo;
	// yes yes, static allocation please. there is no usecase for dynamic additions
	// all components can be adequatly instanced, there is no need for additions while running
	float* vertices;
	size_t vsize,rlen;
} Buffer;


// buffer utility
Buffer* buffer_generate();
void buffer_bind(Buffer* b);
void buffer_unbind();
void buffer_upload_vertices(Buffer* b);
void buffer_destroy(Buffer* b);


// shader utility
unsigned int shader_compile(const char* path,GLenum type);
// TODO: dont forget to destroy compiled shaders
unsigned int shader_pipeline(unsigned int vs,unsigned int fs);
void shader_enable(unsigned int sp);
void shader_disable();
void shader_define(unsigned int sp,const char* varname,
				   unsigned int dimension,unsigned int offset,unsigned int capacity);
// TODO: do all the uniform upload stuff


// generative utility
void generate_gpu_data(Buffer* qb,Buffer* pb,Buffer* lvb);


// render utility
void render(unsigned int sp,Buffer* b);


#endif
