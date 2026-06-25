from fastapi import APIRouter
from fastapi import Depends
from fastapi import HTTPException

from sqlalchemy.orm import Session

from app.database.database import get_db

from app.schemas.device import (
    DeviceCreate,
    HeartbeatRequest
)

from app.services.device_service import DeviceService

router = APIRouter(
    prefix="/devices",
    tags=["Devices"]
)


@router.post("/register")
def register_device(
    payload: DeviceCreate,
    db: Session = Depends(get_db)
):

    device = DeviceService.register_device(
        payload,
        db
    )

    return {
        "message": "Device Registered",
        "device_id": device.device_id,
        "device_name": device.device_name
    }


@router.post("/heartbeat")
def heartbeat(
    payload: HeartbeatRequest,
    db: Session = Depends(get_db)
):

    device = DeviceService.heartbeat(
        payload.device_id,
        db
    )

    if device is None:
        raise HTTPException(
            status_code=404,
            detail="Device not found"
        )

    return {
        "message": "Heartbeat received"
    }


@router.get("/")
def get_all_devices(
    db: Session = Depends(get_db)
):

    return DeviceService.get_all_devices(db)


@router.get("/{device_id}")
def get_device(
    device_id: str,
    db: Session = Depends(get_db)
):

    device = DeviceService.get_device(
        device_id,
        db
    )

    if device is None:
        raise HTTPException(
            status_code=404,
            detail="Device not found"
        )

    return device