from __future__ import annotations

from main import db


class CartItems(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    cart_id = db.Column(db.Integer, nullable=False)
    item_id = db.Column(db.Integer, nullable=False)

    @staticmethod
    def getAll(**kwargs) -> [CartItems]:
        return CartItems.query.filter_by(**kwargs).all()

    def serialize(self):
        return {
            'cart_id': self.cart_id,
            'item_id': self.item_id
        }
