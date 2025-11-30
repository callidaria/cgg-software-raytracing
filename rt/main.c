#include "gfx.h"


int main(int argc,char** argv)
{
	// create final output buffer
	Image* image_buffer = create_image(BUFFER_RESOLUTION_WIDTH,BUFFER_RESOLUTION_HEIGHT);
	Camera* camera = create_camera((vec3){ 0,1,-4 });
	update_camera(camera);

	// raytrace
	rt(image_buffer,camera);

	// write & finalize
	write_image(image_buffer,"../images/cout.ppm");
	destroy_camera(camera);
	destroy_image(image_buffer);
	return 0;
}
