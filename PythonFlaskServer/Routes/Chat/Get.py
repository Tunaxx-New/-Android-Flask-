from flask import request

from flask import Blueprint
from Routes.UserData.Wrapper.LoginRequired import login_required
from Models.Chat.Message import Messages
from utils import strings

get_message_blueprint = Blueprint('getLastMessages', __name__)


@get_message_blueprint.route('/getLastMessages', methods=['POST'])
@login_required
def getLastMessages(user):
    to_id = request.form.get("to_id", None)
    start = request.form.get("start", None)
    end = request.form.get("end", None)

    if None in [to_id, start, end]:
        return dict(status=False, reason=strings.MISSED_DATA)

    return Messages.getLast(user.id, to_id, start, end)
