import os
import shutil
import uuid

from fastapi import UploadFile
from sqlalchemy.orm import Session

from app.models.media import Media


class MediaService:

    MEDIA_FOLDER = "media"

    IMAGE_EXTENSIONS = (
        ".png",
        ".jpg",
        ".jpeg",
        ".webp"
    )

    VIDEO_EXTENSIONS = (
        ".mp4",
        ".webm",
        ".mov"
    )

    @staticmethod
    def upload_media(
        file: UploadFile,
        db: Session
    ):

        extension = os.path.splitext(
            file.filename
        )[1].lower()

        original_name = os.path.splitext(
            file.filename
        )[0]

        safe_name = (
            original_name
            .replace(" ", "_")
            .replace("/", "_")
            .replace("\\", "_")
        )

        unique_filename = (
            f"{safe_name}_{uuid.uuid4().hex[:8]}{extension}"
        )

        file_path = os.path.join(
            MediaService.MEDIA_FOLDER,
            unique_filename
        )

        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(
                file.file,
                buffer
            )

        media_type = (
            "image"
            if extension in MediaService.IMAGE_EXTENSIONS
            else "video"
        )

        media = Media(
            filename=unique_filename,
            original_name=file.filename,
            media_type=media_type
        )

        db.add(media)
        db.commit()
        db.refresh(media)

        return media

    @staticmethod
    def get_all_media(
        db: Session
    ):

        return db.query(Media).all()

    @staticmethod
    def delete_media(
        media_id: int,
        db: Session
    ):

        media = (
            db.query(Media)
            .filter(Media.id == media_id)
            .first()
        )

        if media is None:
            return None

        path = os.path.join(
            MediaService.MEDIA_FOLDER,
            media.filename
        )

        if os.path.exists(path):
            os.remove(path)

        db.delete(media)
        db.commit()

        return media