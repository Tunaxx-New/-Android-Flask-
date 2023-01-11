from flask import request
from flask_socketio import emit

from Routes.Chat.Send import sendMessage
from Routes.UserData.Wrapper.LoginRequiredSocket import login_required_s
from __main__ import socketio

clients = dict()

@socketio.on('connect')
def connect():
    print(request.sid + " connected")
    emit('connect', 'ok')

@socketio.on('disconnect')
def lol():
    print(request.sid + " disconnected")

@socketio.on('join')
def test_connect(uhash):
    user = login_required_s(uhash)
    clients[user.id] = request.sid
    print(clients.values(), " joined")


@socketio.on('leave')
def test_connect(uhash):
    user = login_required_s(uhash)
    if clients.get(user.id):
        del clients[user.id]
    print(clients.values(), " left")


#
# Sends data = (message, to_id, from_id, date_time)
#
@socketio.on('message')
def message(uhash, msg, to_id):
    user = login_required_s(uhash)
    user_id = user.id

    date = sendMessage(user, msg, to_id)

    if clients.get(to_id):
        socketio.emit("message", data=(msg, user_id, str(date)), to=clients.get(to_id), room=clients.get(to_id))
    socketio.emit("message", data=(msg, user_id, str(date)), to=clients.get(user_id), room=clients.get(user_id))