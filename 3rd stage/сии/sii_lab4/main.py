import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import random
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix

# Загрузка данных о вине
file_path = 'WineDataset.csv'
wine_data = pd.read_csv(file_path)

# Вывод первых нескольких строк датасета для понимания структуры
print(wine_data.head())


# Функция для визуализации статистики признака
def show_feature(feature_data, feature_name):
    """
    Визуализирует основные статистические показатели признака.
    """
    feature_data = np.array(feature_data)

    mean = np.mean(feature_data)
    std_dev = np.std(feature_data, ddof=1)
    min_val = np.min(feature_data)
    max_val = np.max(feature_data)
    q1 = np.percentile(feature_data, 25)
    q3 = np.percentile(feature_data, 75)

    labels = ['Среднее', 'Стд. отклонение', 'Минимум', 'Максимум', '25-й квантиль', '75-й квантиль']
    values = [mean, std_dev, min_val, max_val, q1, q3]

    plt.figure(figsize=(10, 6))
    plt.bar(labels, values)
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.title(f'{feature_name} (Кол-во: {len(feature_data)})')
    plt.show()


# Функция для нормализации признаков
def normalize(feature_data):
    """
    Нормализует данные: (значение - среднее) / стандартное отклонение.
    """
    feature_data = np.array(feature_data)
    mean = np.mean(feature_data)
    std_dev = np.std(feature_data)
    return list(map(lambda value: (value - mean) / std_dev, feature_data))


# Разделение данных на признаки и целевую переменную
features = wine_data.drop(columns=['Wine'])  # Признаки
target = wine_data['Wine']  # Целевая переменная

# Визуализация статистики для каждого признака
for column in features.columns:
    show_feature(features[column], column)

# Нормализация всех признаков
normalized_features = features.apply(normalize)

# Подготовка данных для метода k-NN
x = normalized_features.values  # Матрица признаков
y = target.values  # Массив целевой переменной

# Разделение данных на обучающую и тестовую выборки
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=42)


# Определение функций для алгоритма k-NN
def calculate_distance(x_train_row, x_test_row, column_indexes):
    """
    Вычисляет расстояние между строками обучающей и тестовой выборки.
    """
    distance = 0
    for column_index in column_indexes:
        distance += (x_train_row[column_index] - x_test_row[column_index]) ** 2
    return distance ** 0.5


def predict(x_train, x_test_row, y_train, k, column_indexes):
    """
    Прогнозирует метку класса на основе k ближайших соседей.
    """
    results = []
    for i in range(len(x_train)):
        results.append([
            calculate_distance(x_train[i], x_test_row, column_indexes),
            y_train[i],
        ])
    results.sort(key=lambda result: result[0])  # Сортировка по расстоянию
    closest = results[:k]
    closest_labels = [result[1] for result in closest]
    return max(set(closest_labels), key=closest_labels.count)  # Наиболее частый класс


def test_model(x_train, x_test, y_train, y_test, k, column_indexes):
    """
    Тестирует модель k-NN на тестовых данных и возвращает матрицу ошибок.
    """
    predictions = [predict(x_train, x_test_row, y_train, k, column_indexes) for x_test_row in x_test]
    return confusion_matrix(y_test, predictions)


def compare_models(x_train, x_test, y_train, y_test, k, column_indexes):
    """
    Сравнивает две модели: с фиксированными признаками и случайно выбранными признаками.
    """
    # Случайные признаки
    random_indexes = random.sample(range(x_train.shape[1]), random.randint(1, x_train.shape[1]))
    random_result = test_model(x_train, x_test, y_train, y_test, k, random_indexes)

    # Фиксированные признаки
    fixed_result = test_model(x_train, x_test, y_train, y_test, k, column_indexes)

    # Визуализация результатов
    values = [
        fixed_result[0][0], random_result[0][0],  # True Positives
        fixed_result[1][1], random_result[1][1]   # True Negatives
    ]
    labels = [
        'TP Fixed', 'TP Random',
        'TN Fixed', 'TN Random'
    ]

    plt.figure(figsize=(10, 6))
    plt.bar(labels, values, color=['blue', 'orange'])
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.title(f'Сравнение фиксированных и случайных признаков (k={k})')
    plt.show()


# Запуск сравнений для разных значений k
fixed_columns = [0, 1, 2]  # Пример: фиксированные первые три признака
for k in [3, 5, 10]:
    compare_models(x_train, x_test, y_train, y_test, k, fixed_columns)
