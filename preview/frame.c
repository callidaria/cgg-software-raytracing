#include "frame.h"


/**
 *		FRAME UTILITY
 */

Frame* frame_open(const char* title,int width,int height)
{
	// data definition
	Frame* out = (Frame*)malloc(sizeof(Frame));
	out->width = width;
	out->height = height;

	// basic setup
	SDL_Init(SDL_INIT_EVERYTHING);
	SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK,SDL_GL_CONTEXT_PROFILE_CORE);
	SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION,3);
	SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION,3);
	SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE,8);

	// setup window
	out->frame = SDL_CreateWindow(title,0,0,width,height,SDL_WINDOW_OPENGL);
	out->context = SDL_GL_CreateContext(out->frame);

	// opengl setup
	glewInit();
	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LEQUAL);
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);  // TODO: do front for preview cubemap later after testing
	glFrontFace(GL_CCW);
	glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
	glViewport(0,0,width,height);

	// set standard values and out
	glClearColor(0,0,0,0);
	out->running = 1;
	return out;
}

void frame_clear()
{
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
}

void frame_swap(Frame* f)
{
	SDL_GL_SwapWindow(f->frame);
}

void frame_close(Frame* f)
{
	SDL_GL_DeleteContext(f->context);
	SDL_Quit();
	free(f);
}


/**
 *		INPUT UTILITY
 */

void input_read(Frame* f)
{
	while (SDL_PollEvent(&f->event))
	{
		switch (f->event.type)
		{
		case SDL_QUIT: f->running = 0;
		}
	}
}
