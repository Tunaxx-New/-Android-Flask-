from __future__ import annotations

from main import db


class Products(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), nullable=False)
    description = db.Column(db.Text)

    image_type = db.Column(db.Text)
    image_count = db.Column(db.Integer)

    price = db.Column(db.Float(2), nullable=False)
    currency = db.Column(db.String(32), default='тг', nullable=False)
    unit = db.Column(db.String(32), nullable=False)
    discount = db.Column(db.Float(2), default=0)

    @staticmethod
    def get(**kwargs) -> Products:
        return Products.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [Products]:
        return Products.query.filter_by(**kwargs).all()

    def serialize_small(self):
        return {
            'id': self.id,
            'name': self.name,
            'image_type': self.image_type,
            'price': self.price,
            'currency': self.currency,
            'discount': self.discount,
        }

    def serialize(self):
        return {
            'id': self.id,
            'name': self.name,
            'description': self.description,
            'image_type': self.image_type,
            'image_count': self.image_count,
            'price': self.price,
            'currency': self.currency,
            'discount': self.discount,
            'unit': self.unit
        }
