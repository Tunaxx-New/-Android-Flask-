import re

email_regex = r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b'


def isPhoneValid(phone: str) -> bool:
    if len(phone) != 11:
        return False
    for p in phone:
        if not p.isnumeric():
            return False
    return True


def isEmailValid(email: str) -> bool:
    if re.fullmatch(email_regex, email):
        return True
    return False
