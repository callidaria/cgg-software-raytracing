#!/bin/bash

export CLASSPATH="bin:lib/com.mokiat.data.front-2.0.1.jar"
export invoke="javac -d bin `fdfind .java src` && java -cp bin cgg.a03.Main"
echo $invoke
eval $invoke
