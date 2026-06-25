from sqlalchemy import Column
from sqlalchemy import Integer
from sqlalchemy import ForeignKey

from app.database.base import Base


class PlaylistItem(Base):

    __tablename__ = "playlist_items"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    playlist_id = Column(
        Integer,
        ForeignKey("playlists.id")
    )

    media_id = Column(
        Integer,
        ForeignKey("media.id")
    )

    sequence = Column(
        Integer,
        default=1
    )