package hr.foi.nj3m.events;

public interface IEventDispatcher {
    void addEvenetListener(String str, IEventHandler handler);
    void removeEventListener(String str);
    void dispatchEvent(Event event);
    boolean hasEventListener(String str);
    void removeAllListeners();
}
