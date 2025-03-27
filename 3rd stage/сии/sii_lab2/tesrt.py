from swiplserver import PrologMQI, PrologThread

with PrologMQI() as mqi:
    with mqi.create_thread() as prolog_thread:
        prolog_thread.query("assert(fact(a)).")
        result = prolog_thread.query("fact(X).")
        print(result)  # Должен вывести: {'X': 'a'}
