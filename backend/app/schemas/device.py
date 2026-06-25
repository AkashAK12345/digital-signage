# app/schemas/device.py

from pydantic import BaseModel

class DeviceCreate(BaseModel):
    device_name: str

class HeartbeatRequest(BaseModel):
    device_id: str