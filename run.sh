#!/usr/bin/env sh

export CLASSPATH="bin:lib/com.mokiat.data.front-2.0.1.jar"
export invoke="javac -d bin `fdfind .java src`"
echo $invoke
eval $invoke
