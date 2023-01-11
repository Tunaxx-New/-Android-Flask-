import uuid
from random import randint


def getHash() -> str:
    return uuid.uuid4().hex


def generateCode() -> str:
    n = 6
    return ''.join(["{}".format(randint(0, 9)) for num in range(0, n)])
