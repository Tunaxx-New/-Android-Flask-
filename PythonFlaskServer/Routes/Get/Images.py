import os

from flask import Blueprint, send_from_directory

from main import app

get_image_blueprint = Blueprint('get_image', __name__)

"""
    Returns image file, and check jpg <-> png
"""


@get_image_blueprint.route('/image/<name>')
def get_image(name):
    if os.path.exists(app.config['UPLOAD_FOLDER'] + '/' + name):
        return send_from_directory(app.config['UPLOAD_FOLDER'], name, as_attachment=True)
    else:
        split_tup = os.path.splitext(name)
        ext: str
        if split_tup[1] == '.jpg':
            ext = '.png'
        elif split_tup[1] == '.png':
            ext = '.jpg'
        else:
            return dict(status=False)
        return send_from_directory(app.config['UPLOAD_FOLDER'], split_tup[0] + ext, as_attachment=True)
