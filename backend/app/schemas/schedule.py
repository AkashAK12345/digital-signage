from pydantic import BaseModel
from typing import List, Optional

class ScheduleBase(BaseModel):
    name: str
    playlistId: str
    startDate: str
    endDate: str
    startTime: str
    endTime: str
    repeat: str
    priority: str
    status: str

class ScheduleCreate(ScheduleBase):
    deviceIds: List[str]

class ScheduleUpdate(BaseModel):
    name: Optional[str] = None
    playlistId: Optional[str] = None
    startDate: Optional[str] = None
    endDate: Optional[str] = None
    startTime: Optional[str] = None
    endTime: Optional[str] = None
    repeat: Optional[str] = None
    priority: Optional[str] = None
    status: Optional[str] = None
    deviceIds: Optional[List[str]] = None

class ScheduleResponse(ScheduleBase):
    id: str
    deviceIds: List[str] = []
    createdAt: int
    updatedAt: int

    class Config:
        from_attributes = True
