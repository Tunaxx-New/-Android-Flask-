from __future__ import annotations
from datetime import datetime

from main import db


class ShopPosition(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    shop_id = db.Column(db.Integer)
    meridian = db.Column(db.REAL)
    longitude = db.Column(db.REAL)

    @staticmethod
    def getAll(**kwargs) -> [ShopPosition]:
        return ShopPosition.query.filter_by(**kwargs).all()

    def serialize(self):
        return {
            'id': self.id,
            'shop_id': self.shop_id,
            'meridian': self.meridian,
            'longitude': self.longitude
        }