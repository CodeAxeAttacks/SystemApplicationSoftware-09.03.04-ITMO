%include "dict.inc"
%include "lib.inc"
%include "words.inc"

%define BUFFER_SIZE 256

section .bss
	buffer: resb BUFFER_SIZE

section .rodata
	too_long_one: db "It is too long string!", 10, 0
	not_found_one: db "The string is not found!", 10, 0
        
section .text
	global _start

_start:
        mov     rdi, buffer        	; pointer to buffer
        mov     rsi, BUFFER_SIZE           	; max length of word –> rsi
        call    read_word          	; goto read_word
        test    rax, rax           	; rax == 0?
        jz      .too_long_word     	; if ZF == 0, goto too_long_word
        mov     rsi, first_word    	; first word in dict –> rsi
        mov     rdi, buffer        	; word -> rdi
        call    find_word         	; goto find_word
        test    rax, rax           	; rax == 0?
        jz      .not_found_word    	; if ZF == 0, goto not_found_word
        mov     rdi, rax           	; rax –> rdi
        add     rdi, 8                  ; take next char
        push    rdi                     ; rdi –> st_top
        call    string_length           ; goto string_length
        pop     rdi                     ; restore rdi
        add     rdi, rax                ; rax –> rdi (addrest of end)
        inc     rdi                     ; rdi++ (skip null-term)
        call    print_string            ; goto print_string
        call    print_newline           ; goto print_newline
        xor     rdi, rdi                ; 0 –> rdi
        call    exit                    ; goto exit
        
.too_long_word:
        mov     rdi, too_long_one    	; "The word is too long!" -> rdi
        jmp     .show_the_error     	; goto show_the_error
        
.not_found_word:
        mov     rdi, not_found_one   	; "The word is not found!" -> rdi
        
.show_the_error:
        push    rdi                     ; rdi -> st_top
        call    string_length           ; goto string_length
        pop     rsi                     ; restore rsi
        mov     rdx, rax                ; rax -> rdx (length)
        mov     rax, 1             	; prepare for syscall
        mov     rdi, 2             	; prepare for syscall
        syscall                         ; print err
        call    exit                    ; goto exit
