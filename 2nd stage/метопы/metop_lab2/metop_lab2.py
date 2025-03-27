import math


a = 0
b = 1
epsilon = 0.03


# исходная функция
def f(x):
    return 1 / 2 * x ** 2 - math.sin(x)


# половинное деление
def halving(a_1m, b_1m, epsilon_1m):
    a_copy = a_1m
    b_copy = b_1m

    while True:
        x1 = (a_copy + b_copy - epsilon_1m) / 2
        x2 = (a_copy + b_copy + epsilon_1m) / 2
        y1 = f(x1)
        y2 = f(x2)

        if y1 > y2:
            a_copy = x1
        else:
            b_copy = x2

        if b_copy - a_copy <= 2 * epsilon_1m:
            return (a_copy + b_copy) / 2


# золотое сечение
def golden_ratio(a_2m, b_2m, epsilon_2m):
    a_copy = a_2m
    b_copy = b_2m

    x1 = a_copy + 0.382 * (b_copy - a_copy)
    x2 = a_copy + 0.618 * (b_copy - a_copy)
    y1 = f(x1)
    y2 = f(x2)

    while True:
        if y1 < y2:
            b_copy = x2
            x2 = x1
            y2 = y1
            x1 = a_copy + 0.382 * (b_copy - a_copy)
            y1 = f(x1)
        else:
            a_copy = x1
            x1 = x2
            y1 = y2
            x2 = a_copy + 0.618 * (b_copy - a_copy)
            y2 = f(x2)

        if abs(b_copy - a_copy) < epsilon_2m:
            return (a_copy + b_copy) / 2


def d1(x):
    return x - math.cos(x)


def chord_method(a_3m, b_3m, epsilon_3m):
    a_copy = a_3m
    b_copy = b_3m

    while True:
        x_tilda = a_copy - (d1(a_copy) / (d1(a_copy) - d1(b_copy))) * (a_copy - b_copy)
        d1_x_tilda = d1(x_tilda)

        if abs(d1_x_tilda) <= epsilon_3m:
            return x_tilda

        if d1_x_tilda > 0:
            b_copy = x_tilda
        else:
            a_copy = x_tilda


def d2(x):
    return math.sin(x) + 1


def newton_method(a_4m, b_4m, epsilon_4m, x0_4m):
    if x0_4m >= a_4m and x0_4m <= b_4m:
        x = x0_4m
    else:
        return "Некорректный x0"

    while True:
        if abs(d1(x)) <= epsilon_4m:
            return x
        x -= d1(x) / d2(x)


def separate():
    print("-----------------------------------------")


separate()
print("Метод половинного деления\nМинимум функции:", halving(a, b, epsilon))
separate()
print("Метод золотого сечения\nМинимум функции:", golden_ratio(a, b, epsilon))
separate()
print("Метод хорд\nМинимум функции:", chord_method(a, b, epsilon))
separate()
print("Метод Ньютона\nМинимум функции:", newton_method(a, b, epsilon, float(input("Введите x0 из диапазона [0, 1]: "))))
separate()
