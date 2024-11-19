#include "graphics.h"


/**
 *		BUFFER UTILITY
 */

// generate buffer to hold data for gpu
Buffer* buffer_generate()
{
	// buffer setup
	Buffer* out = (Buffer*)malloc(sizeof(Buffer));
	glGenVertexArrays(1,&out->vao);
	glGenBuffers(1,&out->vbo);
	// TODO add default values
	return out;
}

// TODO
ElementBuffer* elbuffer_generate()
{
	// buffer setup
	ElementBuffer* out = (ElementBuffer*)malloc(sizeof(ElementBuffer));
	glGenBuffers(1,&out->ibo);

	// default values
	out->active_range = 0;
	out->vsize = 0;
	out->rlen = 0;
	return out;
}

// buffer activation
// \param b: buffer to activate
void buffer_bind(Buffer* b)
{
	glBindVertexArray(b->vao);
}

// TODO
void elbuffer_bind(ElementBuffer* b)
{
	glBindBuffer(GL_ARRAY_BUFFER,b->ibo);
}

// deactivates ALL buffers
void buffer_unbind()
{
	glBindVertexArray(0);
	glBindBuffer(GL_ARRAY_BUFFER,0);
}

// upload buffer data
// \param b: buffer, supposed to upload
void buffer_upload_vertices(Buffer* b)
{
	glBindBuffer(GL_ARRAY_BUFFER,b->vbo);
	glBufferData(GL_ARRAY_BUFFER,b->vsize*b->rlen*sizeof(float),b->vertices,GL_STATIC_DRAW);
}

// TODO
void buffer_upload_elements(ElementBuffer* b)
{
	elbuffer_bind(b);
	glBufferData(GL_ARRAY_BUFFER,b->vsize*b->rlen*sizeof(float),b->elements,GL_STATIC_DRAW);
}

// destruction of buffer, don't forget, not too early, stop nagging
void buffer_destroy(Buffer* b)
{
	free(b->vertices);
	free(b);
}

// TODO
void elbuffer_destroy(ElementBuffer* b)
{
	free(b->elements);
	free(b);
}


/**
 *		SHADER UTILITY
 */

// compile shader, yes even geometry shaders, although they are not supported by my pipeline assembly process
// \param path: path to shader code file
// \param type: shader type as defined by gl enumerator
//				GL_VERTEX_SHADER -> vertex shader component
//				GL_FRAGMENT_SHADER -> fragment shader component
unsigned int shader_compile(const char* path,GLenum type)
{
	// open file
	FILE* __file = fopen(path,"rb");
	COMM_ERR_COND(!__file,"shader %s not found!",path);

	// snoop code size
	fseek(__file,0,SEEK_END);
	long __size = ftell(__file);
	rewind(__file);

	// read code into buffer
	//char* __source = (char*)malloc(__size+1);
	char* __source = (char*)calloc(__size+1,sizeof(char));
	if (!fread(__source,__size,1,__file)) COMM_ERR("issues reading shader code file %s",path);
	fclose(__file);

	// shader birth & compilation
	unsigned int out = glCreateShader(type);
	glShaderSource(out,1,&__source,NULL);
	glCompileShader(out);

	// shader debug logging
	int __status;
	glGetShaderiv(out,GL_COMPILE_STATUS,&__status);
	if (!__status)
	{
		char __log[512];
		glGetShaderInfoLog(out,512,NULL,__log);
		COMM_ERR("shader error -> %s : %s",path,__log);
	}

	// finalizing
	free(__source);
	return out;
}

// assembles shader pipeline from given vertex and fragment shader
// \param vs: compiled vertex shader
// \param fs: compiled fragment shader
unsigned int shader_pipeline(unsigned int vs,unsigned int fs)
{
	unsigned int out = glCreateProgram();
	glAttachShader(out,vs);
	glAttachShader(out,fs);
	glBindFragDataLocation(out,0,"outColour");
	glLinkProgram(out);
	return out;
}

// enable given shader
// \param sp: shader program to activate
void shader_enable(unsigned int sp)
{
	glUseProgram(sp);
}

// disable ALL shader programs
void shader_disable()
{
	glUseProgram(0);
}

// define shader upload parameter in vertex data
// \param sp: shader program
// \param varname: variable name in vertex shader
// \param dimension: dimension of float component (e.g. 1=float, 2=vec2, 3=vec3)
// \param offset: offset of upload data in upload raster
// \param capacity: maximum capacity of upload raster
void shader_define(unsigned int sp,const char* varname,
				   unsigned int dimension,unsigned int offset,unsigned int capacity)
{
	int __attrib = glGetAttribLocation(sp,varname);
	glEnableVertexAttribArray(__attrib);
	glVertexAttribPointer(__attrib,dimension,GL_FLOAT,GL_FALSE,
						  capacity*sizeof(float),(void*)(offset*sizeof(float)));
}

// TODO
void shader_define_element(unsigned int sp,const char* varname,
						   unsigned int dimension,unsigned int offset,unsigned int capacity)
{
	int __attrib = glGetAttribLocation(sp,varname);
	glEnableVertexAttribArray(__attrib);
	glVertexAttribPointer(__attrib,dimension,GL_FLOAT,GL_FALSE,
						  capacity*sizeof(float),(void*)(offset*sizeof(float)));
	glVertexAttribDivisor(__attrib,1);
}


/**
 *		GENERATIVE UTILITY
 */

// generate basic buffer data
// \param qb: quad buffer
// \param pb: projection buffer
// \param lvb: live view buffer
void generate_gpu_data(Buffer* qb,ElementBuffer* eqb,Buffer* pb,Buffer* lvb)
{
	// generate preview windows
	qb->vsize = 6;
	qb->rlen = 2;
	size_t qbsize = qb->rlen*qb->vsize*sizeof(float);
	qb->vertices = (float*)malloc(qbsize);
	memcpy(qb->vertices,(float[]){
			-.5f,.5f, .5f,-.5f, .5f,.5f,		// triangle 0
			.5f,-.5f, -.5f,.5f, -.5f,-.5f,		// triangle 1
		},qbsize);

	// setup preview element geometry
	eqb->active_range = 1;
	eqb->vsize = 2;
	eqb->rlen = 4;
	size_t eqbsize = eqb->rlen*eqb->vsize*sizeof(float);
	eqb->elements = (float*)malloc(eqbsize);
	memcpy(eqb->elements,(float[]){
			100.f,100.f, 100.f,100.f,
			100.f,100.f, 100.f,100.f,
		},eqbsize);

	// generate projective target
	// TODO

	// generate realtime preview target
	// TODO
}


/**
 *		RENDER UTILITY
 */

// draw buffer content
// \param sp: shader program to use when drawing buffer data
// \param b: buffer, to draw data from
// DEPRECATED: explicit render instructions have replaced this kind of rendercall. go home gamer gramps
void render(unsigned int sp,Buffer* b)
{
	buffer_bind(b);
	shader_enable(sp);
	glDrawArrays(GL_TRIANGLES,0,b->vsize);
}

// TODO
void render_windows(unsigned int sp,Buffer* b,ElementBuffer* e)
{
	buffer_bind(b);
	shader_enable(sp);
	buffer_upload_elements(e);
	glDrawArraysInstanced(GL_TRIANGLES,0,b->vsize,e->active_range);
}
