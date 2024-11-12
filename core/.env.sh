linker="-lGL -lGLEW -lSDL2"
extras="-O3 -fno-gcse"

alias b="rm -rf bin && mkdir bin && cd bin && gcc -c ../*.c $linker $extras && cd .. && gcc ./bin/*.o $linker $extras -o preview"
alias r="./preview"
alias d="valgrind --leak-check=full ./preview"
alias fe="b&&r"
alias fd="b&&d"
