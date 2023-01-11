from flask import request

from flask import Blueprint
from Models.User.User import Users
from Routes.UserData.Wrapper.LoginRequired import login_required

from main import db
from utils import strings

validate_phone_blueprint = Blueprint('validate_phone', __name__)

@validate_phone_blueprint.route('/validate_phone', methods=['POST'])
@login_required
def validate_phone(user):
    code = request.form.get('code', None)

    if code is None:
        return dict(status=False, reason=strings.MISSED_DATA)

    if user.phone_code == code:
        if Users.get(phone=user.phone, phone_activated=True) is not None:
            return dict(status=False, reason=strings.ACTIVATED_PHONE)

        user.phone_activated = True
        db.session.commit()

        anotherUsers = Users.getAll(phone=user.phone, phone_activated=False)
        for au in anotherUsers:
            au.phone = ''

        db.session.commit()
        return dict(status=True)
    else:
        return dict(status=False, reason=strings.WRONG_CODE)


validate_email_blueprint = Blueprint('validate_email', __name__)

@validate_email_blueprint.route('/validate_email', methods=['POST'])
@login_required
def validate_email(user):
    code = request.form.get('code', None)

    if code is None:
        return dict(status=False, reason=strings.MISSED_DATA)

    if user.email_code == code:
        if Users.get(email=user.email, email_activated=True) is not None:
            return dict(status=False, reason=strings.ACTIVATED_EMAIL)

        user.email_activated = True
        db.session.commit()

        anotherUsers = Users.getAll(email=user.email, email_activated=False)
        for au in anotherUsers:
            au.email = ''

        db.session.commit()
        return dict(status=True)
    else:
        return dict(status=False, reason=strings.WRONG_CODE)
