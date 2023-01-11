from flask import Blueprint

from Models.Map.Map import ShopPosition

map_blueprint = Blueprint('mapBlueprint', __name__)


@map_blueprint.route('/getShopPositions', methods=['GET'])
def getShopPositions():
    shop_positions = ShopPosition.getAll()

    l, m = [], []

    for position in shop_positions:
        l.append(position.longitude)
        m.append(position.meridian)

    return dict(status=True, magnitudes=m, longitudes=l)