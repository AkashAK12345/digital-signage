from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.database.database import get_db
from app.schemas.device import DeviceCreate, DeviceUpdate, DeviceResponse, HeartbeatRequest, DeviceStatusResponse
from app.services.device_service import DeviceService

router = APIRouter(
    prefix="/devices",
    tags=["Devices"]
)

@router.get("", response_model=List[DeviceResponse])
def get_devices(db: Session = Depends(get_db)):
    return DeviceService.get_devices(db)

@router.post("", response_model=DeviceResponse, status_code=status.HTTP_201_CREATED)
def create_device(payload: DeviceCreate, db: Session = Depends(get_db)):
    return DeviceService.create_device(db, payload)

@router.post("/heartbeat", response_model=DeviceResponse)
def process_heartbeat(payload: HeartbeatRequest, db: Session = Depends(get_db)):
    device = DeviceService.process_heartbeat(db, payload)
    if not device:
        raise HTTPException(status_code=404, detail="Device not found")
    return device

@router.get("/{device_id}/status", response_model=DeviceStatusResponse)
def get_device_status(device_id: str, db: Session = Depends(get_db)):
    status_response = DeviceService.get_device_status(db, device_id)
    if not status_response:
        raise HTTPException(status_code=404, detail="Device not found")
    return status_response

@router.put("/{device_id}", response_model=DeviceResponse)
def update_device(device_id: str, payload: DeviceUpdate, db: Session = Depends(get_db)):
    device = DeviceService.update_device(db, device_id, payload)
    if not device:
        raise HTTPException(status_code=404, detail="Device not found")
    return device

@router.delete("/{device_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_device(device_id: str, db: Session = Depends(get_db)):
    success = DeviceService.delete_device(db, device_id)
    if not success:
        raise HTTPException(status_code=404, detail="Device not found")
    return None