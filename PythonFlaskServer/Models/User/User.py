from __future__ import annotations
from datetime import datetime

from main import db


class Users(db.Model):
    id = db.Column(db.Integer, primary_key=True)

    name = db.Column(db.String(255))
    surname = db.Column(db.String(255))

    phone = db.Column(db.String(255), nullable=False)
    phone_code = db.Column(db.String(6))
    phone_activated = db.Column(db.Boolean, default=False)

    email = db.Column(db.String(255), nullable=False)
    email_code = db.Column(db.String(6))
    email_activated = db.Column(db.Boolean, default=False)

    password = db.Column(db.String(255), nullable=False)
    hash = db.Column(db.String(255), nullable=False)
    registered = db.Column(db.TIMESTAMP, nullable=False, default=datetime.now)

    @staticmethod
    def get(**kwargs) -> Users:
        return Users.query.filter_by(**kwargs).first()

    @staticmethod
    def getAll(**kwargs) -> [Users]:
        return Users.query.filter_by(**kwargs).all()

    def serialize(self):
        return {
            'id': self.id,
            'username': self.username,
            'password': self.password,
            'email': self.email,
            'is_active': self.is_active,
            'registered': self.registered
        }