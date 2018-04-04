/*Aufgabe 6*/
E ( (EF Sender == 1 & EF Receiver == 1) U EF Sender == 2);
A ( (Sender == 1 -> S_Lost_Or_Forward_B0 == 1 -> Receiver == 1 -> R_Lost_Or_Forward_B0 == 1) U Sender == 2);