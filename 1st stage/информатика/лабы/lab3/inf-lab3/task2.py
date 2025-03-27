# lab3, variant: 367868
# task2, var: 2
import re

print("Введите текст:")
test = input()
testr = test

for i in range(len(test)):
    for j in "!@#$%^&*()±§<>?/\|';:`~{}[].,=-–№":
        if test[i] == j:
            testr = testr.replace(test[i], "")

print(re.findall(r"\bВТ\b *(?:\b[^\n][\w]+\b){,4} *\bИТМО\b", testr))