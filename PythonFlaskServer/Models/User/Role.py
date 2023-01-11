from __future__ import annotations
from datetime import datetime

from main import db


class Roles(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    role = db.Column(db.String(255), nullable=False)

    @staticmethod
    def get(**kwargs) -> [Roles]:
        return Roles.query.filter_by(**kwargs).first()