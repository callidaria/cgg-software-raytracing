#ifndef RT_LINGUISTICS_HEADER
#define RT_LINGUISTICS_HEADER

#include "gfx.h"

#define LINGUISTICS_COMMAND_CHARLEN 32;


enum InterpreterStatus
{
	INTERPRETER_STATUS_NEUTRAL,
	INTERPRETER_STATUS_COMMANDCOUNT
};

void interpret_linguistics(const char* path);


#endif
