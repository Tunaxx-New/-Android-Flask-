from flask import request
from functools import wraps

from utils import strings
from Models.User.User import Users

"""
    This function check, if user logged in and which type
"""


def login_required(f):
    @wraps(f)
    def wrap():
        uhash = request.form.get('hash', None)
        phone = request.form.get('phone', None)
        password = request.form.get('password', None)
        if uhash is None:
            if None in [phone, password]:
                return dict(status=False, reason=strings.MISSED_DATA)
            else:
                user = Users.get(phone=phone)

                if user is None:
                    return dict(status=False, reason=strings.WRONG_PHONE_NUMBER)
                if user.password != password:
                    return dict(status=False, reason=strings.WRONG_PASSWORD)
                return f(user)
        else:
            user = Users.get(hash=uhash)
            if user is None:
                return dict(status=False, reason=strings.WRONG_HASH)
            return f(user)

    return wrap
