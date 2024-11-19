#include <stdlib.h>
#include <stdio.h>


int main(int argc,char** argv)
{
	if (argc<2)
	{
		printf("sample size has to be passed\n");
		return -1;
	}
	int samples = atoi(argv[1]);
	double* sequence = (double*)malloc(samples);

	// lookup table file
	FILE* file = fopen("./res/lut/vdcorput","w");

	// iterate samples
	for (int i=0;i<samples;i++)
	{
		if (i>0) fprintf(file,",");
		unsigned vdc = (i<<16)|(i>>16);
		vdc = (vdc&0x55555555)<<1|(vdc&0xAAAAAAAA)>>1;
		vdc = (vdc&0x33333333)<<2|(vdc&0xCCCCCCCC)>>2;
		vdc = (vdc&0x0F0F0F0F)<<4|(vdc&0xF0F0F0F0)>>4;
		vdc = (vdc&0x00FF00FF)<<8|(vdc&0xFF00FF00)>>8;
		float fvdc = (float)vdc*2.3283064365386963e-10;
		fprintf(file,"%f,%f",(double)i/(double)samples,fvdc);
	}

	// destruction
	fclose(file);
	free(sequence);
}
