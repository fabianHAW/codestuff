import matplotlib.pyplot as plt
import numpy as np
import math

step_values = dict.fromkeys([0.001, 0.02], 0)
x_end = 31

for x in step_values.keys():
    step_values[x] = int(x_end / x)


def y1_dot(y1, y2):
    return 6 * (1 - math.pow(y2, 2)) * y1 - y2


def y2_dot(y1):
    return y1


for h, step in step_values.items():
    fig, ax = plt.subplots(figsize=(10, 5))
    print('aktueller step: {} mit schrittweite: {}'.format(step, h))
    # Array für unabhängige Variable
    x = np.zeros(step)
    # Output für abhängige Variable (berechnet mit Euler explizit)
    y1_euler = np.zeros(step)
    y2_euler = np.zeros(step)
    # Output für abhängige Variable (berechnet mit RK2)
    y1RK2 = np.zeros(step)
    y2RK2 = np.zeros(step)

    y1_euler[0] = 1

    # Euler_ex
    for i in range(step - 1):
        y1_euler[i+1] = y1_euler[i] + h * y1_dot(y1_euler[i], y2_euler[i])
        y2_euler[i+1] = y2_euler[i] + h * y2_dot(y1_euler[i])
        x[i+1] = x[i] + h

    ax.plot(x, y2_euler, label='euler_{}'.format(h))

    x = np.zeros(step)
    y1RK2[0] = 1

    # RK2
    for i in range(step - 1):
        k1 = h * y1_dot(y1RK2[i], y2RK2[i])
        l1 = h * y2_dot(y1RK2[i])

        k2 = h * y1_dot(y1RK2[i] + k1/2, y2RK2[i] + l1/2)
        l2 = h * y2_dot(y1RK2[i] + k1/2)

        y1RK2[i+1] = y1RK2[i] + k2
        y2RK2[i+1] = y2RK2[i] + l2
        x[i+1] = x[i] + h

    ax.plot(x, y2RK2, label='RK2_{}'.format(h))
    ax.legend()
    ax.set(xlabel='x', ylabel='y',
           title='DGL 2. Ordnung mit Euler explizit und RK2 berechnet mit '
           'Schrittweite: {}'.format(h))
    ax.grid()

# fig.savefig("DGL_1O.png")
plt.show()
