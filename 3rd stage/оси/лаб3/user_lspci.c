#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define PROC_FILE "/proc/my_lspci"

int main(int argc, char *argv[]) {
	char buffer[1024];
	FILE *proc_file;

	if (argc > 1) {
    	// Передача параметра в procfs
    	proc_file = fopen(PROC_FILE, "w");
    	if (!proc_file) {
        	perror("Failed to open proc file for writing");
        	return 1;
    	}
    	fprintf(proc_file, "%s", argv[1]); // Запись фильтра
    	fclose(proc_file);
	}

	// Чтение из procfs
	proc_file = fopen(PROC_FILE, "r");
	if (!proc_file) {
    	perror("Failed to open proc file for reading");
    	return 1;
	}

	printf("Reading PCI devices:\n");
	while (fgets(buffer, sizeof(buffer), proc_file)) {
    	printf("%s", buffer);
	}

	fclose(proc_file);
	return 0;
}
