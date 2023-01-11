from flask import request

from flask import Blueprint

from Models.Product.Order.Cart import Carts
from Models.User.User import Users
from Models.User.Card import Cards

from main import db
from utils import strings
from utils.hash import getHash
from utils.hash import generateCode
from utils.valid import isPhoneValid
from utils.valid import isEmailValid

register_blueprint = Blueprint('register', __name__)


@register_blueprint.route('/register', methods=['POST'])
def register():
    phone = request.form.get('phone', None)
    password = request.form.get('password', None)
    email = request.form.get('email', None)

    if None in [phone, password, email]:
        return dict(status=False, reason=strings.MISSED_DATA)

    if not isPhoneValid(phone):
        return dict(status=False, reason=strings.WRONG_PHONE_NUMBER)

    if not isEmailValid(email):
        return dict(status=False, reason=strings.WRONG_EMAIL)

    existUser = Users.get(phone=phone)
    if existUser is not None:
        if existUser.phone_activated:
            return dict(status=False, reason=strings.USER_PHONE_EXIST)

    existUser = Users.get(email=email)
    if existUser is not None:
        if existUser.email_activated:
            return dict(status=False, reason=strings.USER_EMAIL_EXIST)

    phoneCode = generateCode()
    emailCode = generateCode()

    # Отправить код на email
    # Отправить код по sms

    hash = getHash()
    user = Users(phone=phone, password=password, email=email, hash=hash, phone_code=phoneCode, email_code=emailCode)
    db.session.add(user)
    db.session.commit()

    # Создаем бонусную карту
    card = Cards(user_id=user.id)
    db.session.add(card)
    # Создаем корзину пользователю
    cart = Carts(user_id=user.id)
    db.session.add(cart)

    db.session.commit()

    return dict(status=True, hash=hash)
