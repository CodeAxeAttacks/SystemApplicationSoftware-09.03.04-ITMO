# lab4, var4, yaml –> xml
import re
schedule = open('schedule.yaml')
openedSchedule = schedule.readlines()
openedResFile = open('resFile.xml', 'w')
mas = [len(openedSchedule)]
for l in range(1, len(openedSchedule)):
    mas.append(re.search(r"^\s+", openedSchedule[l]).end() + re.search(r"^\s+", openedSchedule[l]).end() % 2)
mas[0] = 0
openedResFile.write("<?xml version=" + '"' + "1.0" + '"' + "?>" + '\n' + "<result>" + '\n')
arr = []
for i in range(0, len(openedSchedule)):
    part = openedSchedule[i]
    if (mas[i]) // 2 <= (len(arr) - 1):
        openedResFile.write(' ' * mas[i] + "</" + arr.pop(-1) + '>' + "\n")
    if re.search(r".+:\n", part) != None:
        arr.append(part.strip()[:-1])
        openedResFile.write(' ' * (part.count(' ') + 1) + '<' + re.search(r"\w+", part).group() + '>' + '\n')
    elif re.search(r"\[", part) != None:
        s = ""
        for j in re.search(r"\[[\w+, ]+\]", part).group():
            if j != "[" and j != "]" and j != " " and j != ",":
                s += j
            else:
                if s != "":
                    openedResFile.write(re.search(r"^\s+", part).group() + '<' + re.search(r"\w+", part).group() + '>' + s + '<' + '/' + re.search(r"\w+", part).group() + '>' + '\n')
                s = ""
    elif re.search(r"\w+: \w+", part) != None:
        openedResFile.write(re.search(r"^\s+", part).group() + '<' + re.search(r"\w+", part).group() + '>' + part[(part.find(':') + 2):][:-1] + '<' + '/' + re.search(r"\w+", part).group() + '>' + '\n')
    elif re.search(r"\w+: \|", part) != None:
        openedResFile.write(re.search(r"^\s+", part).group() + '<' + re.search(r"\w+", part).group() + '>' + openedSchedule[i + 1][:-1][re.search(r"^\s+", openedSchedule[i + 1]).end():] + '</' + re.search(r"\w+", part).group() + '>' + '\n')
for i in range((len(arr) - 1), -1, -1):
    openedResFile.write('  ' * (i + 1) + "</" + arr.pop(-1) + ">" + "\n")
openedResFile.write("</result>")
openedResFile.close()