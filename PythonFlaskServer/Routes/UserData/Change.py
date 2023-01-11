from flask import Blueprint, request

from Routes.UserData.Wrapper.LoginRequired import login_required
from Routes.UserData.Wrapper.NewHash import new_hash
from main import db
from utils.hash import generateCode

change_blueprint = Blueprint('change', __name__)


@change_blueprint.route('/change', methods=['POST'])
@login_required
def change(user):
    new_phone = request.form.get('new_phone', None)
    if new_phone is not None:
        user.phone = new_phone
        user.phone_activated = False

        phoneCode = generateCode()
        user.phoneCode = phoneCode

    new_email = request.form.get('new_email', None)
    if new_email is not None:
        user.email = new_email
        user.email_activated = False

        emailCode = generateCode()
        user.emailCode = emailCode

    # Отправить код на email
    # Отправить код по sms

    new_name = request.form.get('new_name', None)
    if new_name is not None:
        user.name = new_name

    new_surname = request.form.get('new_surname', None)
    if new_surname is not None:
        user.surname = new_surname

    db.session.commit()
    return dict(status=True)
