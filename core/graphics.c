#include "graphics.h"


/**
 *		BUFFER UTILITY
 */

Buffer* buffer_generate()
{
	Buffer* out = (Buffer*)malloc(sizeof(Buffer));
	glGenVertexArrays(1,&out->vao);
	glGenBuffers(1,&out->vbo);
	return out;
}

void buffer_bind(Buffer* b)
{
	glBindVertexArray(b->vao);
}

void buffer_unbind()
{
	glBindVertexArray(0);
	glBindBuffer(GL_ARRAY_BUFFER,0);
}

void buffer_upload_vertices(Buffer* b,float* vertices,size_t range)
{
	glBindBuffer(GL_ARRAY_BUFFER,b->vbo);
	glBufferData(GL_ARRAY_BUFFER,range,vertices,GL_STATIC_DRAW);
}


/**
 *		SHADER UTILITY
 */

unsigned int shader_compile(const char* path,GLenum type)
{
	// open file
	FILE* __File = fopen(path,"rb");
	COMM_ERR_COND(!__File,"shader %s not found!",path);

	// snoop code size
	fseek(__File,0L,SEEK_END);
	long __Size = ftell(__File);
	rewind(__File);

	// read code into buffer
	char* __Source = (char*)malloc(__Size);
	if (!fread(__Source,__Size,1,__File)) COMM_ERR("issues reading shader code file %s",path);
	fclose(__File);

	// shader birth & compilation
	unsigned int out = glCreateShader(type);
	glShaderSource(out,1,&__Source,NULL);
	glCompileShader(out);

	// shader debug logging
	int __Status;
	glGetShaderiv(out,GL_COMPILE_STATUS,&__Status);
	if (!__Status)
	{
		char __Log[512];
		glGetShaderInfoLog(out,512,NULL,__Log);
		COMM_ERR("shader error -> %s : %s",path,__Log);
	}

	// finalizing
	free(__Source);
	return out;
}

unsigned int shader_pipeline(unsigned int vs,unsigned int fs)
{
	unsigned int out = glCreateProgram();
	glAttachShader(out,vs);
	glAttachShader(out,fs);
	glBindFragDataLocation(out,0,"outColour");
	glLinkProgram(out);
	return out;
}

void shader_enable(unsigned int sp)
{
	glUseProgram(sp);
}

void shader_disable()
{
	glUseProgram(0);
}

void shader_define(unsigned int sp,const char* varname,
				   unsigned int dimension,unsigned int offset,unsigned int capacity)
{
	int __Attribute = glGetAttribLocation(sp,varname);
	glEnableVertexAttribArray(__Attribute);
	glVertexAttribPointer(__Attribute,dimension,GL_FLOAT,GL_FALSE,
						  capacity*sizeof(float),(void*)(offset*sizeof(float)));
}
