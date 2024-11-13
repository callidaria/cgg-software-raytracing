#ifndef DEF_BASE
#define DEF_BASE

#include <stdio.h>
#include <GL/glew.h>
#include <SDL2/SDL.h>
#include <SDL2/SDL_opengl.h>


// logger utility
// text colour definitions
#define LOG_WHITE "\e[0;30m"
#define LOG_RED "\e[1;31m"
#define LOG_GREEN "\e[1;32m"
#define LOG_YELLOW "\e[1;33m"
#define LOG_BLUE "\e[1;34m"
#define LOG_PURPLE "\e[1;35m"
#define LOG_CYAN "\e[1;36m"
#define LOG_GREY "\e[1;90m"
#define LOG_CLEAR "\e[0;39m"

// command definition
#define COMM_CLS printf("%s\n",LOG_CLEAR);
#define COMM_MSG(c,...) printf("%s",col),printf(__VA_ARGS__),COMM_CLS;
#define COMM_LOG(...) COMM_MSG(LOG_CLEAR,__VA_ARGS__);
#define COMM_ERR(...) printf("%serror: ",LOG_RED),printf(__VA_ARGS__),COMM_CLS;
#define COMM_SCC(...) COMM_MSG(LOG_GREEN,__VA_ARGS__);

// communicate by condition
#define COMM_MSG_COND(cnd,c,...) if (cnd) { COMM_MSG(c,__VA_ARGS__); }
#define COMM_ERR_COND(cnd,...) if (cnd) { COMM_ERR(__VA_ARGS__); }
#define COMM_LOG_COND(cnd,...) if (cnd) { COMM_LOG(__VA_ARGS__); }
#define COMM_MSG_FALLBACK(c,...) else { COMM_MSG(c,__VA_ARGS__); }
#define COMM_ERR_FALLBACK(...) else { COMM_ERR(__VA_ARGS__); }
#define COMM_LOG_FALLBACK(...) else { COMM_LOG(__VA_ARGS__); }
// end logger utility


// math data
typedef struct Vec2_ { float x,y;  } Vec2;
typedef struct Vec3_ { float x,y,z; } Vec3;


#endif
