import uuid
from datetime import datetime

from sqlalchemy.orm import Session

from app.models.device import Device
from app.schemas.device import DeviceCreate


class DeviceService:

    @staticmethod
    def register_device(
        payload: DeviceCreate,
        db: Session
    ):

        device = Device(
            device_id=str(uuid.uuid4()),
            device_name=payload.device_name,
            status="online"
        )

        db.add(device)
        db.commit()
        db.refresh(device)

        return device

    @staticmethod
    def heartbeat(
        device_id: str,
        db: Session
    ):

        device = (
            db.query(Device)
            .filter(Device.device_id == device_id)
            .first()
        )

        if device is None:
            return None

        device.status = "online"
        device.last_seen = datetime.utcnow()

        db.commit()
        db.refresh(device)

        return device

    @staticmethod
    def get_all_devices(
        db: Session
    ):

        return db.query(Device).all()

    @staticmethod
    def get_device(
        device_id: str,
        db: Session
    ):

        return (
            db.query(Device)
            .filter(Device.device_id == device_id)
            .first()
        )