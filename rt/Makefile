CC = gcc

LDST = lib/
SDST = .
TARGET = rt
LINKER = -lm

SRCS = $(filter-out main.c, $(wildcard *.c))
OBJS = $(SRCS:%.c=$(LDST)%.o)
MAIN = main.c

DEBUG_SUFFIX = -pg -g -O0 -DDEBUG
RELEASE_SUFFIX = -O3 -fno-gcse

CXXFLAGS = $(RELEASE_SUFFIX) $(LINKER)

all: $(TARGET)

debug: CXXFLAGS = $(DEBUG_SUFFIX) $(LINKER)
debug: $(TARGET)

clean:
	rm -rf $(LDST)*.o $(TARGET)

$(TARGET): $(LDST)main.o $(OBJS)
	@mkdir -p $(dir $@)
	$(CC) $(LDST)main.o $(OBJS) -o $@ $(CXXFLAGS)

$(LDST)%.o: %.c %.h
	@mkdir -p $(LDST)
	$(CC) $< -o $@ -c $(CXXFLAGS)

$(LDST)main.o: main.c
	@mkdir -p $(LDST)
	$(CC) $< -o $@ -c $(CXXFLAGS)
