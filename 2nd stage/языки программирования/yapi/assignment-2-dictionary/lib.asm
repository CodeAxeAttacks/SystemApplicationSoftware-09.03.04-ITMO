global exit
global string_length
global print_string
global print_char
global print_newline
global print_uint
global print_int
global string_equals
global read_char
global read_word
global parse_uint
global parse_int
global string_copy

section .text
 
 
; Принимает код возврата и завершает текущий процесс
exit: 
    mov rax, 60
    syscall


; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
    xor rax, rax                ; 0 to rax

.loop:
    cmp byte [rdi+rax], 0       ; is null_ter?
    je .end                     ; true –> ret, else –> next
    inc rax                     ; rax++
    jmp .loop                   ; repeat!

.end:
    ret


; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
    xor rax, rax                ; 0 –> rax
    push rdi                    ; get_str_beg –> st_top
    call string_length          ; str_len –> rax
    pop rsi                     ; st_top –> give_str_beg
    mov rdx, rax                ; rax –> o_data
    mov rdi, 1                  ; 1 –> get_str_beg (for sc)
    mov rax, 1                  ; 1 –> rax (write)
    syscall
    ret


; Принимает код символа и выводит его в stdout
print_char:
    xor rax, rax                ; 0 –> rax
    push rdi                    ; get_str_beg –> st_top
    mov rsi, rsp                ; st_top –> give_str_beg (which char to write)
    mov rdx, 1                  ; 1 –> o_data (1 byte)
    mov rdi, 1                  ; 1 –> get_str_beg (for sc)
    mov rax, 1                  ; 1 –> rax (write)
    syscall
    pop rdi
    ret


; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    mov rsi, 10                 ; 10 –> give_str_beg
    jmp print_char


; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
    xor rax, rax                ; 0 –> rax
    mov rax, rdi                ; get_srt_beg –> rax
    mov rdi, 10                 ; '\n' –> get_str_beg
    mov rsi, rsp                ; st_top –> give_str_beg
    push 0                      ; 0 –> st_top (null-terminator)
    
.parse:
    xor rdx, rdx                ; 0 –> rdx (prepare for div)
    div rdi                     ; rdx:rax / 10
    add dl, '0'                 ; make ASCII code by adding '48' + dl (dl in [0, 9])
    dec rsp                    ; st_top-- (because push has made st_top++)
    mov byte [rsp], dl          ; dl –> byte mem(st_top)
    test rax, rax               ; rax == 0?
    jnz .parse                  ; if ZF == 0, goto parse
    
    mov rdi, rsp                ; st_top –> get_str_beg
    push rsi                    ; give_str_beg –> st_top
    call print_string           ; print int
   
    pop rsi
	inc rsp    ; st_top –> give_str_beg
    mov rsp, rsi                ; give_str_beg –> st_top
    ret


; Выводит знаковое 8-байтовое число в десятичном формате 
print_int:
    xor rax, rax                ; 0 –> rax
    mov rax, rdi                ; give_str_beg –> rax
    test rax, rax               ; set flags on &
    jge .posi                   ; rax >= 0 goto posi 
    mov rdi, '-'                ; else print '–'
    push rax                    ; rax –> st_top
    call print_char             ; print '–'
    pop rax                     ; st_top –> rax 
    neg rax                     ; rax * (-1)

.posi:
    mov rdi, rax                ; give_str_beg –> rax

    call print_uint             ; print int without sign
    ret


; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
    xor rax, rax                ; 0 –> rax

.loop:
    mov al, byte [rdi]          ; get_str_beg –> r0b (rax)
    mov dl, byte [rsi]          ; give_str_beg –> r2b (rdx)
    cmp al, dl                  ; set flags on sub
    jnz .different              ; if ZF != 0 goto different
    test al, al                 ; set flags on & to check on null-terminator
    jz .same                    ; if ZF == 0 goto same
    inc rdi                     ; next byte
    inc rsi                     ; next byte
    jmp .loop                   ; repeat!

.different:
    xor rax, rax                ; false
    ret

.same:
    mov rax, 1                  ; true
    ret


; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
    xor rax, rax                ; 0 –> rax
    xor rdi, rdi                ; 0 –> get_str_beg
    sub rsp, 8                  ; st_top-- (abi)
    mov rsi, rsp                ; st_top –> give_str_beg
    mov rdx, 1                  ; 1 –> o_data (for sc)
    syscall
    test rax, rax               ; set flags on & to check on null-terminator
    jz .end                     ; if ZF == 0 goto end
    mov al, byte [rsp]          ; st_top –> r0b
    add rsp, 8                  ; st_top++
    ret

.end:
    add rsp, 8                  ; st_top++
    ret


; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор
read_word:
    push r12                    ; save because it is callee-saved register
    push r13                    ; save because it is callee-saved register
    push r14                    ; save because it is callee-saved register
    mov r12, rdi                ; get_str_beg –> r12 (for address)
    mov r13, rsi                ; give_str_beg –> r13 (for size)
    xor r14, r14                ; 0 –> r14 (for length)
    
.loop:
    call read_char              ; take one char
    test rax, rax               ; set flags on & to check last
    jz .done                    ; if ZF == 0 goto done
    cmp rax, 0x20               ; is ' '?
    jz .skip                    ; if ZF == 0 goto skip
    cmp rax, 0x9                ; is '\t'?
    jz .skip                    ; if ZF == 0 goto skip
    cmp rax, 0xA                ; is '\n'?
    jz .skip                    ; if ZF == 0 goto skip
    cmp r14, r13                ; set flags on sub to test lenght
    jge .too_long               ; if r14 >= r13 goto too_long
    mov [r12+r14], rax          ; save the char
    inc r14                     ; length++
    jmp .loop                   ; repeat with next char!

.done:
    mov byte [r12+r14], 0       ; add null-terminator
    jmp .exit
        
.skip:
    test r14, r14               ; set flags on & to check lenght
    jz .loop                    ; if ZF == 0 repeat with next char!
    jmp .done
    ret
        
.too_long:
    xor r12, r12                ; 0 –> address (return 0)

.exit:
    mov rax, r12                ; adress –> rax
    mov rdx, r14                ; length –> io_data
    pop r14                     ; restore because it is callee-saved register
    pop r13                     ; restore because it is callee-saved register
    pop r12                     ; restore because it is callee-saved register
    ret
 

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
    xor rax, rax                ; 0 –> rax
    xor rdx, rdx                ; 0 –> io_data (for length)
    xor r8, r8                  ; 0 –> r8 (for char)

.loop:
    mov r8b, byte [rdi+rdx]     ; take one char
    test r8b, r8b               ; set flags on & to check on null-terminator
    jz .exit                    ; if ZF == 0 goto exit 
    cmp r8b, '9'                ; set flags to check r8b > 9
    ja .not_in_range            ; if r8b > 9 goto not_in_range
    sub r8b, '0'                ; set flags to check r8b < 0 (r8b - '0' –> r8b)
    jb .not_in_range            ; if r8b < 0 goto not_in_range
    imul rax, rax, 10           ; mul to shift left
    add rax, r8                 ; rax + r8 –> rax
    inc rdx                     ; length++
    jmp .loop                   ; repeat!

.not_in_range:
    test rax, rax               ; check on 0
    jnz .exit                   ; if rax != 0 goto exit
    xor rdx, rdx                ; 0 –> length

.exit:
    ret


; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:
    xor rax, rax                ; 0 –> rax
    xor rdx, rdx                ; 0 –> io_data (for length)
    xor r8, r8                  ; 0 –> r8 (for char)
    mov r8b, byte [rdi+rdx]     ; take one char
    cmp r8b, '-'                ; set flags on sub to check signs
    jz .minus                   ; if ZF == 0 goto minus
    cmp r8b, '+'                ; set flags on sub to check signs
    jz .plus                    ; if ZF == 0 goto plus
    call parse_uint             ; take uint
    ret

.plus:
    inc rdi                     ; get_str_beg++
    call parse_uint             ; take uint
    test rdx, rdx               ; set flags on & to check on exit
    jz .exit                    ; if ZF == 0 goto exit
    inc rdx                     ; length++

.minus:
    inc rdi                     ; get_str_beg++
    call parse_uint             ; take uint
    test rdx, rdx               ; set flags on & to check on exit
    jz .exit                    ; if ZF == 0 goto exit
    inc rdx                     ; length++
    neg rax                     ; rax * (-1)

.exit:  
    ret 


; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
    xor rax, rax                ; 0 –> rax
    xor r8, r8                  ; 0 –> r8 (for char)

.loop:
    cmp rax, rdx                ; set flags on sub of rax and length
    jge .too_long               ; if rax >= length goto too_long
    mov r8b, byte [rdi+rax]     ; save char 
    mov byte [rsi+rax], r8b     ; next char
    test r8b, r8b               ; set flags on & to check on exit
    jz .exit                    ; if ZF == 0 goto exit
    inc rax                     ; rax++
    jmp .loop                   ; repeat!

.too_long:
    xor rax, rax                ; 0 –> rax

.exit:
    ret



