; Задание №1. Разработать программу обработки для элементов массива M, в которой:
; 1. Массив имеет следующие характеристики:
; - адрес начала массива в памяти БЭВМ - 0x687;
; - число измерений исходного массива - 2;
; - количество элементов исходного массива - 3x3;
; - каждый элемент является знаковым числом с разрядностью 16 бит;
; - нумерация элементов начинается с 1;
; - элементы хранятся в массиве по границам слов, нет необходимости в плотной упаковке;
; 2. Для каждого элемента массива необходимо вычислить функцию:
; - формула функции F(X) = 3 * X + 23479;
; - функцию вычислять только для элементов массива с кратными 3-м i-индексами, четными j-индексами;
; - если результат вычисления функции выходит за пределы области допустимых значений элемента массива из п.1, то он принимается равным 18457
; 3. Из всех полученных значений функции необходимо вычислить исключающее 'ИЛИ' значений, и записать в 32-разрядный результат.
; Примечание: все числа представлены в десятичной системе счисления, если явно не указано иное.
;A XOR B = (A AND (NOT B)) OR ((NOT A) AND B)

	ORG 0x10
VECTOR_ADDR: 	WORD	0x687 ; задаём начало массива и его размерность
	DIM_M: 	WORD 	0x3
	DIM_N:	WORD	0x3
POINTER: 	WORD 	?		  ; задаём указатель массива
	INT_I:	WORD 	0x1
	INT_J: 	WORD 	0x1
CONST1:	WORD 	23479
CONST2: WORD	18457
ARG: 	WORD 	?		  ; переменная для хранения промежуточных значений
RESULT: WORD	?		  ; переменная для хранения результат
START:	LD	VECTOR_ADDR
	ST 	POINTER
NEXT: 	LD 	(POINTER)+
	ST	ARG
	CALL	CHECK_INDEX
	LD	INT_J
	CMP	DIM_N
	BZC	INC_I
	INC
	ST 	INT_J
	JUMP	NEXT
INC_I:	LD 	#0x1
	ST 	INT_J
	LD 	INT_I
	CMP DIM_M
	BZS	STOP_P
	INC
	ST INT_I
	JUMP NEXT
STOP_P: 	HLT
CHECK_INDEX:	LD	INT_I	  ; проверка релевантности индексов
	DIV_I: 	SUB 	#0x3	  ; проверка i на кратность 3
		BZS 	CHECK_J
		BNC		DIV_I
		RET
	CHECK_J:	LD 	INT_J	  ; проверка j на кратность 2
		DIV_J:	SUB #0x2
			BZS	CALCULATE
			BNC	DIV_J
			RET
	CALCULATE:	CALL 	FUNC
				CALL 	XOR
				RET
XOR_PART:	WORD	?
XOR: 	PUSH
	LD	RESULT
	NOT
	AND	&0
	ST 	XOR_PART
	POP
	NOT
	AND	RESULT
	OR 	XOR_PART
	ST 	RESULT
	RET
FUNC: 	LD 	ARG
	ADD 	ARG
	BVS		ERR
	ADD 	ARG
	BVS		ERR
	ADD 	CONST1
	BVS 	ERR
	RET
	ERR:	LD 	CONST2
		RET
