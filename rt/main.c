#include "image.h"


int main(int argc,char** argv)
{
	// create final output buffer
	Image* output_buffer = create_image(BUFFER_RESOLUTION_WIDTH,BUFFER_RESOLUTION_HEIGHT);
	Camera* camera = create_camera((vec3){ 0,1,-4 });
	update_camera(camera);

	// write to image
	for (u8 i=0;i<50;++i)
	{
		for (u8 j=0;j<50;j++)
			write_pixel(output_buffer,400+i,400+j,(crgb){ 120,0,0 });
	}

	// write & finalize
	write_image(output_buffer,"../images/cout.ppm");
	destroy_camera(camera);
	destroy_image(output_buffer);
	return 0;
}
