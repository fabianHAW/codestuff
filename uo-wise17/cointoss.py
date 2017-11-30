import random


def toss_coin(num_flips, filename):
    flips = []
    counter_heads = 0
    counter_heads_temp = 0
    counter_tails = 0
    counter_tails_temps = 0
    for x in [random.randint(0, 1) for x in range(num_flips)]:
        if x == 1:
            flips.append('Heads')
            if flips[len(flips) - 1] == 'Heads':
                counter_tails = max(counter_tails, counter_tails_temps)
                counter_tails_temps = 0
                counter_heads_temp += 1
        else:
            flips.append('Tails')
            if flips[len(flips) - 1] == 'Tails':
                counter_heads = max(counter_heads, counter_heads_temp)
                counter_heads_temp = 0
                counter_tails_temps += 1
    counter_heads = max(counter_heads, counter_heads_temp)
    counter_tails = max(counter_tails, counter_tails_temps)
    heads = sum([x == 'Heads' for x in flips])
    tails = num_flips - heads
    with open(filename, 'a') as f:
        f.write('{};{};{};{};{};{}\n'.format(
            heads, tails, counter_heads, counter_tails,
            heads - tails, tails - heads))


if __name__ == '__main__':
    rounds = 1000
    num_flips = 100000
    filename = 'coin_toss_{}_2.csv'.format(rounds)
    with open(filename, 'w') as f:
        f.write('#heads;#tails;max heads series;max tails series;euro heads;'
                'euro tails\n')
    for _ in range(rounds):
        toss_coin(num_flips, filename)
