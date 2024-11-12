#include "frame.h"


int main(char** argc,int argv)
{
	Frame* frame = frame_open("raytracing preview",1600,900);

	// render
	unsigned running = 1;
	SDL_Event event;
	while (running)
	{
		// input
		while (SDL_PollEvent(&event))
		{
			switch (event.type)
			{
			case SDL_QUIT: running = 0;
			}
		}

		// START DRAW
		frame_clear();

		// END DRAW
		frame_swap(frame);
	}

	// close window
	frame_close(frame);
}
