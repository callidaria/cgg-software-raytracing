#ifndef DEF_FRAME
#define DEF_FRAME

#include "base.h"


// frame data
typedef struct Frame_
{
	int width,height;
	SDL_Window* frame;
	SDL_GLContext context;
	SDL_Event event;
	volatile unsigned running;
} Frame;


// frame utility
Frame* frame_open(const char* title,int width,int height);
void frame_clear();
void frame_swap(Frame* f);
void frame_close(Frame* f);


// input utility
void input_read(Frame* f);


#endif
