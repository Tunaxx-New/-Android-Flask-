from utils import strings

#send_message_blueprint = Blueprint('sendMessage', __name__)


#@send_message_blueprint.route('/sendMessage', methods=['POST'])
#@login_required
def sendMessage(user, data, to_id):
    from main import db
    from Models.Chat.Message import Messages
    from Models.User.Role import Roles
    from Models.User.UserRole import UserRoles

    if None in [data, to_id]:
        return dict(status=False, reason=strings.MISSED_DATA)
    try:
        int(to_id)
    except ValueError:
        return dict(status=False, reason=strings.MISSED_DATA)

    role = Roles.get(role="tech_support")
    if role is None:
        return dict(status=False, reason=strings.WRONG_SEND_USER)

    to_user_role = UserRoles.get(user_id=to_id)
    from_user_role = UserRoles.get(user_id=user.id)

    if not (to_user_role is None) ^ (from_user_role is None):
        return dict(status=False, reason=strings.WRONG_SEND_USER)
    if to_user_role is not None:
        if not (to_user_role.role_id == role.id):
            return dict(status=False, reason=strings.WRONG_SEND_USER)
    if from_user_role is not None:
        if not (from_user_role.role_id == role.id):
            return dict(status=False, reason=strings.WRONG_SEND_USER)

    message = Messages(data=data, from_id=user.id, to_id=to_id)
    db.session.add(message)
    db.session.commit()
    print(message.created)

    return message.created
