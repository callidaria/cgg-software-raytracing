#ifndef RT_IMAGE_HEADER
#define RT_IMAGE_HEADER

#include "math.h"


// ----------------------------------------------------------------------------------------------------
// Data

typedef struct {
	u32 width,height;
	crgb* pixels;
} Image;


// ----------------------------------------------------------------------------------------------------
// Utility

// image memory utility
Image* create_image(u32,u32);
void write_image(Image*,char*);
void destroy_image(Image*);

// image pixel utility
void write_pixel(Image*,u32,u32,crgb);


#endif
