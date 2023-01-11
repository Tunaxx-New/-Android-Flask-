from flask import Blueprint

from main import db
from utils.hash import getHash
from Routes.UserData.Wrapper.LoginRequired import login_required
from Routes.UserData.Wrapper.NewHash import new_hash


getUser_blueprint = Blueprint('getUser', __name__)


@getUser_blueprint.route('/getUser', methods=['POST'])
@login_required
@new_hash
def getUser(user, uhash):
    return dict(
        status=True,
        hash=uhash,
        name=user.name,
        surname=user.surname,
        email=user.email,
        phone=user.phone,
        phone_activated=user.phone_activated,
        email_activated=user.email_activated,
        registered=user.registered
        )