import sys
import os
import subprocess
import time
import socket
from TimeEvent import TimeEvent
import json

def read_time_data(fname, prec_in_second):
    time_events_list = []
    with open(fname, "r") as tf:
        for l in tf:
            time_events_list.append(TimeEvent(l.strip(), prec_in_second))
    return time_events_list

def send_traffic(dst, f_size):
    subprocess.Popen("yes | head -n %s | netcat %s %s" % (f_size, dst, 5058), shell=True, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

if __name__ == "__main__":

    if len(sys.argv) < 4:
        print("Don't forget worker [0-num_workers] [total_servers] [file_initial]!")
        exit()

    prec_in_second = 0.00001
    X = 1/prec_in_second
    num_servers = int(sys.argv[2])
    worker = sys.argv[1]
    file_initial = sys.argv[3]
    sys.stderr.write("loading data...\n")
    events_list = read_time_data(file_initial + worker + '.txt', prec_in_second)
    sys.stderr.write("starting the traffic...\n")
    myhostname = socket.gethostname()
    begin = time.time()
    indices = [0] * (num_servers-1) # remove itself
    max_time = 1900

    with open('servers.json') as f:
        servers_dict = json.load(f)

    while time.time() - begin < max_time:

        cur_time = int((time.time() - begin) * X)

        for s in range(0, num_servers-1):
            if indices[s] < len(events_list[s].inst_events):
                if (events_list[s].inst_events[indices[s]][0] <= cur_time):
                    flow_length = events_list[s].inst_events[indices[s]][1]
                    dst = servers_dict[str(events_list[s].dst+1)]
                    send_traffic(dst, flow_length)
                    print("%s is sending %s bytes to %s at %s" % (myhostname, flow_length, dst, cur_time))
                    indices[s] = indices[s] + 1
