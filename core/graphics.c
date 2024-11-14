#include "graphics.h"


/**
 *		BUFFER UTILITY
 */

// generate buffer to hold data for gpu
Buffer* buffer_generate()
{
	Buffer* out = (Buffer*)malloc(sizeof(Buffer));
	glGenVertexArrays(1,&out->vao);
	glGenBuffers(1,&out->vbo);
	return out;
}

// buffer activation
// \param b: buffer to activate
void buffer_bind(Buffer* b)
{
	glBindVertexArray(b->vao);
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

// destruction of buffer, don't forget, not too early, stop nagging
void buffer_destroy(Buffer* b)
{
	free(b->vertices);
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
	int __Attribute = glGetAttribLocation(sp,varname);
	glEnableVertexAttribArray(__Attribute);
	glVertexAttribPointer(__Attribute,dimension,GL_FLOAT,GL_FALSE,
						  capacity*sizeof(float),(void*)(offset*sizeof(float)));
}


/**
 *		GENERATIVE UTILITY
 */

// generate basic buffer data
// \param qb: quad buffer
// \param pb: projection buffer
// \param lvb: live view buffer
void generate_gpu_data(Buffer* qb,Buffer* pb,Buffer* lvb)
{
	// generate preview quads
	qb->vsize = 6;
	qb->rlen = 2;
	size_t qbsize = 2*qb->vsize*sizeof(float);
	qb->vertices = (float*)malloc(qbsize);
	memcpy(qb->vertices,(float[]){
			-.5f,.5f, .5f,-.5f, .5f,.5f,		// triangle 0
			.5f,-.5f, -.5f,.5f, -.5f,-.5f,		// triangle 1
		},qbsize);

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
void render(unsigned int sp,Buffer* b)
{
	buffer_bind(b);
	shader_enable(sp);
	glDrawArrays(GL_TRIANGLES,0,b->vsize);
}
