#include "gfx.h"


int main(int argc,char** argv)
{
	// create final output buffer
	Image* image_buffer = create_image(BUFFER_RESOLUTION_WIDTH,BUFFER_RESOLUTION_HEIGHT);
	Scene* scene = create_scene((vec3){ 0,0,-4 });

	// setup scene
	reserve_subsequent(&scene->graph,1);
	//define_sphere(&scene->graph,(vec3){ 0,0,0 },1.f,MATERIAL_PHONG,(vec4){ 0,0,.5f,1 });
	define_box(&scene->graph,(vec3){ 0,0,0 },1,1,1,MATERIAL_PHONG,(vec4){ 1.f,1.f,1.f,1.f });
	create_light_sun(&scene->lighting,(vec3){ 1,1,1 },(vec3){ 1,1,1 });

	// raytrace
	rt(image_buffer,scene);

	// write & finalize
	write_image(image_buffer,"../images/cout.ppm");
	destroy_scene(scene);
	destroy_image(image_buffer);
	return 0;
}
