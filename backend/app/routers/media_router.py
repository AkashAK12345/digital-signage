from fastapi import APIRouter
from fastapi import Depends
from fastapi import UploadFile
from fastapi import File
from fastapi import HTTPException

from sqlalchemy.orm import Session

from app.database.database import get_db
from app.services.media_service import MediaService

router = APIRouter(
    prefix="/media",
    tags=["Media"]
)


@router.post("/upload")
def upload_media(
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):

    media = MediaService.upload_media(
        file,
        db
    )

    return {
        "id": media.id,
        "filename": media.filename
    }


@router.get("/")
def get_media(
    db: Session = Depends(get_db)
):

    return MediaService.get_all_media(db)


@router.delete("/{media_id}")
def delete_media(
    media_id: int,
    db: Session = Depends(get_db)
):

    media = MediaService.delete_media(
        media_id,
        db
    )

    if media is None:
        raise HTTPException(
            status_code=404,
            detail="Media not found"
        )

    return {
        "message": "Deleted successfully"
    }