from __future__ import annotations
from datetime import datetime

from main import db


class Orders(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    cart_id = db.Column(db.Integer, nullable=False)
    created = db.Column(db.TIMESTAMP, nullable=False, default=datetime.now)

    @staticmethod
    def get(**kwargs) -> Orders:
        return Orders.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [Orders]:
        return Orders.query.filter_by(**kwargs).all()

    def serialize(self):
        return {
            'id': self.id,
            'cart_id': self.user_id,
            'created': self.created
        }
