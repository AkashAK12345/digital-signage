from sqlalchemy import Column
from sqlalchemy import Integer
from sqlalchemy import String
from sqlalchemy import DateTime

from datetime import datetime

from app.database.base import Base


class Media(Base):

    __tablename__ = "media"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    filename = Column(
        String,
        nullable=False
    )

    original_name = Column(
        String,
        nullable=False
    )

    media_type = Column(
        String,
        nullable=False
    )

    duration = Column(
        Integer,
        default=3000
    )

    created_at = Column(
        DateTime,
        default=datetime.utcnow
    )