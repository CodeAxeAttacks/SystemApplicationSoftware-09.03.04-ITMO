ASM=nasm
ASFLAGS=-f elf64
LD=ld

SRC=main.asm lib.asm dict.asm
OBJ=$(SRC:.asm=.o)
TARGET=program

.PHONY: clean all 
all: $(TARGET)

$(TARGET): $(OBJ)
	$(LD) -o $@ $^

%.o: %.asm
	$(ASM) $(ASFLAGS) $< -o $@
	
main.o: main.asm dict.inc colon.inc lib.inc words.inc

dict.o: dict.asm lib.inc

clean:
	rm -f $(OBJ) $(TARGET)
	
test: $(TARGET)
	python3 test.py
	
rebuild: clean all
