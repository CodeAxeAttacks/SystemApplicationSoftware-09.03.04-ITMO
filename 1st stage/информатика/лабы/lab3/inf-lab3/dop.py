import re
test = input()
print(re.findall(r"\b([А-Яа-я])[\w]*\1", test))