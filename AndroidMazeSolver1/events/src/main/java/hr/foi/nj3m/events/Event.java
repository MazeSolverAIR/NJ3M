package hr.foi.nj3m.events;

public class Event implements IEvent {

    String EventType = "";
    public static String COMPLETE="complete";
    public Event(String eventType)
    {
        this.EventType = eventType;
    }

    @Override
    public String getEventType() {
        return null;
    }
}
