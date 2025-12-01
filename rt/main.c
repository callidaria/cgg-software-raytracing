#include "gfx.h"


int main(int argc,char** argv)
{
	// create final output buffer
	Image* image_buffer = create_image(BUFFER_RESOLUTION_WIDTH,BUFFER_RESOLUTION_HEIGHT);

	// setup camera
	Camera* camera = create_camera((vec3){ 0,1,-4 });
	update_camera(camera);

	// setup scene
	SGNode* scene = create_graph();
	reserve_subsequent(scene,4);
	define_sphere(scene,(vec3){ 0,0,0 },1.f);

	// raytrace
	rt(image_buffer,camera,scene);

	// write & finalize
	write_image(image_buffer,"../images/cout.ppm");
	destroy_graph(scene);
	destroy_camera(camera);
	destroy_image(image_buffer);
	return 0;
}
