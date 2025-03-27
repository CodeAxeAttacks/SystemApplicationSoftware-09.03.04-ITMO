import math


def f(x):
    return x * x / 2 - math.sin(x)


def doublet_appr(x1, dx, epsilon):
    while True:
        x2 = x1 + dx
        y1 = f(x1)
        y2 = f(x2)
        if y1 > y2:
            x3 = x1 + 2 * dx
        else:
            x3 = x1 - dx
        y3 = f(x3)
        while True:
            y_min = min(y1, y2, y3)
            x_min = min(x1, x2, x3)
            if (x2 - x3) * y1 + (x3 - x1) * y2 + (x1 - x2) * y3 == 0:
                x1 = x_min
                break
            x_int_pol_min = 0.5 * ((x2 * x2 - x3 * x3) * y1 + (x3 * x3 - x1 * x1) * y2 + (x1 * x1 - x2 * x2) * y3) / (
                        (x2 - x3) * y1 + (x3 - x1) * y2 + (x1 - x2) * y3)
            y_int_pol_min = f(x_int_pol_min)
            if abs((y_min - y_int_pol_min) / y_int_pol_min) < epsilon and abs(
                    (x_min - x_int_pol_min) / x_int_pol_min) < epsilon:
                return x_int_pol_min
            elif x1 <= x_int_pol_min <= x3:
                if y_int_pol_min < y2:
                    if x_int_pol_min < x2:
                        x3 = x2
                        x2 = x_int_pol_min
                        y3 = y2
                        y2 = y_int_pol_min
                    else:
                        x1 = x2
                        x2 = x_int_pol_min
                        y1 = y2
                        y2 = y_int_pol_min
                else:
                    if x_int_pol_min < x2:
                        x1 = x_int_pol_min
                        y1 = y_int_pol_min
                    else:
                        x3 = x_int_pol_min
                        y3 = y_int_pol_min
            else:
                x1 = x_int_pol_min
                break


print(doublet_appr(3, 1, 0.0001))
