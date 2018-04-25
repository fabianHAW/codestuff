import matplotlib.pyplot as plt
import numpy as np
import math

step_values = dict.fromkeys([0.001, 0.003, 0.004, 0.005], 0)
x_end = 0.2

for x in step_values.keys():
    step_values[x] = int(x_end / x)


def y_dot(x, y):
    return 10 - 500 * y + 5000 * x


def y_analytic_func(x):
    return 10 * x + math.exp(-500 * x)


for h, step in step_values.items():
    fig, ax = plt.subplots(figsize=(10, 5))
    print('aktueller step: {} mit schrittweite: {}'.format(step, h))
    # Array für unabhängige Variable
    x = np.zeros(step)
    # Output für abhängige Variable (berechnet mit Euler explizit)
    y_euler_ex = np.zeros(step)
    # Output für abhängige Variable (berechnet mit RK2)
    y_RK2 = np.zeros(step)
    # Output für abhängige Variable (berechnet mit Euler implizit)
    y_euler_imp = np.zeros(step)
    # Output für analytische Lösung
    y_analytic = np.zeros(step)

    y_euler_ex[0] = 1

    # Euler_ex
    for i in range(step - 1):
        y_euler_ex[i+1] = y_euler_ex[i] + h * y_dot(x[i], y_euler_ex[i])
        x[i+1] = x[i] + h

    ax.plot(x, y_euler_ex, label='euler_ex_{}'.format(h))

    x = np.zeros(step)
    y_RK2[0] = 1

    # RK2
    for i in range(step - 1):
        k1 = h * y_dot(x[i], y_RK2[i])
        k2 = h * y_dot(x[i] + h/2, y_RK2[i] + k1/2)

        y_RK2[i+1] = y_RK2[i] + k2
        x[i+1] = x[i] + h

    ax.plot(x, y_RK2,   label='RK2_{}'.format(h))

    x = np.zeros(step)
    y_euler_imp[0] = 1

    # Euler_imp
    for i in range(step - 1):
        x[i+1] = x[i] + h
        y_euler_imp[i+1] = (
            y_euler_imp[i] + 10 * h + 5000 * x[i+1] * h) / (500 * h + 1)

    ax.plot(x, y_euler_imp, label='euler_imp_{}'.format(h))

    # x = np.zeros(step)
    # analytic solution
    for i in range(step):
        y_analytic[i] = y_analytic_func(i * h)

    ax.plot(x, y_analytic, label='y_analytic_{}'.format(h))

    ax.legend()
    ax.set(xlabel='x', ylabel='y',
           title='DGL 1. Ordnung mit Euler explizit/implizit und RK2 '
           'berechnet, sowie die analytische Lösung mit Schrittweite: '
           '{}'.format(h))
    ax.grid()

# fig.savefig("DGL_1O.png")
plt.show()
