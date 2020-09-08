false
0
2
true
srcIp

$a
0
true;0;0;(not (and (= event_type 4) (= $a srcIp)))
false;0;1;(and (= event_type 4) (= $a srcIp))
false;1;2;(let ((a!1 (not (and (not (= event_type 5)) (not (= srcIp $a))))))(let ((a!2 (and (not (and (= event_type 4) (= $a srcIp))) a!1 (= event_type 1) (= srcIp $a))) (a!3 (and (not (and (= event_type 4) (= $a srcIp))) (= event_type 1) (= srcIp $a) a!1))) (or a!2 a!3)))
false;1;0;(let ((a!1 (not (and (not (= event_type 5)) (not (= srcIp $a))))))(let ((a!2 (and (not (and (= event_type 4) (= $a srcIp))) (not (and (= event_type 1) (= srcIp $a))) a!1)) (a!3 (and (not (and (= event_type 4) (= $a srcIp))) a!1 (not (and (= event_type 1) (= srcIp $a)))))) (or a!2 a!3)))
true;1;1;(let ((a!1 (not (and (not (= event_type 5)) (not (= srcIp $a))))) (a!3 (and (not (and (= event_type 4) (= $a srcIp))) (not (and (= event_type 1) (= srcIp $a))) (not (= event_type 5)) (not (= srcIp $a)))) (a!4 (and (not (and (= event_type 4) (= $a srcIp))) (not (= event_type 5)) (not (= srcIp $a)) (not (and (= event_type 1) (= srcIp $a))))))(let ((a!2 (and (= event_type 4) (= $a srcIp) a!1 (not (and (= event_type 1) (= srcIp $a))))) (a!5 (and (= event_type 4) (= $a srcIp) (not (and (= event_type 1) (= srcIp $a))) a!1))) (or a!2 a!3 a!4 a!5)))
true;2;0;(not (and (= event_type 4) (= $a srcIp)))
false;2;1;(and (= event_type 4) (= $a srcIp))
