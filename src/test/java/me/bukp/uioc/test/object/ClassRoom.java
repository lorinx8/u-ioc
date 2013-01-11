package me.bukp.uioc.test.object;

public class ClassRoom {
	
	private String roomId;
	
	private Teacher teacher;
	
	public ClassRoom() {
		
	}
	
	public ClassRoom(String roomId, Teacher teacher) {
		this.roomId = roomId;
		this.teacher = teacher;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}
