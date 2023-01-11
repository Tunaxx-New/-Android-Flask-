from os import getenv

from urllib.parse import quote_plus


user = getenv('DATABASE_USER')
password = getenv('DATABASE_PASSWORD')
host = getenv('DATABASE_HOST')
name = getenv('DATABASE_NAME')


class Config:
    SQLALCHEMY_DATABASE_URI = f'postgresql://{user}:{password}@{host}/{name}'
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_MAX_OVERFLOW = 200
    SQLALCHEMY_POOL_SIZE = 2
    UPLOAD_FOLDER = './images'
    SQLALCHEMY_POOL_TIMEOUT = 1000
    SECRET_KEY = getenv('SECRET_KEY')