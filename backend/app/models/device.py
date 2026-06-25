from sqlalchemy import Column
from sqlalchemy import Integer
from sqlalchemy import String
from sqlalchemy import DateTime

from datetime import datetime

from app.database.base import Base


class Device(Base):

    __tablename__ = "devices"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    device_id = Column(
        String,
        unique=True,
        nullable=False
    )

    device_name = Column(
        String,
        nullable=False
    )

    status = Column(
        String,
        default="offline"
    )

    last_seen = Column(
        DateTime,
        default=datetime.utcnow
    )

    created_at = Column(
        DateTime,
        default=datetime.utcnow
    )