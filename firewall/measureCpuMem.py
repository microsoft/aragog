import psutil
from os import listdir
from os.path import isfile
from time import sleep
from datetime import datetime
import sys

def main():
    nameToPidDict = dict()
    path = '/tmp/'
    for f in listdir(path):
        if f[:5] == 'flink' and f[-4:] == '.pid':
            # print(f)
            file = open(path + f, 'r')
            nameToPidDict[f] = int(file.read().strip())
            file.close()
        elif ('localVerifier.pid' in f):
            file = open(path + f, 'r')
            nameToPidDict[f] = int(file.read().strip())
            file.close()
        else:
            # print("not flink: " + f)
            continue

    logFile = open('logCpuMem' + sys.argv[1] + '.txt', 'w')
    nameToProcessDict = dict()

    for name, pid in nameToPidDict.items():
        nameToProcessDict[name] = psutil.Process(pid)
    # processList = [ for pid in pidList]

    for x in range(0,6000):
        for name, process in nameToProcessDict.items(): 
            logFile.write(str(datetime.now().strftime("%H:%M:%S.%f")) + ',' + str(process.cpu_percent()) + 
            ',' + str(process.memory_info().rss) + ',' + name + '\n')
        sleep(0.1)
        

if __name__ == '__main__':
    main()