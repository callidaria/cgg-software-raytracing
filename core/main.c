#include "frame.h"
#include "graphics.h"


int main(char** argc,int argv)
{
	Frame* frame = frame_open("raytracing preview",1600,900);

	// geometry definition
	Buffer* buffer_quad = buffer_generate();
	ElementBuffer* elbuffer_quad = elbuffer_generate();
	generate_gpu_data(buffer_quad,elbuffer_quad,NULL,NULL);

	// shader pipelines
	buffer_bind(buffer_quad);
	buffer_upload_vertices(buffer_quad);
	unsigned int pipeline_quad = shader_pipeline(
			shader_compile("./shader/quad.vs",GL_VERTEX_SHADER),
			shader_compile("./shader/quad.fs",GL_FRAGMENT_SHADER)
		);
	shader_enable(pipeline_quad);
	shader_define(pipeline_quad,"position",2,0,2);
	elbuffer_bind(elbuffer_quad);
	shader_define_element(pipeline_quad,"offset",2,0,4);
	shader_define_element(pipeline_quad,"scale",2,2,4);

	// render
	while (frame->running)
	{
		input_read(frame);

		// START DRAW
		frame_clear();
		render_windows(pipeline_quad,buffer_quad,elbuffer_quad);
		frame_swap(frame);
		// END DRAW
	}

	// close window
	elbuffer_destroy(elbuffer_quad);
	buffer_destroy(buffer_quad);
	frame_close(frame);
}
