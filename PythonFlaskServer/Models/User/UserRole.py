from __future__ import annotations
from datetime import datetime

from main import db


class UserRoles(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, nullable=False)
    role_id = db.Column(db.Integer, nullable=False)

    @staticmethod
    def get(**kwargs) -> UserRoles:
        return UserRoles.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [UserRoles]:
        return UserRoles.query.filter_by(**kwargs).all()