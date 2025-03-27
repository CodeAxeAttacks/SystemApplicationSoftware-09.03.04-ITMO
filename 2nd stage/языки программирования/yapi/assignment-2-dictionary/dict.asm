%include "lib.inc"

global find_word

find_word:
    push r12            ; r12 –> st_top
    test rsi, rsi       ; rsi == 0?
    jz .exit            ; if ZF == 0, goto exit
    
.loop:
    mov r12, rsi        ; rsi –> r12
    add rsi, 8  	; take next char
    call string_equals  ; goto string_equals
    test rax, rax       ; rax == 0?
    jz .take_next       ; if ZF == 0, goto next (strings are not equal)
    mov rax, r12        ; r12 –> rax (take adress of string)
    pop r12             ; restore r12
    ret
    
.take_next:
    mov rsi, r12        ; r12 –> rsi (restore rsi)
    mov rsi, [rsi]      ; [rsi] –> rsi (take next string)
    test rsi, rsi       ; rsi == 0?
    jnz .loop           ; if ZF != 0, goto loop (not last one)
    xor rax, rax        ; 0 –> rax
    pop r12             ; restore r12
    ret

.exit:
    xor rax, rax        ; 0 –> rax
    ret
