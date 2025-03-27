import re
from swiplserver import PrologMQI, PrologThread, create_posix_path


def get_result(res, no, yes):
    """Форматирует и возвращает результат запроса."""
    resp = ""
    if not res or type(res) != bool and len(res) == 0:
        resp = no
    else:
        if type(res) == bool:
            resp = yes
        else:
            ans = {}
            for el in res:
                for key in el:
                    if key not in ans:
                        ans[key] = [el[key]]
                    else:
                        ans[key].append(el[key])
            res = []
            for key in ans:
                res.append(f"{key}:")
                res.append("\n".join(ans[key]))
            resp = "\n".join(res)
    resp += "\n" + "-" * 30
    return resp


class HeroRecommendation:
    """Класс для получения рекомендаций героев на основе предпочтений."""

    def __init__(self, query):
        self.query = query

    def execute(self, prolog_thread):
        """Исполняет запрос Prolog для рекомендаций героев."""
        if not self.query:
            print("Неправильный формат запроса.")
            return

        # Печатаем запрос перед его отправкой в Prolog
        print(f"Запрос: {self.query}")

        # Запрос к Prolog
        res = prolog_thread.query(self.query)
        print(get_result(res, "Ничего не найдено", "Герои найдены"))


PROLOG_PATH = "/Users/daniilbatmanov/PycharmProjects/sii_lab2/data/facts.pl"  # Путь к файлу Prolog с базой данных героев

# Маппинг человеческих запросов в Prolog
QUERY_MAPPING = {
    "Give me a list of heroes": "hero(Hero).",
    "Give me a list of roles": "role(Role).",
    "Give me a list of attributes": "attribute(Attribute).",
    "Give me the role of hero": "hero_role(Hero, Role).",
    "Give me heroes with role": "hero_role(Hero, Role).",
    "Give me heroes with attribute": "hero_attribute(Hero, Attribute).",
    "Is hero a mage": "is_mage(Hero).",
    "Is hero a tank": "is_tank(Hero).",
    "Give me ultimate abilities of heroes": "has_ultimate(Hero, Ability).",
    "Give me heroes that are team fighters": "team_fighter(Hero).",
    "Give me heroes without role": "hero_no_role(Hero, Role).",
    "Give me heroes without attribute": "hero_no_attribute(Hero, Attribute)."
}

# Примеры запросов
examples = list(QUERY_MAPPING.keys())

# Шаблон для поиска корректных запросов
query_pattern = r"^[a-zA-Z0-9_() ,\.\+\-\*\/;]+$"
role_pattern = r"Give me heroes with role (\w+)"  # Регулярное выражение для поиска запроса "Give me heroes with <role>"
attribute_pattern = r"Give me heroes with attribute (\w+)"  # Для атрибутов
hero_role_pattern = r"Give me the role of (\w+)"  # Регулярное выражение для поиска запроса "Give me the role of <hero>"
no_role_pattern = r"Give me heroes without role (\w+)"  # Для поиска героев без определенной роли
no_attribute_pattern = r"Give me heroes without attribute (\w+)"  # Для поиска героев без определенного атрибута
mage_pattern = r"Is (\w+) a mage"  # Для поиска запросов "Is hero a mage"
tank_pattern = r"Is (\w+) a tank"  # Для поиска запросов "Is hero a tank"


with PrologMQI() as mqi:
    with mqi.create_thread() as prolog_thread:
        path = create_posix_path('/Users/daniilbatmanov/PycharmProjects/sii_lab2/data/facts.pl')
        prolog_thread.query(f'consult("{path}")')  # Загружаем базу данных героев
        print("Пример запросов:")
        print("\n- ".join(examples))
        print("Для завершения напишите - exit")

        while True:
            # Ввод пользователя
            ent = input('?- ')
            if ent == 'exit':
                break

            # Проверка на точные совпадения с маппингом
            if ent in QUERY_MAPPING:
                prolog_query = QUERY_MAPPING[ent]
                # Создаём объект HeroRecommendation и выполняем запрос
                recommendation = HeroRecommendation(prolog_query)
                recommendation.execute(prolog_thread)

            # Если запрос похож на "Give me heroes with role <role>"
            elif re.match(role_pattern, ent):
                match = re.search(role_pattern, ent)
                if match:
                    role = match.group(1).lower()  # Извлекаем роль из запроса и приводим к нижнему регистру
                    prolog_query = f"hero_role(Hero, {role})."  # Формируем Prolog запрос
                    # Создаём объект HeroRecommendation и выполняем запрос
                    recommendation = HeroRecommendation(prolog_query)
                    recommendation.execute(prolog_thread)

            # Если запрос похож на "Give me heroes with attribute <attribute>"
            elif re.match(attribute_pattern, ent):
                match = re.search(attribute_pattern, ent)
                if match:
                    attribute = match.group(1).lower()  # Извлекаем атрибут
                    prolog_query = f"hero_attribute(Hero, {attribute})."  # Формируем Prolog запрос
                    # Создаём объект HeroRecommendation и выполняем запрос
                    recommendation = HeroRecommendation(prolog_query)
                    recommendation.execute(prolog_thread)

            # Если запрос похож на "Give me the role of <hero>"
            elif re.match(hero_role_pattern, ent):
                match = re.search(hero_role_pattern, ent)
                if match:
                    hero = match.group(1).lower()  # Извлекаем имя героя
                    prolog_query = f"hero_role({hero}, Role)."  # Формируем Prolog запрос
                    # Создаём объект HeroRecommendation и выполняем запрос
                    recommendation = HeroRecommendation(prolog_query)
                    recommendation.execute(prolog_thread)

            # Если запрос похож на "Give me heroes without role <role>"
            elif re.match(no_role_pattern, ent):
                match = re.search(no_role_pattern, ent)
                if match:
                    role = match.group(1).lower()  # Извлекаем роль
                    prolog_query = f"hero_no_role(Hero, {role})."  # Формируем Prolog запрос
                    # Создаём объект HeroRecommendation и выполняем запрос
                    recommendation = HeroRecommendation(prolog_query)
                    recommendation.execute(prolog_thread)

            # Если запрос похож на "Give me heroes without attribute <attribute>"
            elif re.match(no_attribute_pattern, ent):
                match = re.search(no_attribute_pattern, ent)
                if match:
                    attribute = match.group(1).lower()  # Извлекаем атрибут
                    prolog_query = f"hero_no_attribute(Hero, {attribute})."  # Формируем Prolog запрос
                    # Создаём объект HeroRecommendation и выполняем запрос
                    recommendation = HeroRecommendation(prolog_query)
                    recommendation.execute(prolog_thread)

            # Если запрос похож на "Is <hero> a mage"
            elif re.match(mage_pattern, ent):
                match = re.search(mage_pattern, ent)
                if match:
                    hero = match.group(1).lower()  # Извлекаем имя героя
                    prolog_query = f"is_mage({hero})."  # Формируем Prolog запрос
                    # Создаём объект HeroRecommendation и выполняем запрос
                    recommendation = HeroRecommendation(prolog_query)
                    recommendation.execute(prolog_thread)

            # Если запрос похож на "Is <hero> a tank"
            elif re.match(tank_pattern, ent):
                match = re.search(tank_pattern, ent)
                if match:
                    hero = match.group(1).lower()  # Извлекаем имя героя
                    prolog_query = f"is_tank({hero})."  # Формируем Prolog запрос
                    # Создаём объект HeroRecommendation и выполняем запрос
                    recommendation = HeroRecommendation(prolog_query)
                    recommendation.execute(prolog_thread)

            else:
                print("Неправильный формат ввода! Используйте один из предложенных запросов.")
