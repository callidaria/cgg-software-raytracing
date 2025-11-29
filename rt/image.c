#include "image.h"


/**
 *	TODO
 */
Image* create_image(u32 width,u32 height)
{
	Image* img = (Image*)malloc(sizeof(Image));
	img->width = width;
	img->height = height;
	img->pixels = (crgb*)malloc(sizeof(crgb)*width*height);
	memset(img->pixels,0,sizeof(crgb)*width*height);
	return img;
}

/**
 *	TODO
 */
void write_image(Image* img,char* path)
{
	// open file & format
	FILE* __File;
	__File = fopen(path,"wb");

	// format & write data
	fprintf(__File,"P6\n# cggrt \n%d %d\n%d\n",img->width,img->height,255);
	fwrite(img->pixels,sizeof(crgb),img->width*img->height,__File);
	fclose(__File);
}

/**
 *	TODO
 */
void destroy_image(Image* img)
{
	free(img->pixels);
	free(img);
}

/**
 *	TODO
 */
void write_pixel(Image* img,u32 x,u32 y,crgb c)
{
	img->pixels[y*img->width+x] = c;
}
