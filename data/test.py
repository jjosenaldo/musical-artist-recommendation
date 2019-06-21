with open("input100.dat", "r") as ins:
    sum = 0
    for line in ins:
        line = line.split(" ")
        if int(line[0]) > 5: break

        sum += float(line[2])
    print(sum)