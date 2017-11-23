.MODEL SMALL
.CODE
Empieza:
mov Ax, @Data
mov Ds, Ax
mov Dx, Offset var0
mov Ah, 9
Int 21h
mov AX, 4C00h
int 21h
.DATA
var0 DB 'yazenia huele a popo',10,13,'$'
.STACK
END Empieza
