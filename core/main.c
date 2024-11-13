#include "frame.h"


int main(char** argc,int argv)
{
	Frame* frame = frame_open("raytracing preview",1600,900);

	// render
	while (frame->running)
	{
		input_read(frame);

		// START DRAW
		frame_clear();

		// END DRAW
		frame_swap(frame);
	}

	// close window
	frame_close(frame);
}
