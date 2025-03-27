import pandas as pd
import math
import random
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
from sklearn.preprocessing import LabelEncoder


class Attribute:
    # Класс для описания атрибута с его индексом и уникальными значениями
    def __init__(self, index, unique_values):
        self.index = index
        self.unique_values = unique_values


class Node:
    # Класс для представления узла дерева решений
    def __init__(self, examples, attribute, c, is_list):
        self.examples = examples  # Примеры, принадлежащие узлу
        self.attribute = attribute  # Атрибут, используемый для разделения
        self.children = {}  # Дети узла
        self.is_list = is_list  # Является ли узел листом
        self.c = c  # Класс (если это лист)


class Tree:
    # Класс для построения дерева решений
    def __init__(self, examples, attributes):
        self.root = None  # Корневой узел
        self.attributes = attributes  # Список атрибутов
        self.examples = examples  # Примеры для обучения

    def train(self, parent_node):
        # (3) Реализация построения дерева решений
        if parent_node is None:
            parent_node = Node(self.examples, None, None, False)
            self.root = parent_node

        if parent_node.is_list:
            return

        examples = parent_node.examples
        info = self.calculate_info(examples)

        gains = []  # Список для хранения приростов информации

        for attribute in self.attributes:
            attribute_unique_values = attribute.unique_values
            attribute_unique_values_examples = []

            for attribute_unique_value in attribute_unique_values:
                # Разделяем примеры по значениям атрибута
                hit = [example for example in examples if example[attribute.index] == attribute_unique_value]
                attribute_unique_values_examples.append(hit)

            # Рассчитываем прирост информации и нормализуем по Split Info
            info_x = 0
            split_info_x = 0
            for attribute_unique_values_example in attribute_unique_values_examples:
                if len(attribute_unique_values_example) == 0:
                    continue
                info_x += len(attribute_unique_values_example) / len(examples) * self.calculate_info(
                    attribute_unique_values_example)
                split_info_x -= len(attribute_unique_values_example) / len(examples) * math.log2(
                    len(attribute_unique_values_example) / len(examples))

            if split_info_x == 0:
                continue

            gains.append([attribute, (info - info_x) / split_info_x])

        # Определяем, является ли узел листом
        success_examples_count = sum(1 for example in examples if example[-1] == 1)
        failure_examples_count = len(examples) - success_examples_count

        if len(gains) == 0:
            parent_node.is_list = True
            parent_node.c = 1 if success_examples_count > failure_examples_count else 0
            return

        # Выбираем лучший атрибут для разделения
        best_gain = max(gains, key=lambda x: x[1])
        best_attribute = best_gain[0]
        parent_node.attribute = best_attribute
        children = {}

        for attribute_unique_value in best_attribute.unique_values:
            # Создаем дочерние узлы
            hit = [example for example in examples if example[best_attribute.index] == attribute_unique_value]

            if len(hit) == 0:
                child = Node(None, None, 1 if success_examples_count > failure_examples_count else 0, True)
            elif len(hit) == 1:
                child = Node(None, None, hit[0][-1], True)
            else:
                child = Node(hit, None, 0, False)

            children[attribute_unique_value] = child

        parent_node.children = children

        for child in children.values():
            self.train(child)

    def test(self, example, node):
        # Рекурсивное тестирование примера
        while not node.is_list:
            return self.test(example, node.children[example[node.attribute.index]])
        return node

    def calculate_info(self, examples):
        # Расчет энтропии узла
        examples_count = len(examples)
        success_examples_count = sum(1 for example in examples if example[-1] == 1)
        failure_examples_count = examples_count - success_examples_count

        if success_examples_count == 0 or failure_examples_count == 0:
            return 0

        success_ratio = success_examples_count / examples_count
        failure_ratio = failure_examples_count / examples_count
        return -success_ratio * math.log2(success_ratio) - failure_ratio * math.log2(failure_ratio)


# (1) Загрузка датасета
columns = [
    "class", "cap-shape", "cap-surface", "cap-color", "bruises", "odor",
    "gill-attachment", "gill-spacing", "gill-size", "gill-color",
    "stalk-shape", "stalk-root", "stalk-surface-above-ring",
    "stalk-surface-below-ring", "stalk-color-above-ring",
    "stalk-color-below-ring", "veil-type", "veil-color", "ring-number",
    "ring-type", "spore-print-color", "population", "habitat"
]
file_path = 'agaricus-lepiota.data'
mushroom_data = pd.read_csv(file_path, names=columns)

# Преобразуем категориальные данные в числовые
encoded_data = mushroom_data.apply(LabelEncoder().fit_transform)

# (2) Подготовка данных
features = encoded_data.drop(columns=["class"]).values
target = encoded_data["class"].values
examples_train, examples_test, target_train, target_test = train_test_split(features, target, test_size=0.2, random_state=42)
examples_train = [list(examples_train[i]) + [target_train[i]] for i in range(len(target_train))]
examples_test = [list(examples_test[i]) + [target_test[i]] for i in range(len(target_test))]

# Выбираем случайный набор признаков
attributes_count = features.shape[1]
attributes_to_take = int(math.sqrt(attributes_count))
attributes_indexes = sorted(random.sample(range(attributes_count), attributes_to_take))
attributes = [Attribute(index, set(example[index] for example in examples_train)) for index in attributes_indexes]

# (3) Обучение дерева решений
tree = Tree(examples_train, attributes)
tree.train(None)

# (4) Оценка точности, полноты и точности
y_true = []
y_pred = []
for test_example in examples_test:
    pred_node = tree.test(test_example, tree.root)
    y_true.append(test_example[-1])
    y_pred.append(pred_node.c)

TP, TN, FP, FN = confusion_matrix(y_true, y_pred).ravel()
accuracy = (TP + TN) / len(examples_test)
precision = TP / (TP + FP) if (TP + FP) > 0 else 0
recall = TP / (TP + FN) if (TP + FN) > 0 else 0

print(f"Accuracy: {accuracy}")
print(f"Precision: {precision}")
print(f"Recall: {recall}")

# (5) Построение кривых AUC-ROC
tested_data = sorted([[y_pred[i], y_true[i]] for i in range(len(y_true))], key=lambda x: (x[1], x[0]), reverse=True)
success = len([1 for _, true in tested_data if true == 1])
failure = len(tested_data) - success
step_x = 1 / failure
step_y = 1 / success

x, y = [0], [0]
for _, true in tested_data:
    if true == 1:
        x.append(x[-1])
        y.append(y[-1] + step_y)
    else:
        x.append(x[-1] + step_x)
        y.append(y[-1])

plt.plot(x, y)
plt.title('AUC-ROC')
plt.show()

# (5) Построение кривой AUC-PR
x, y = [0], [1]
for _, true in tested_data:
    if true == 1:
        x.append(x[-1])
        y.append(y[-1] - step_y)
    else:
        x.append(x[-1] + step_x)
        y.append(y[-1])

plt.plot(x, y)
plt.title('AUC-PR')
plt.show()
