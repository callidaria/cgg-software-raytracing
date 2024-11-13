#ifndef DEF_GRAPHICS
#define DEF_GRAPHICS

#include "base.h"


// graphics data
typedef struct Buffer_
{
	unsigned int vao,vbo;
} Buffer;


// graphics utility
Buffer* buffer_generate();
void buffer_bind(Buffer* b);
void buffer_unbind();
void buffer_upload_vertices(Buffer* b,float* vertices,size_t range);


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
// TODO


#endif
