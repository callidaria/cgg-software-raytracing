#include "image.h"

#define BUFFER_RESOLUTION_WIDTH 1920
#define BUFFER_RESOLUTION_HEIGHT 1080


int main(int argc,char** argv)
{
	// create final output buffer
	Image* output_buffer = create_image(BUFFER_RESOLUTION_WIDTH,BUFFER_RESOLUTION_HEIGHT);

	// write to image
	for (u8 i=0;i<50;++i)
	{
		for (u8 j=0;j<50;j++)
			write_pixel(output_buffer,400+i,400+j,(crgb){ 120,0,0 });
	}

	// write & finalize
	write_image(output_buffer,"../images/cout.ppm");
	destroy_image(output_buffer);
	return 0;
}
