from flask import request
from flask_socketio import disconnect

"""
    This function check, if user logged in and which type
"""


def login_required_s(uhash):
    if uhash is None:
        disconnect(request.sid)
        return None
    else:
        from Models.User.User import Users
        user = Users.get(hash=uhash)
        if user is None:
            disconnect(request.sid)
            return None
        return user
