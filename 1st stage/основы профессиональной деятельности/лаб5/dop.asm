ORG	0x16

C1:	WORD	0x3233	;Записываем координаты судна
C2:	WORD	0x3333
C3:	WORD	0x3433
C4:	WORD	0x3533

COUNT11:	WORD	0xB
START:	LD	#0xFF	;Рисуем судно вместе с полем
	OUT	0x10
	LD	#0x0	
	OUT	0x10
	OUT	0x10
	LD	#0x10
	OUT	0x10
	OUT	0x10
	OUT	0x10
	OUT	0x10
	LD	#0x0
	OUT	0x10
	OUT	0x10
	LD	#0xFF
	OUT	0x10

	LD	#0x0		;Смещаем поле на середину ВУ-6 для красоты
MOVE:	OUT	0x10
	LOOP	COUNT11
	JUMP	MOVE

C:	WORD	?

S0:	CLA
S1:	IN	0x19	;Читаем x
	AND	#0x40
	BEQ	S1
	IN	0x18
	
	CMP	#0x30	;Первая проверка - введенный код не может быть меньше 0x30
	BLO	S1	;При неверном вводе - повторный ввод координаты
	CMP	#0x38	;Вторая проверка - введенный код не может быть больше или равен 0x38
	BHIS	S1	;При неверном вводе - повторный ввод координаты
	
	SWAB
	ST	C	;Добавляем координату x

	CLA		;Ожидаем ввода запятой
S2:	IN	0x19	;Читаем символ
	AND	#0x40
	BEQ	S2
	IN	0x18
	CMP	#0x2C
	BNE	S2	;При неравенстве введенного кода с кодом запятой (0x2C) ожидаем дальше ввод запятой

	CLA
S3:	IN	0x19	;Читаем y
	AND	#0x40
	BEQ	S3
	IN	0x18
	
	CMP	#0x30	;Первая проверка - введенный код не может быть меньше 0x30
	BLO	S3	;При неверном вводе - повторный ввод координаты
	CMP	#0x38	;Вторая проверка - введенный код не может быть больше или равен 0x38
	BHIS	S3	;При неверном вводе - повторный ввод координаты
	
	ADD	C
	ST	C	;Добавляем координату y

	CMP	C1	;Проверка на попа дание
	BEQ	HIT
	CMP	C2
	BEQ	HIT
	CMP	C3
	BEQ	HIT
	CMP	C4
	BEQ	HIT
	JUMP	MISS

HIT:	LD	#0xD0
	OUT	0xC
	LD	#0xCF
	OUT	0xC
	LD	#0xD0
	OUT	0xC
	LD	#0xC1
	OUT	0xC
	LD	#0xCC
	OUT	0xC
	LD	#0x00
	OUT	0xC
	JUMP	S0

MISS:	LD	#0xCD
	OUT	0xC
	LD	#0xC9
	OUT	0xC
	LD	#0xCD
	OUT	0xC
	LD	#0xCF
	OUT	0xC
	LD	#0x00
	OUT	0xC
	JUMP	S0
