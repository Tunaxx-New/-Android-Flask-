from __future__ import annotations

import os
import sys
from dataclasses import dataclass
from datetime import datetime

from config import Config
from main import db


class Messages(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    created = db.Column(db.TIMESTAMP, nullable=False, default=datetime.now)
    from_id = db.Column(db.Integer, nullable=False)
    to_id = db.Column(db.Integer, nullable=False)
    data = db.Column(db.Text, default="")

    @staticmethod
    def get(**kwargs) -> [Messages]:
        return Messages.query.filter_by(**kwargs)

    @staticmethod
    def getToTech(tech_id: int):
        try:
            int(tech_id)
        except ValueError:
            return []

        query_file = open(os.path.join(sys.path[0], "Routes/Chat/getDistinctTechMessages.sql"), "r")
        query = query_file.read()
        query = query % tech_id
        query_file.close()

        cursor = db.session.execute(query)

        @dataclass
        class id:
            user_id: int
        ids = []
        for row in cursor:
            id_ = id(0)
            id_.user_id = row[0]
            ids.append(id_)
        db.session.commit()
        return ids

    @staticmethod
    def getLast(user_id, to_id, start, end) -> [Messages]:
        try:
            int(to_id)
            int(start)
            int(end)
        except ValueError:
            return dict(status=False)
        count = int(end) - int(start)
        if count > 100 or count < 0:
            return dict(status=False)

        query_file = open(os.path.join(sys.path[0], "Routes/Chat/getMessages.sql"), "r")
        query = query_file.read()
        query = query % (user_id, to_id, start, end)
        query_file.close()

        dates = []
        from_ids = []
        to_ids = []
        datas = []

        cursor = db.session.execute(query)
        for row in cursor:
            dates.append(row[0])
            from_ids.append(row[1])
            to_ids.append(row[2])
            datas.append(row[3])
        db.session.commit()

        return dict(
            status=True,
            froms=from_ids,
            messages=datas,
            dates=dates,
            user_id=user_id
            )