schedule = open('schedule.yaml')
openedSchedule = schedule.readlines()
openedResFile = open('resFile.xml', 'w')
s = ""
mas = [len(openedSchedule)]
for l in range(0, len(openedSchedule)):
    max = 0
    k = 0
    for j in range(0, len(openedSchedule[l])):
        h = openedSchedule[l]
        if h[j] == " ":
            k += 1
        else:
            if max < k:
                max = k
                mas.append(max + max % 2)
            k = 0
mas[0] = 0
openedResFile.write("<?xml version=" + '"' + "1.0" + '"' + "?>" + "\\n" + "<result>" + "\\n")
arr = []
for i in range(0, len(openedSchedule)):
    part = openedSchedule[i]
    if (mas[i]) // 2 <= (len(arr) - 1):
        openedResFile.write(' ' * mas[i] + "</" + arr.pop(-1) + '>' + "\\n")
    if part[-2] == ":":
        arr.append(part.strip()[:-1])
        openedResFile.write(' ' * (part.count(' ') + 1) + '<' + part[:-2].strip() + '>' + '\\n')
    elif part.find('[') != -1:
        for j in range(0, len(part)):
            if part[j] != ":":
                s += part[j]
            else:
                v = s
                s = ""
                break
        for j in range(part.find('[') + 1, part.find(']') + 1):
            if (part[j] != ",") and (part[j] != " ") and (part[j] != ']'):
                s += part[j]
            else:
                if s != "":
                    openedResFile.write(' ' * (v.count(' ') + 1) + '<' + v.strip() + '>' + s + '<' + '/' + v.strip() + '>' + '\\n')
                s = ""
    elif (part[-2] != ":") and (part.find(':') != -1) and (part.find('|') == -1):
        for j in range(0, len(part)):
            if part[j] != ":":
                s += part[j]
            else:
                v = s
                openedResFile.write(' ' * (part.count(' ')) + '<' + v.strip() + '>' + part[(part.find(':') + 2):][:-1] + '<' + '/' + v.strip() + '>' + '\\n')
                s = ""
                break
    elif (part[-2] != ":") and (part.find(':') != -1) and (part.find('|') != -1):
        mx = 0
        k = 0
        for j in range(0, len(openedSchedule[i + 1])):
            h = openedSchedule[i + 1]
            if h[j] == " ":
                k += 1
            else:
                if mx < k:
                    mx = k
                k = 0
        for j in range(0, len(part)):
            if part[j] != ":":
                s += part[j]
            else:
                v = s
                openedResFile.write(' ' * (part.count(' ') + 1) + '<' + v.replace(' ', '') + '>' + openedSchedule[i + 1][mx:][:-1] + '<' + '/' + v.replace(' ', '') + '>' + '\\n')
                s = ""
                break
for i in range((len(arr) - 1), -1, -1):
    openedResFile.write('  ' * (i + 1) + "</" + arr.pop(-1) + ">" + "\\n")
openedResFile.write("</result>")
openedResFile.close()
