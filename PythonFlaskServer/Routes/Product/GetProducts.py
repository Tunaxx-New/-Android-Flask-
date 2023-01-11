from flask import Blueprint

from Models.Product.Category import Categories
from Models.Product.Product import Products
from Models.Product.ProductCategory import ProductCategories
from main import db
from utils import strings

product_id_blueprint = Blueprint('product_id', __name__)
product_category_blueprint = Blueprint('product_category', __name__)
category_blueprint = Blueprint('category', __name__)
category_all_blueprint = Blueprint('category_all', __name__)


@product_id_blueprint.route('/products/id/<id>', methods=['GET'])
def getProduct(id):
    product = Products.get(id=id)
    if product is None:
        return dict(status=False, reason=strings.PRODUCT_NOT_FOUND)
    response: dict = product.serialize_small()
    response.update(dict(status=True))
    return response


@product_id_blueprint.route('/products/id/extended/<id>', methods=['GET'])
def getProductExtended(id):
    product = Products.get(id=id)
    if product is None:
        return dict(status=False, reason=strings.PRODUCT_NOT_FOUND)
    response: dict = product.serialize()
    response.update(dict(status=True))
    return response


@product_category_blueprint.route('/products/category/<category_id>', methods=['GET'])
def getAllProduct(category_id):
    product_categories = ProductCategories.getAll(category_id=category_id)
    ids = []
    for product_category in product_categories:
        product = Products.get(id=product_category.product_id)
        ids.append(product.id)
    if len(ids) == 0:
        return dict(status=False, ids=ids)
    return dict(status=True, ids=ids)


@category_blueprint.route('/category/<id>', methods=['GET'])
def getCategory(id):
    category = Categories.get(id=id)
    if category is None:
        return dict(status=False, reason=strings.PRODUCT_NOT_FOUND)
    response: dict = category.serialize()
    response.update(status=True)
    return response

@category_all_blueprint.route('/category/all', methods=['GET'])
def getAllCategory():
    categories = Categories.getAll()
    ids = []
    for category in categories:
        ids.append(category.id)
    return dict(status=True, ids=ids)
