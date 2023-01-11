from __future__ import annotations
from datetime import datetime

from main import db


class Cards(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, unique=True)
    points = db.Column(db.Integer, default=2500)
    registered = db.Column(db.TIMESTAMP, nullable=False, default=datetime.now)

    @staticmethod
    def get(**kwargs) -> Cards:
        return Cards.query.filter_by(**kwargs).first()

    def serialize(self):
        return {
            'id': self.id,
            'user_id': self.user_id,
            'points': self.points,
            'registered': self.registered
        }