ORG 0x01D8
; Методика направлена на проверку работы микрокоманды со знаковыми числами
; Тест А - проверка на правильный результат (положительные числа + без переполнений)

START:   JUMP   $TEST1

        
A1:     WORD    0x3     ; A1 = 3
A2:     WORD    0x4     ; A2 = 4
A3:     WORD    0x1     ; A3 = 1

; Подготовка: запись чисел A1, A2 в стек
TEST1:  LD      $A1
        PUSH
        LD      $A2
        PUSH
; Выполнение команды 0x0F01
        WORD    0x0F01
; Проверка результата - должен получиться результат, равный A3
        LD      &0x0
        BVS     FALSE1
        CMP     $A3
        BEQ     TRUE1
FALSE1: LD      #0x0
        ST      $X
        JUMP    $TEST2
TRUE1:  LD      #0x1
        ST      $X
        JUMP    $TEST2

; Тест B - проверка на правильный результат (отрицательные числа + без переполнений)
        
B1:     WORD    0xFFFF  ; B1 = -1
B2:     WORD    0xFFFF  ; B2 = -1
B3:     WORD    0x0000  ; B3 = 0

; Подготовка: очистка стека, запись чисел B1, B2 в стек
TEST2:  POP
        POP
        POP     
        LD      $B1
        PUSH
        LD      $B2
        PUSH
; Выполнение команды 0x0F01
        WORD    0x0F01
; Проверка результата - должен получиться результат, равный B3
        LD      &0x0
        BVS     FALSE2  ; Переполнение - ошибка
        CMP     $B3
        BEQ     TRUE2
FALSE2: LD      #0x0
        ST      $Y
        JUMP    $TEST3
TRUE2:  LD      #0x1
        ST      $Y
        JUMP    $TEST3


; Тест C - проверка на правильный результат (переполнение)
        
С1:     WORD    0xFFFF  ; C1 = -1
С2:     WORD    0x7FFF  ; C2 = 32767

; Подготовка: очистка стека, запись чисел C1, C2 в стек
TEST3:  POP
        POP
        POP     
        LD      $С1
        PUSH
        LD      $С2
        PUSH
; Выполнение команды 0x0F01
        WORD    0x0F01
; Проверка результата - должно произойти переполнение
        BVS     TRUE3
FALSE3: LD      #0x0
        ST      $Z
        JUMP    $CHECK
TRUE3:  LD      #0x1
        ST      $Z
        JUMP    $CHECK

        ORG 0x90
X:      WORD    0x0
Y:      WORD    0x0
Z:      WORD    0x0
ANS:    WORD    0x0
CHECK:     LD      X
        AND     Y
        AND     Z
        ST      ANS
        HLT