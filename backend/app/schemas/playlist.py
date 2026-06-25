from pydantic import BaseModel


class PlaylistCreate(BaseModel):
    name: str
class PlaylistItemCreate(BaseModel):
    playlist_id: int
    media_id: int
    sequence: int = 1
