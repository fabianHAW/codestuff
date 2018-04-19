import matplotlib.pyplot as plt
import numpy as np

h = 0.002
x_end = 120

steps = int(x_end / h)


def x1_dot(x, y):
    return -10 * (x - y)


def y1_dot(x, y, z):
    return (40 - z) * x - y


def y1_dot_change(x, y, z):
    return (40.000000001 - z) * x - y


def z1_dot(x, y, z):
    return x * y - 2.67 * z


def runtime(y_method):
    # Array für unabhängige Variable
    t = np.zeros(steps)
    # Output für abhängige Variable (berechnet mit RK2)
    xRK2 = np.zeros(steps)
    yRK2 = np.zeros(steps)
    zRK2 = np.zeros(steps)

    xRK2[0] = 0.01
    yRK2[0] = 0.01
    zRK2[0] = 0.0

    # RK2
    for i in range(steps - 1):
        k1 = h * x1_dot(xRK2[i], yRK2[i])
        l1 = h * y_method(xRK2[i], yRK2[i], zRK2[i])
        m1 = h * z1_dot(xRK2[i], yRK2[i], zRK2[i])

        k2 = h * x1_dot(xRK2[i] + k1/2, yRK2[i] + l1/2)
        l2 = h * y_method(xRK2[i] + k1/2, yRK2[i] + l1/2, zRK2[i] + m1/2)
        m2 = h * z1_dot(xRK2[i] + k1/2, yRK2[i] + l1/2, zRK2[i] + m1/2)

        xRK2[i+1] = xRK2[i] + k2
        yRK2[i+1] = yRK2[i] + l2
        zRK2[i+1] = zRK2[i] + m2
        t[i+1] = t[i] + h

    return xRK2, yRK2, zRK2, t


xRK2_1, yRK2_1, zRK2_1, t_1 = runtime(y1_dot)
fig1, ax1 = plt.subplots(figsize=(10, 5))
print('steps: {} mit schrittweite: {}'.format(steps, h))

ax1.plot(t_1, xRK2_1, label='RK2_x_{}'.format(h))
ax1.plot(t_1, yRK2_1, label='RK2_y_{}'.format(h))
ax1.plot(t_1, zRK2_1, label='RK2_z_{}'.format(h))
ax1.legend()
ax1.set(xlabel='x', ylabel='y',
        title='3 DGL 1. Ordnung mit RK2 berechnet mit Schrittweite: '
        '{}'.format(h))
ax1.grid()


xRK2_2, yRK2_2, zRK2_2, t_2 = runtime(y1_dot_change)
fig2, ax2 = plt.subplots(figsize=(10, 5))
print('steps: {} mit schrittweite: {}'.format(steps, h))

ax2.plot(t_1, xRK2_1, label='RK2_x_1_{}'.format(h))
ax2.plot(t_2, xRK2_2, label='RK2_x_2_{}'.format(h))
ax2.legend()
ax2.set(xlabel='x', ylabel='y',
        title='2 DGL 1. Ordnung mit RK2 berechnet mit Schrittweite: '
        '{}. Unterscheidung von x(t)'.format(h))
ax2.grid()

# fig.savefig("DGL_1O.png")
plt.show()
