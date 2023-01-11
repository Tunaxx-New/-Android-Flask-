from flask import Blueprint, request

from Models.Product.Order.Cart import Carts
from Models.Product.Order.CartItem import CartItems
from Models.Product.Order.Item import Items
from Models.Product.Product import Products
from Routes.UserData.Wrapper.LoginRequired import login_required
from main import db
from utils.strings import CART_NOT_FOUND, MISSED_DATA

cart_blueprint = Blueprint('cart', __name__)
cart_items_blueprint = Blueprint('cart_items', __name__)
cart_add_blueprint = Blueprint('cart_add', __name__)
cart_delete_blueprint = Blueprint('cart_delete', __name__)
cart_delete_all_blueprint = Blueprint('cart_delete_all', __name__)


@cart_blueprint.route('/cart', methods=['POST'])
@login_required
def getCart(user):
    cart = Carts.get(user_id=user.id)
    if cart is None:
        return dict(status=False, reason=CART_NOT_FOUND)
    return dict(status=True, cart=cart.serialize())


@cart_items_blueprint.route('/cart/items', methods=['POST'])
@login_required
def getItems(user):
    cart = Carts.get(user_id=user.id)
    if cart is None:
        return dict(status=False, reason=CART_NOT_FOUND)

    cart_items = CartItems.getAll(cart_id=cart.id)
    ids = []
    product_ids = []
    counts = []
    for cart_item in cart_items:
        item = Items.get(id=cart_item.item_id, isFinished=False)
        if item is not None:
            ids.append(item.id)
            product_ids.append(item.product_id)
            counts.append(item.count)

    return dict(status=True, ids=ids, product_ids=product_ids, counts=counts)


@cart_add_blueprint.route('/cart/add', methods=['POST'])
@login_required
def add(user):
    cart = Carts.get(user_id=user.id)
    if cart is None:
        return dict(status=False, reason=CART_NOT_FOUND)

    product_id = request.form.get('product_id', None)
    count = request.form.get('count', None)

    if None in [product_id, count]:
        return dict(status=False, reason=MISSED_DATA)
    try:
        int(product_id)
        int(count)
    except ValueError:
        return dict(status=False)

    product = Products.get(id=product_id)
    if product is None or int(count) <= 0:
        return dict(status=False, reason=MISSED_DATA)

    item = Items(product_id=product_id, count=count)
    db.session.add(item)
    db.session.commit()
    cart_item = CartItems(cart_id=cart.id, item_id=item.id)
    db.session.add(cart_item)
    db.session.commit()

    return dict(status=True)


@cart_delete_blueprint.route('/cart/delete', methods=['POST'])
@login_required
def delete(user):
    cart = Carts.get(user_id=user.id)
    if cart is None:
        return dict(status=False, reason=CART_NOT_FOUND)

    item_id = request.form.get('item_id', None)

    if item_id is None:
        return dict(status=False, reason=MISSED_DATA)
    try:
        int(item_id)
    except ValueError:
        return dict(status=False)

    Items.query.filter_by(id=item_id, isFinished=False).delete()
    CartItems.query.filter_by(item_id=item_id).delete()
    db.session.commit()

    return dict(status=True)


@cart_delete_all_blueprint.route('/cart/delete_all', methods=['POST'])
@login_required
def delete_all(user):
    cart = Carts.get(user_id=user.id)
    if cart is None:
        return dict(status=False, reason=CART_NOT_FOUND)

    cart_items = CartItems.getAll(cart_id=cart.id)
    for cart_item in cart_items:
        item_id = cart_item.item_id
        Items.query.filter_by(id=item_id, isFinished=False).delete()

    CartItems.query.filter_by(cart_id=cart.id).delete()
    db.session.commit()

    return dict(status=True)