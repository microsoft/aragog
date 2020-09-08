(let ((a!1 (and (= event_type 1) (= srcIp |"10.10.4.1"|) (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))))) (a!2 (and (not (and (= event_type 1) (= srcIp |"10.10.4.1"|))) (= event_type 1) (= srcIp |"10.10.4.2"|))) (a!3 (and (= event_type 1) (= srcIp |"10.10.4.2"|) (not (and (= event_type 1) (= srcIp |"10.10.4.1"|))))) (a!4 (and (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))) (= event_type 1) (= srcIp |"10.10.4.1"|)))) (or a!1 (and (= event_type 1) (= srcIp |"10.10.4.1"|) (= srcIp |"10.10.4.2"|)) a!2 (and (= event_type 1) (= srcIp |"10.10.4.2"|) (= srcIp |"10.10.4.1"|)) a!3 a!4))
0
1
(and (= event_type 1) (or (= srcIp |"10.10.4.1"|) (= srcIp |"10.10.4.2"|)))
srcIp


0
false;0;1;(let ((a!1 (and (= event_type 1) (= srcIp |"10.10.4.1"|) (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))))) (a!2 (and (not (and (= event_type 1) (= srcIp |"10.10.4.1"|))) (= event_type 1) (= srcIp |"10.10.4.2"|))) (a!3 (and (= event_type 1) (= srcIp |"10.10.4.2"|) (not (and (= event_type 1) (= srcIp |"10.10.4.1"|))))) (a!4 (and (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))) (= event_type 1) (= srcIp |"10.10.4.1"|)))) (or a!1 (and (= event_type 1) (= srcIp |"10.10.4.1"|) (= srcIp |"10.10.4.2"|)) a!2 (and (= event_type 1) (= srcIp |"10.10.4.2"|) (= srcIp |"10.10.4.1"|)) a!3 a!4))
true;0;0;(let ((a!1 (and (not (and (= event_type 1) (= srcIp |"10.10.4.1"|))) (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))))) (a!2 (and (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))) (not (and (= event_type 1) (= srcIp |"10.10.4.1"|)))))) (or a!1 a!2))
true;1;0;(and (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))) (not (and (= event_type 1) (= srcIp |"10.10.4.1"|))))
false;1;1;(let ((a!1 (and (= event_type 1) (= srcIp |"10.10.4.2"|) (not (and (= event_type 1) (= srcIp |"10.10.4.1"|))))) (a!2 (and (not (and (= event_type 1) (= srcIp |"10.10.4.2"|))) (= event_type 1) (= srcIp |"10.10.4.1"|)))) (or (and (= event_type 1) (= srcIp |"10.10.4.2"|) (= srcIp |"10.10.4.1"|)) a!1 a!2))
