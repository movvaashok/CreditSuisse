package com.creditsuisse.loganalysis.beans;

public class EventLogBean {

	private String eventId;
	private int eventDuration;
	private String type;
	private String host;
	boolean alert;

	public EventLogBean() {
	}

	public EventLogBean(String eventId, int eventDuration, String type, String host, boolean alert) {
		this.eventId = eventId;
		this.eventDuration = eventDuration;
		this.type = type;
		this.host = host;
		this.alert = alert;
	}

	public String getEvenId() {
		return eventId;
	}

	public void setEvenId(String eventId) {
		this.eventId = eventId;
	}

	public int getEventDuration() {
		return eventDuration;
	}

	public void setEventDuration(int eventDuration) {
		this.eventDuration = eventDuration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}
	
	public String toString() {
		System.out.println(eventId+" "+eventDuration+" "+type+" "+host);
		return "";
	}

}
