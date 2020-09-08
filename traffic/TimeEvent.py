class TimeEvent:
    src = -1
    dst = -1
    events = []
    prec = 0.000000001
    inst_events = []

    def __init__(self, text, prec_in_second=0.000000001):
        items = text.split(": ")
        self.src = int(items[0].split(", ")[0][1:])
        self.dst = int(items[0].split(", ")[1][:-1])
        self.events = map(lambda t: t.split(" "), items[1].split("; "))
        self.round(prec_in_second)

    def toString(self):
        text = "<%s, %s>: " % (self.src, self.dst)
        event_text = "; ".join(map(lambda t: str(t[0]*self.prec)+" "+str(t[1]), self.inst_events))
        return text+event_text

    def round(self, prec_in_second):
        self.inst_events = []
        self.prec = prec_in_second
        X = 1/self.prec
        # round
        tmp_events = map(lambda t: [int(float(t[0])*X), int(t[1])], self.events)
        # merge
        cur_event = tmp_events[0]
        for i in range(1, len(tmp_events)):
            if cur_event[0] == tmp_events[i][0]:
                cur_event[1] += tmp_events[i][1]
            else:
                self.inst_events.append(cur_event)
                cur_event = tmp_events[i]
        self.inst_events.append(cur_event)

    def getTotalSize(self):
        return sum([t[1] for t in self.inst_events])

    def merge(self, other):
        l1 = self.inst_events
        l2 = other.inst_events
        p1 = 0
        p2 = 0
        while p1 < len(l1) and p2 < len(l2):
            if l1[p1][0] == l2[p2][0]:
                l1[p1][1] += l2[p2][1]
                p1 += 1
                p2 += 1
            elif l1[p1][0] < l2[p2][0]:
                p1 += 1
            else:
                l1.insert(p1, l2[p2])
                p1 += 1
                p2 += 1
        if p2 < len(l2):
            while p2 < len(l2):
                l1.append(l2[p2])
                p2 += 1

def from_file(fname, prec_in_second):
    time_events_list = []
    with open(fname, "r") as tf:
        for l in tf:
            time_events_list.append(TimeEvent(l.strip(), prec_in_second))
    return time_events_list
