% Zeichnet Erde, Mond und die Satellitenbahn
% Satellitenbahn:  x_Sat, y_Sat

clf reset;                 % alte Plots entfernen 

plot(x_space, y_space, 'red'); % zeichne Satellitenbahn
axis equal;                % gleicher Ma�stab in x- und y-Richtung
hold on;                   % weitere Objekte in figure zeichnen

% --- Erde zeichnen -------
rE=6378000; % Erdradius [m] 

x_E=0:0.05:2*pi; % x-Werte der Kreispunkte berechnen
x_E=sin(x_E) * rE;
y_E=0:0.05:2*pi; % y-Werte der Kreispunkte berechnen
y_E=cos(y_E) * rE;

patch(x_E, y_E,'blue'); % Vollkreis zeichnen
% --------------------------

hold on;

% --- Mond zeichnen -------
rM=1700000; % Mondradius [m]
Mondentfernung = 380000000; %[m]
   
x_M=0:0.05:2*pi; % x-Werte der Kreispunkte berechnen
x_M=sin(x_M) * rM;
y_M=0:0.05:2*pi; % y-Werte der Kreispunkte berechnen
y_M=cos(y_M) * rM - Mondentfernung; 

patch(x_M, y_M,'yellow'); % Vollkreis zeichnen
% ---------------------------