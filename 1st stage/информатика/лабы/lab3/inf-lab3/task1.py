# lab3, variant: 367868
# task1, var: X-\
import re

print("Введите текст:")
test = input()
print("Введите смайлик:")
smile = re.escape(input())

print("Количество совпадений в тексте:", len(re.findall(smile, test)))