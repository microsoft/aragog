(let ((a!1 (and (= event_type 1) (= srcIp |"10.10.5.2"|) (not (and (= event_type 1) (= srcIp |"10.10.5.1"|))))) (a!2 (and (not (and (= event_type 1) (= srcIp |"10.10.5.2"|))) (= event_type 1) (= srcIp |"10.10.5.1"|)))) (or (and (= event_type 1) (= srcIp |"10.10.5.2"|) (= srcIp |"10.10.5.1"|)) a!1 a!2))
0
1
(and (= event_type 1) (or (= srcIp |"10.10.5.1"|) (= srcIp |"10.10.5.2"|)))
srcIp


0
true;0;0;(and (not (and (= event_type 1) (= srcIp |"10.10.5.2"|))) (not (and (= event_type 1) (= srcIp |"10.10.5.1"|))))
false;0;1;(let ((a!1 (and (= event_type 1) (= srcIp |"10.10.5.2"|) (not (and (= event_type 1) (= srcIp |"10.10.5.1"|))))) (a!2 (and (not (and (= event_type 1) (= srcIp |"10.10.5.2"|))) (= event_type 1) (= srcIp |"10.10.5.1"|)))) (or (and (= event_type 1) (= srcIp |"10.10.5.2"|) (= srcIp |"10.10.5.1"|)) a!1 a!2))
false;1;1;(let ((a!1 (and (= event_type 1) (= srcIp |"10.10.5.2"|) (not (and (= event_type 1) (= srcIp |"10.10.5.1"|))))) (a!2 (and (not (and (= event_type 1) (= srcIp |"10.10.5.2"|))) (= event_type 1) (= srcIp |"10.10.5.1"|)))) (or (and (= event_type 1) (= srcIp |"10.10.5.2"|) (= srcIp |"10.10.5.1"|)) a!1 a!2))
true;1;0;(and (not (and (= event_type 1) (= srcIp |"10.10.5.2"|))) (not (and (= event_type 1) (= srcIp |"10.10.5.1"|))))
