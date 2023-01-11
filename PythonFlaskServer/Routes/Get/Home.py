import os

from flask import Blueprint, send_from_directory

from main import app

home_blueprint = Blueprint('home_my', __name__)

"""
    Returns image file names
"""


@home_blueprint.route('/home/contents/slider', methods=['GET'])
def getSliderImages():
    return dict(status=True, image_names=['slider_1.png', 'slider_2.jpg'])

@home_blueprint.route('/home/contents/stocks', methods=['GET'])
def getStocksImages():
    return dict(status=True, image_names=['stock_1.jpg', 'stock_2.jpg', 'stock_3.jpg', 'stock_4.jpg', 'product_1', 'category_7'])
