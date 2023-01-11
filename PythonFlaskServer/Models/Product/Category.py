from __future__ import annotations

from main import db


class Categories(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), nullable=False)

    image_name = db.Column(db.String(255))
    description = db.Column(db.Text)

    @staticmethod
    def get(**kwargs) -> Categories:
        return Categories.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [Categories]:
        return Categories.query.filter_by(**kwargs).all()

    def serialize(self):
        return {
            'name': self.name,
            'description': self.description,
            'image_name': self.image_name
        }
