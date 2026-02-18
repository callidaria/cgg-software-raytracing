#include "linguistics.h"


/**
 *	definition of all interpreter modi
 *	\param root: rootnode of processing subtree
 *	\param file: dialectics file
 *	\param status: interpreter read status, this will be changed by command while interpreting
 *	\param cmd: direct command
 */
#define LINGUISTICS_INTERPRETER_PARAMETERS SGNode* root,FILE* file,InterpreterStatus* status,const char* cmd
typedef void (*_linguistics_interpreter_procedure)(LINGUISTICS_INTERPRETER_PARAMETERS);

// starting command info, expecting node depth indent
void _interpreter_wait_for_command(LINGUISTICS_INTERPRETER_PARAMETERS)
{
	// TODO
}

_linguistics_interpreter_procedure _interpret_liguistics[INTERPRETER_STATUS_COMMANDCOUNT] = {
	_interpreter_wait_for_command,
};

/**
 *	interpreter for linguistics logic
 *	target functionality of this feature is to read a scenegraph tree & cram it into linear memory space,
 *	utilizing only a singular memory allocation for the entire tree structure. later the tree nodes can be
 *	iterated like expected.
 *	this dialects linguistics work, utilizing the following syntacitc rules:
 *		(<+>+<->+e) COMMAND <command_params>
 *		<+> + <->: is an optional char value to indicate tree depth based on previous depth
 *	COMMANDS:
 *		sphere <position vec3>
 *		tree <subtree_name>: inserts subtree
 *	type specifications:
 *		int/float:	n
 *		vec2:		(x,y)
 *		vec3:		(x,y,z)
 *		vec4:		(w,x,y,z)
 *		quat:		(x,y,z,w)
 *		string:		<text>
 *	subtree definition:
 *		to define a subtree the developer may use "st <subtree_name>:" or "sti <subtree_name>:"
 *		"ste" is used to mark end of subtree definitione
 *		to insert subtree in another tree definition use (+)n tree <subtree_name>
 *	"<" and ">" are not to be interpreted as literal syntax, they specify beginning and end of placeholders
 *	\param graph: pointer to rootnode of scene graph (this can also be the root of a subtree)
 *	\param path: path to scene definition file
 */
void interpret_linguistics(SGNode* graph,const char* path)
{
	// open scene definition file
	FILE* __File = fopen(path);
	if (!__File)
	{
		printf("error: scene definition file could not be found");
		return;
	}

	// preprocess to assemble tree structure
	while (fscanf(__File,"%s",__CMD)!=EOF)
	{
		// TODO
	}

	// iterate definitions
	fseek(__File,0,0);  // TODO how does this work again?
	char __CMD[LINGUISTICS_COMMAND_CHARLEN];
	while (fscanf(__File,"%s",__CMD)!=EOF)
	{
		// TODO
	}

	// finish interpretation
	fclose(__File);
}
