from __future__ import annotations

from main import db


class Carts(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, nullable=False)

    @staticmethod
    def get(**kwargs) -> Carts:
        return Carts.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [Carts]:
        return Carts.query.filter_by(**kwargs).all()

    def serialize(self):
        return {
            'id': self.id,
            'user_id': self.user_id
        }
