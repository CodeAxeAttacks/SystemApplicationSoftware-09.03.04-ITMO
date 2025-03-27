import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

# 1. Загрузка и предварительная обработка данных
file_path = "diabetes.csv"
data = pd.read_csv(file_path)

# Замена нулевых значений на NaN для указанных колонок
columns_with_zeros = ['Glucose', 'BloodPressure', 'SkinThickness', 'Insulin', 'BMI']
data[columns_with_zeros] = data[columns_with_zeros].replace(0, np.nan)

# Заполнение NaN медианными значениями
data[columns_with_zeros] = data[columns_with_zeros].fillna(data[columns_with_zeros].median())

# Проверка, что NaN отсутствуют
if data.isna().sum().sum() > 0:
    raise ValueError("В данных остались пропущенные значения!")

# Нормализация входных данных
X = data.drop('Outcome', axis=1)
X = (X - X.mean()) / X.std()
y = data['Outcome']

# 2. Получение и визуализация статистики
print(data.describe())

# Визуализация гистограмм
data.hist(figsize=(12, 10))
plt.tight_layout()
plt.show()

# 3. Разделение данных на обучающий и тестовый наборы
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)


# 4. Реализация логистической регрессии
class LogisticRegressionScratch:
    def __init__(self, learning_rate=0.01, iterations=1000):
        self.learning_rate = learning_rate
        self.iterations = iterations
        self.weights = None
        self.bias = None

    def sigmoid(self, z):
        # Численно стабильная версия сигмоидной функции
        return np.where(z >= 0,
                        1 / (1 + np.exp(-z)),
                        np.exp(z) / (1 + np.exp(z)))

    def log_loss(self, y_true, y_pred):
        epsilon = 1e-15
        y_pred = np.clip(y_pred, epsilon, 1 - epsilon)
        return -np.mean(y_true * np.log(y_pred) + (1 - y_true) * np.log(1 - y_pred))

    def fit(self, X, y):
        n_samples, n_features = X.shape
        self.weights = np.zeros(n_features)
        self.bias = 0

        for _ in range(self.iterations):
            model = np.dot(X, self.weights) + self.bias
            predictions = self.sigmoid(model)

            # Градиентный спуск
            dw = np.dot(X.T, (predictions - y)) / n_samples
            db = np.sum(predictions - y) / n_samples

            # Обновление параметров
            self.weights -= self.learning_rate * dw
            self.bias -= self.learning_rate * db

    def predict(self, X):
        model = np.dot(X, self.weights) + self.bias
        predictions = self.sigmoid(model)
        return [1 if i > 0.5 else 0 for i in predictions]


# 5. Обучение модели
model = LogisticRegressionScratch(learning_rate=0.01, iterations=1000)
model.fit(X_train.to_numpy(), y_train.to_numpy())
y_pred = model.predict(X_test.to_numpy())

# 6. Исследование гиперпараметров
learning_rates = [0.001, 0.01, 0.1]
iterations_list = [500, 1000, 2000]
results = []

for lr in learning_rates:
    for iters in iterations_list:
        model = LogisticRegressionScratch(learning_rate=lr, iterations=iters)
        model.fit(X_train.to_numpy(), y_train.to_numpy())
        y_pred = model.predict(X_test.to_numpy())

        acc = accuracy_score(y_test, y_pred)
        prec = precision_score(y_test, y_pred)
        rec = recall_score(y_test, y_pred)
        f1 = f1_score(y_test, y_pred)

        results.append((lr, iters, acc, prec, rec, f1))

# 7. Оценка модели
for result in results:
    print(
        f"LR: {result[0]}, Iterations: {result[1]} -> Accuracy: {result[2]:.4f}, Precision: {result[3]:.4f}, Recall: {result[4]:.4f}, F1-Score: {result[5]:.4f}")
