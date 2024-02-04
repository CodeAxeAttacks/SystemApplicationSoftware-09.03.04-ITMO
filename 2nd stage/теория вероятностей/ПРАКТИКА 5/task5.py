import math
import matplotlib.pyplot as plt
import numpy as np
from scipy.interpolate import CubicSpline

# Исходная выборка
sample = [-0.03, 0.73, -0.59, -1.59, 0.38, 1.49, 0.14, -0.62, -1.59, 1.45, -0.38, -1.49, -0.15, 0.63, 0.06, -1.59, 0.61, 0.62, -0.05, 1.56]

# Вариационный ряд
variation_range = list(set(sample))
variation_range = sorted(variation_range)

# Экстремальные значения и размах
min_value = variation_range[0]
max_value = variation_range[-1]
range_value = round(max_value - min_value, 2)

# Частота
frequency = []
for i in variation_range:
    frequency.append(sample.count(i))

# Статистический ряд
sample_len = len(sample)
statistical_series = []
for i in range(len(variation_range)):
    el = []
    el.append(variation_range[i])
    el.append(frequency[i])
    el.append(frequency[i] / sample_len)
    statistical_series.append(el)

# Интервальный статистический ряд
partial_interval_length = round(range_value / (1 + math.log(sample_len, 2)), 2) # Длина частичного интервала (по формуле Стерджеса)
interval_count = math.ceil(1 + math.log(sample_len, 2))
x_start = min_value - partial_interval_length / 2
variation_range_len = len(variation_range)

intervals = []
for i in range(interval_count):
    el = []
    el.append(x_start)
    x_start = round(x_start + partial_interval_length, 2)
    el.append(x_start)
    intervals.append(el)

frequency_for_intervals = []
for i in range(interval_count):
    frequency_for_intervals.append(0)
for i in variation_range:
    if i >= intervals[0][0] and i < intervals[0][1]:
        frequency_for_intervals[0] += 1
    elif i >= intervals[1][0] and i < intervals[1][1]:
        frequency_for_intervals[1] += 1
    elif i >= intervals[2][0] and i < intervals[2][1]:
        frequency_for_intervals[2] += 1
    elif i >= intervals[3][0] and i < intervals[3][1]:
        frequency_for_intervals[3] += 1
    elif i >= intervals[4][0] and i < intervals[4][1]:
        frequency_for_intervals[4] += 1
    elif i >= intervals[5][0] and i < intervals[5][1]:
        frequency_for_intervals[5] += 1

interval_statistical_series = []
for i in range(interval_count):
    el = []
    el.append(intervals[i])
    el.append(frequency_for_intervals[i])
    el.append(round(frequency_for_intervals[i] / variation_range_len, 2))
    interval_statistical_series.append(el)

# Среднее значение
average_value = round(sum(sample) / sample_len, 2)

# Выборочная дисперсия
sum_for_dis = 0
for i in statistical_series:
    sum_for_dis += (i[0] - average_value) ** 2 * i[1]
dispersion = round(1 / sample_len * sum_for_dis, 2)

# Среднеквадратическое отклонение
standard_deviation = round(dispersion ** 0.5, 2)

# Исправленная выборочная дисперсия
сorrected_sample_variance = round(sample_len / (sample_len - 1) * dispersion, 2)

# Исправленное выборочное среднеквадратическое отклонение
corrected_standard_deviation = round(сorrected_sample_variance ** 0.5)

# Мода M_o
max_frequency = max(frequency)
for i in statistical_series:
    if i[1] == max_frequency:
        mode = i[0]

# Медиана M_e
mid_index = int(len(variation_range) / 2)
if len(variation_range) % 2 == 0:
    median = (variation_range[mid_index] + variation_range[mid_index - 1]) / 2
else:
    median = variation_range[mid_index]

# Эмпирическая функция распределения
def print_empirical_distribution_function(statistical_series):
    x = statistical_series[0][0]
    frequency_counter = 0
    y_values = []
    print(f'Эмпирическая функция распределения:\n{frequency_counter}, при x <= {x}')
    for i in statistical_series:
        y_values.append(frequency_counter)
        x_old = x
        x = round(x + i[0], 2)
        frequency_counter = round(frequency_counter + i[2], 2)
        print(f'{frequency_counter}, при {x_old} < x <= {x}')

    # Строим график
    plt.step(variation_range, y_values, where='post')
    plt.xlabel('x')
    plt.ylabel('F(x)')
    plt.title('Эмпирическая функция распределения')
    plt.grid(True)
    plt.show()

# Графическое изображение статистического распределения
def print_statistical_distribution(statistical_series):
    y_values_stat = []
    for i in statistical_series:
        y_values_stat.append(i[2])

    # Строим график
    plt.plot(variation_range, y_values_stat)
    plt.xlabel('x')
    plt.ylabel('f(x)')
    plt.title('Графическое изображение статистического распределения')
    plt.grid(True)
    plt.show()

# Гистограмма частот
def print_frequency_histogram():
    # Extract heights and midpoints
    heights = [round(i[2] / interval_count, 2) for i in interval_statistical_series]
    midpoints = [(interval[0] + interval[1]) / 2 for interval in intervals]
    bar_widths = [(interval[1] - interval[0]) for interval in intervals]

    for i, (midpoint, height) in enumerate(zip(midpoints, heights)):
        width = bar_widths[i]
        plt.bar(midpoint, height, width=width, align='center', alpha=0.7, label='Гистограмма' if i == 0 else '')

    cs = CubicSpline(midpoints, heights)

    smooth_midpoints = np.linspace(min(midpoints), max(midpoints), 100)
    smooth_values = cs(smooth_midpoints)

    plt.plot(smooth_midpoints, smooth_values, label='Функция (Интерполяция)')

    plt.xlabel('Интервалы')
    plt.ylabel('Высота столбов')
    plt.title('Гистограмма с функцией')
    plt.legend()
    plt.show()

# Вывод результатов
print(f'Вариационный ряд: {variation_range}')
print(f'Экстремальные значения: Минимум = {min_value}, Максимум = {max_value}')
print(f'Статистический ряд (x_i, n_i, p_i): {statistical_series}')
print(f'Интервальный статистический ряд (интервал, n_i, p_i): {interval_statistical_series}')
print(f'Среднее значение: {average_value}')
print(f'Выборочная дисперсия: {dispersion}')
print(f'Среднеквадратическое отклонение: {standard_deviation}')
print(f'Исправленная выборочная дисперсия: {сorrected_sample_variance}')
print(f'Исправленное выборочное среднеквадратическое отклонение: {corrected_standard_deviation}')
print(f'Размах: {range_value}')
print(f'Мода M_o: {mode}')
print(f'Медиана M_e: {median}')
print_empirical_distribution_function(statistical_series)
print_statistical_distribution(statistical_series)
print_frequency_histogram()
