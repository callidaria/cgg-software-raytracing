#version 330 core

in vec2 position;

in vec2 offset;
in vec2 scale;

uniform vec2 fc_coordscale = vec2(1.)/vec2(1280.,720.);


void main()
{
    vec2 rpos = offset*fc_coordscale;
    vec2 rscl = scale*position*fc_coordscale;
    gl_Position = vec4(rpos+rscl,.0,1.);
}
