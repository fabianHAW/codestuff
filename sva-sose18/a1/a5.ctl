/*5. Immer wenn der Sender im 1-Zustand ist, kommt irgendwann auch ein 1-Acknowledgement und ab dann (irgendwann) ist der Sender im 0-Zustand.*/
/*eins von den beiden? die zweite ist aus dem marci-manual.pdf*/
EF Sender == 2 -> EF Receiver == 2 -> EF Sender == 1;
!E (Sender == 2 U Sender == 1);