import random


def toss_coin(num_flips, cointoss_data_filename, cointoss_history_filename):
    flips = []
    balance = []
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
        balance.append(counter_heads_temp - counter_tails_temps)
    counter_heads = max(counter_heads, counter_heads_temp)
    counter_tails = max(counter_tails, counter_tails_temps)
    heads = sum([x == 'Heads' for x in flips])
    tails = num_flips - heads
    with open(cointoss_data_filename, 'a') as f:
        f.write('{};{};{};{};{};{}\n'.format(
            heads, tails, counter_heads, counter_tails,
            heads - tails, tails - heads))
    with open(cointoss_history_filename, 'a') as f:
        for flip in range(num_flips):
            f.write('{};'.format(balance[flip]))
        f.write('\n')


if __name__ == '__main__':
    rounds = 5
    num_flips = 1000
    cointoss_data_filename = 'coin_toss_data_{}.csv'.format(rounds)
    cointoss_history_filename = 'coin_toss_history_{}.csv'.format(rounds)
    with open(cointoss_data_filename, 'w') as f:
        f.write('#heads;#tails;max heads series;max tails series;euro heads;'
                'euro tails\n')
    with open(cointoss_history_filename, 'w') as f:
        f.write('Coin toss balance history\n')
    for _ in range(rounds):
        toss_coin(num_flips, cointoss_data_filename, cointoss_history_filename)
