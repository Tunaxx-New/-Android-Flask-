from flask import Flask
from dotenv import load_dotenv
from os import getenv
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_socketio import SocketIO

from config import Config

app = Flask(__name__)
app.config.from_object(Config)

db = SQLAlchemy(app)
migrate = Migrate(app, db)
socketio = SocketIO(app)

import Routes.Chat.SocketEmitters

if __name__ == '__main__':
    load_dotenv()

    with app.app_context():
        db.init_app(app)
        migrate.init_app(app, db)

    from Routes.UserData.Login import login_blueprint

    app.register_blueprint(login_blueprint)

    from Routes.UserData.Register import register_blueprint

    app.register_blueprint(register_blueprint)
    from Routes.UserData.Validate import validate_phone_blueprint, validate_email_blueprint

    app.register_blueprint(validate_phone_blueprint)
    app.register_blueprint(validate_email_blueprint)

    from Routes.UserData.UserGet import getUser_blueprint

    app.register_blueprint(getUser_blueprint)

    from Routes.Chat.Get import get_message_blueprint
    app.register_blueprint(get_message_blueprint)

    from Routes.Get.TechSupports import getTechSupports_blueprint
    app.register_blueprint(getTechSupports_blueprint)

    from Routes.UserData.Change import change_blueprint
    app.register_blueprint(change_blueprint)

    from Routes.Product.GetProducts import product_id_blueprint, product_category_blueprint, category_blueprint, category_all_blueprint
    app.register_blueprint(product_id_blueprint)
    app.register_blueprint(product_category_blueprint)
    app.register_blueprint(category_blueprint)
    app.register_blueprint(category_all_blueprint)

    from Routes.Product.Cart import cart_blueprint, cart_items_blueprint, cart_add_blueprint, cart_delete_blueprint, cart_delete_all_blueprint
    app.register_blueprint(cart_blueprint)
    app.register_blueprint(cart_items_blueprint)
    app.register_blueprint(cart_add_blueprint)
    app.register_blueprint(cart_delete_blueprint)
    app.register_blueprint(cart_delete_all_blueprint)

    from Routes.Get.Images import get_image_blueprint
    app.register_blueprint(get_image_blueprint)

    from Routes.Get.Home import home_blueprint
    app.register_blueprint(home_blueprint)

    from Routes.Get.Map import map_blueprint
    app.register_blueprint(map_blueprint)

    socketio.run(app, host=getenv('HOST'), port=getenv('PORT'), debug=getenv('DEBUG'))

from Models.Product.Product import Products
from Models.Product.Category import Categories
from Models.Product.ProductCategory import ProductCategories

from Models.Chat.Message import Messages
#from Models.User.User import Users
from Models.User.UserRole import UserRoles
from Models.User.Role import Roles
from Models.User.Card import Cards

from Models.Product.Order.Cart import Carts
from Models.Product.Order.Order import Orders
from Models.Product.Order.Item import Items
from Models.Product.Order.CartItem import CartItems

from Models.Map.Map import ShopPosition