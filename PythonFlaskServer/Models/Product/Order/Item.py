from __future__ import annotations

from main import db


class Items(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    order_id = db.Column(db.Integer, nullable=True)
    product_id = db.Column(db.Integer, nullable=False)
    count = db.Column(db.Integer, nullable=False)
    isFinished = db.Column(db.Boolean, default=False, nullable=False)

    @staticmethod
    def get(**kwargs) -> Items:
        return Items.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [Items]:
        return Items.query.filter_by(**kwargs).all()

    def serialize(self):
        return {
            'id': self.id,
            'order_id': self.order_id,
            'product_id': self.product_id,
            'count': self.count,
            'isFinished': self.isFinished
        }
