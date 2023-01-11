from flask import Blueprint

from Models.Chat.Message import Messages
from Models.User.Role import Roles
from Models.User.User import Users
from Models.User.UserRole import UserRoles
from Routes.UserData.Wrapper.LoginRequired import login_required
from main import db
from utils import strings

getTechSupports_blueprint = Blueprint('getTechSupports', __name__)


@getTechSupports_blueprint.route('/getTechSupports', methods=['POST'])
@login_required
def getTechSupports(user):
    role = Roles.get(role="tech_support")
    if role is None:
        return dict(status=False, reason=strings.WRONG_SEND_USER)

    user_role = UserRoles.get(user_id=user.id)

    users = None
    if user_role is None:
        users = UserRoles.getAll(role_id=role.id)
    else:
        users = Messages.getToTech(user.id)


    full_names = ""
    ids = ""

    for opponent in users:
        opponent_user = Users.get(id=opponent.user_id)
        ids += str(opponent_user.id) + ','
        full_names += opponent_user.name + ' ' + opponent_user.name + ','

    full_names = full_names[:-1]
    ids = ids[:-1]

    return dict(
        status=True,
        full_names=full_names,
        ids=ids
        )