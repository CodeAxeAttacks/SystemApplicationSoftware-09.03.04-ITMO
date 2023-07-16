# lab4, var4, yaml –> xm
import xmlplain, yaml
with open("schedule.yaml") as inf:
    root = yaml.safe_load(inf)
with open("resFile.xml", 'w') as outf:
    xmlplain.xml_from_obj(root, outf, pretty=True)