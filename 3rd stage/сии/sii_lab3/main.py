import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split

# Загрузка и предварительная обработка датасета (Пункт 2: Предварительная обработка данных)
def load_california_housing_data(dataset_path):
    # Чтение датасета из CSV файла
    dataset = pd.read_csv(dataset_path)
    values = dataset.values

    # Разделение данных по столбцам
    longitude = [row[0] for row in values]  # Долгота
    latitude = [row[1] for row in values]  # Широта
    housing_median_age = [row[2] for row in values]  # Средний возраст домов
    total_rooms = [row[3] for row in values]  # Общее количество комнат
    total_bedrooms = [row[4] for row in values]  # Общее количество спален
    population = [row[5] for row in values]  # Численность населения
    households = [row[6] for row in values]  # Количество домохозяйств
    median_income = [row[7] for row in values]  # Средний доход
    median_house_value = [row[8] for row in values]  # Медианная стоимость жилья (целевая переменная)

    # Возвращаем значения в виде отдельных массивов
    return (longitude, latitude, housing_median_age, total_rooms,
            total_bedrooms, population, households, median_income, median_house_value)

# Нормализация признаков (Пункт 2: Нормировка)
def normalize_feature(values):
    mean_val = np.mean(values)  # Вычисляем среднее значение
    std_dev_val = np.std(values)  # Вычисляем стандартное отклонение
    return [(value - mean_val) / std_dev_val for value in values], mean_val, std_dev_val

# Построение гистограммы (Пункт 1: Визуализация статистики)
def show_histogram(values, name):
    values = np.array(values)

    # Вычисляем ключевые статистические параметры
    min_val = np.min(values)
    max_val = np.max(values)
    mean_val = np.mean(values)
    deviation_val = np.std(values)
    q1_val = np.percentile(values, 25)  # Квантиль 0.25
    q3_val = np.percentile(values, 75)  # Квантиль 0.75

    # Метки для гистограммы
    labels = ['Минимум', 'Максимум', 'Мат. Ожидание', 'СКО', 'Квантиль 0.25', 'Квантиль 0.75']
    values = [min_val, max_val, mean_val, deviation_val, q1_val, q3_val]

    # Построение графика
    plt.figure(figsize=(10, 6))
    plt.bar(labels, values)
    plt.title(name)
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.show()

# Подготовка данных (Пункт 2: Кодирование и нормировка)
def prepare_data(longitude, latitude, housing_median_age, total_rooms,
                 total_bedrooms, population, households, median_income, median_house_value):
    # Нормализуем все признаки
    longitude, _, _ = normalize_feature(longitude)
    latitude, _, _ = normalize_feature(latitude)
    housing_median_age, _, _ = normalize_feature(housing_median_age)
    total_rooms, _, _ = normalize_feature(total_rooms)
    total_bedrooms, _, _ = normalize_feature(total_bedrooms)
    population, _, _ = normalize_feature(population)
    households, _, _ = normalize_feature(households)
    median_income, _, _ = normalize_feature(median_income)
    median_house_value, _, _ = normalize_feature(median_house_value)

    # Формируем матрицу признаков X и целевой вектор y
    x = [
        [longitude[i], latitude[i], housing_median_age[i], total_rooms[i], total_bedrooms[i],
         population[i], households[i], median_income[i]]
        for i in range(len(longitude))
    ]
    y = median_house_value

    return x, y

# Реализация линейной регрессии (Пункт 4)
def calculate_linear_regression(data, columns_indexes):
    X = []
    Y = []
    for row in data:
        # Формируем матрицу X с добавлением единичной колонки (для учета свободного члена)
        X.append([1] + [row[i] for i in columns_indexes])
        Y.append(row[-1])  # Целевая переменная

    X = np.array(X)
    Y = np.array(Y)

    # Вычисляем коэффициенты методом наименьших квадратов
    B = np.linalg.inv(X.T @ X) @ X.T @ Y
    return B

# Оценка модели (Пункт 6: Коэффициент детерминации)
def test(model, raw_test_data, columns):
    y_actual = [row[-1] for row in raw_test_data]  # Реальные значения
    y_actual_mean = np.mean(y_actual)  # Среднее значение целевой переменной

    # Общая сумма квадратов
    SStot = sum((y - y_actual_mean) ** 2 for y in y_actual)
    SSres = 0

    # Сумма квадратов остатков
    for row in raw_test_data:
        y_predict = model[0] + sum(model[j + 1] * row[columns[j]] for j in range(len(columns)))
        SSres += (row[-1] - y_predict) ** 2

    # Возвращаем коэффициент детерминации
    return 1 - SSres / SStot

if __name__ == "__main__":
    # Загрузка и предварительная обработка данных (Пункты 1 и 2)
    dataset_path = 'california_housing_train.csv'
    longitude, latitude, housing_median_age, total_rooms, total_bedrooms, population, households, median_income, median_house_value = load_california_housing_data(dataset_path)

    # Визуализация статистики (Пункт 1)
    show_histogram(longitude, 'Longitude')
    show_histogram(latitude, 'Latitude')
    show_histogram(housing_median_age, 'Housing Median Age')
    show_histogram(total_rooms, 'Total Rooms')
    show_histogram(total_bedrooms, 'Total Bedrooms')
    show_histogram(population, 'Population')
    show_histogram(households, 'Households')
    show_histogram(median_income, 'Median Income')
    show_histogram(median_house_value, 'Median House Value')

    # Подготовка данных
    x, y = prepare_data(longitude, latitude, housing_median_age, total_rooms, total_bedrooms,
                        population, households, median_income, median_house_value)

    # Разделение данных на обучающий и тестовый наборы (Пункт 3)
    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2)

    # Формируем обучающий и тестовый наборы
    raw_data_train = [x_train[i] + [y_train[i]] for i in range(len(x_train))]
    raw_data_test = [x_test[i] + [y_test[i]] for i in range(len(x_test))]

    # Модель 1: Используем все признаки (Пункт 5)
    model_1 = calculate_linear_regression(raw_data_train, list(range(8)))
    result_1 = test(model_1, raw_data_test, list(range(8)))

    # Модель 2: Используем longitude, latitude и median_income (Пункт 5)
    model_2 = calculate_linear_regression(raw_data_train, [0, 1, 7])
    result_2 = test(model_2, raw_data_test, [0, 1, 7])

    # Модель 3: Используем housing_median_age, total_rooms и population (Пункт 5)
    model_3 = calculate_linear_regression(raw_data_train, [2, 3, 5])
    result_3 = test(model_3, raw_data_test, [2, 3, 5])

    # Визуализация результатов (Пункт 6)
    labels = ['Модель 1', 'Модель 2', 'Модель 3']
    values = [result_1, result_2, result_3]

    plt.figure(figsize=(10, 6))
    plt.bar(labels, values)
    plt.title('Результаты')
    plt.xticks(rotation=45)
    plt.yticks(np.arange(0, 1.05, 0.05))
    plt.tight_layout()
    plt.show()
