from __future__ import annotations

from main import db


class ProductCategories(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    product_id = db.Column(db.Integer, nullable=False)
    category_id = db.Column(db.Integer, nullable=False)

    @staticmethod
    def get(**kwargs) -> ProductCategories:
        return ProductCategories.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [ProductCategories]:
        return ProductCategories.query.filter_by(**kwargs).all()
