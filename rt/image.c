#include "image.h"


/**
 *	generate image buffer
 *	\param width: buffer resolution width
 *	\param height: buffer resolution height
 *	\returns pointer to image in memory
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
 *	write image buffer to a file
 *	\param img: pointer to image buffer
 *	\param path: path to output file
 */
void write_image(const Image* img,char* path)
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
 *	delete image buffer from memory
 *	\param img: image buffer to remove from memory
 */
void destroy_image(Image* img)
{
	free(img->pixels);
	free(img);
}

/**
 *	set a pixel in image buffer
 *	\param img: pointer to image buffer
 *	\param x: x coordinate of pixel array in image data
 *	\param y: y coordinate of pixel array in image data
 *	\param c: rgb colour values to write to pixel
 */
void write_pixel(Image* img,u32 x,u32 y,crgb c)
{
	img->pixels[y*img->width+x] = c;
}

/**
 *	convert vector in R3 to rgb colourspace
 *	\param v: vector to convert to colourspace (r=x,g=y,b=z)
 *	\returns vector as colour
 */
crgb convertv3rgb(const vec3* v)
{
	return (crgb){ v->x*255,v->y*255,v->z*255 };
}
