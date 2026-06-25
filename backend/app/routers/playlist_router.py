from fastapi import APIRouter
from fastapi import Depends

from sqlalchemy.orm import Session

from app.database.database import get_db
from app.models.playlist import Playlist
from app.schemas.playlist import PlaylistCreate
from app.models.playlist_item import PlaylistItem
from app.schemas.playlist import PlaylistItemCreate
from app.models.media import Media

router = APIRouter(
    prefix="/playlists",
    tags=["Playlists"]
)


@router.post("/")
def create_playlist(
    payload: PlaylistCreate,
    db: Session = Depends(get_db)
):

    playlist = Playlist(
        name=payload.name
    )

    db.add(playlist)
    db.commit()
    db.refresh(playlist)

    return {
        "id": playlist.id,
        "name": playlist.name
    }


@router.get("/")
def get_playlists(
    db: Session = Depends(get_db)
):

    playlists = db.query(
        Playlist
    ).all()

    return playlists

@router.post("/add-media")
def add_media_to_playlist(
    payload: PlaylistItemCreate,
    db: Session = Depends(get_db)
):

    item = PlaylistItem(
        playlist_id=payload.playlist_id,
        media_id=payload.media_id,
        sequence=payload.sequence
    )

    db.add(item)
    db.commit()
    db.refresh(item)

    return {
        "message": "Media added to playlist",
        "id": item.id
    }
@router.get("/{playlist_id}")
def get_playlist_details(
    playlist_id: int,
    db: Session = Depends(get_db)
):

    items = (
        db.query(
            PlaylistItem,
            Media
        )
        .join(
            Media,
            PlaylistItem.media_id == Media.id
        )
        .filter(
            PlaylistItem.playlist_id == playlist_id
        )
        .order_by(
            PlaylistItem.sequence
        )
        .all()
    )

    result = []

    for item, media in items:

        result.append({
            "media_id": media.id,
            "filename": media.filename,
            "media_type": media.media_type,
            "sequence": item.sequence
        })

    return result