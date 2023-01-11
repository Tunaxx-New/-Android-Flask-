from flask import Blueprint

from Routes.UserData.Wrapper.LoginRequired import login_required
from Routes.UserData.Wrapper.NewHash import new_hash

login_blueprint = Blueprint('login', __name__)


@login_blueprint.route('/login', methods=['POST'])
@login_required
@new_hash
def login(user, uhash):
    return dict(
        status=True,
        hash=uhash
    )
