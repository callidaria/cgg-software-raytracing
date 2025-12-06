#include "gfx.h"


int main(int argc,char** argv)
{
	// create final output buffer
	Image* image_buffer = create_image(BUFFER_RESOLUTION_WIDTH,BUFFER_RESOLUTION_HEIGHT);

	// setup camera
	Camera* camera = create_camera((vec3){ 0,0,-4 });
	update_camera(camera);

	// setup scene
	Scene* scene = create_scene();
	reserve_subsequent(&scene->graph,1);
	define_sphere(&scene->graph,(vec3){ 0,0,0 },1.f,MATERIAL_PHONG);

	// raytrace
	rt(image_buffer,camera,scene);

	// write & finalize
	write_image(image_buffer,"../images/cout.ppm");
	destroy_scene(scene);
	destroy_camera(camera);
	destroy_image(image_buffer);
	return 0;
}
