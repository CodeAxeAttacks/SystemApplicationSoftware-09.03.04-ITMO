# lab3, variant: 367868
# task3, var: 3 (гр. Р3107)
import re

print("Дорогой пользователь, введите, пожалуйста, список своих любимых (и не любимых) одногрупников:")
test = input()
mas = []
mid = []

while test != "STOP":
    mas.append(test)
    test = input()

for i in range(0, len(mas) - 1):
    vrm = mas[i]
    regex = re.compile(r"(\b([А-Я])\w*(?:[- ]\2\w*)?\s+(?:\2.){2} Р3107)")
    if regex.findall(vrm) == []:
        mid.append(mas[i])

print(mid)
print("Теперь эти ребята будут голодать!")