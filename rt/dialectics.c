#include "dialectics.h"


/**
 *	interpreter for dialectics logic
 *	dialectics works, utilizing the following syntacitc rules:
 *		<n> COMMAND <command_params>
 *		<n>: n is an integer value to indicate tree depth at node
 *	COMMANDS:
 *		sphere <
 *		tree <subtree_name>: inserts subtree
 *	subtree definition:
 *		to define a subtree the developer may use "st <subtree_name>:" or "sti <subtree_name>:"
 *		"ste" is used to mark end of subtree definitione
 *		to insert subtree in another tree definition use (+)n tree <subtree_name>
 *	\param path: path to scene definition file
 */
void interpret_dialectics(const char* path)
{
	// open scene definition file
	FILE* __File = fopen(path);
	if (!__File)
	{
		printf("error: scene definition file could not be found");
		return;
	}

	// iterate definitions
	char __CMD[DIALECTICS_COMMAND_CHARLEN];
	while (fscanf(__File,"%s",__CMD)!=EOF)
	{
		// TODO
	}

	// finish interpretation
	fclose(__File);
}
