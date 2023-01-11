from functools import wraps

from utils.hash import getHash
from main import db

"""
    This function changes and return new hash
"""

def new_hash(f):
    @wraps(f)
    def wrap(user):
        uhash = getHash()
        user.hash = uhash
        db.session.commit()
        return f(user, uhash)
    return wrap
