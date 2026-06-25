from sqlalchemy import Column
from sqlalchemy import Integer
from sqlalchemy import String
from sqlalchemy import ForeignKey

from app.database.base import Base


class DevicePlaylist(Base):

    __tablename__ = "device_playlists"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    device_id = Column(
        String,
        ForeignKey("devices.device_id"),
        nullable=False
    )

    playlist_id = Column(
        Integer,
        ForeignKey("playlists.id"),
        nullable=False
    )