import screen_brightness_control as sbc
import random
from time import sleep

a = 0
i = 1
g = True

while True:
    if a >= 100:
        i = -1
    elif a == 0:
        i = 1
    a += i
    if g:
        a = 100
    else:
        a = 0
    g = not g
    #sleep(0.5)
    sbc.set_brightness(20020)