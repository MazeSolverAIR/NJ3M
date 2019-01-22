package hr.foi.nj3m.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventDispatcher implements IEventDispatcher {

    public ArrayList<Listener> ListenerList = new ArrayList<>();

    @Override
    public void addEvenetListener(String str, IEventHandler handler) {
        Listener listener = new Listener(str, handler);
        ListenerList.add(listener);
    }

    @Override
    public void removeEventListener(String str) {
        for(Listener listener: ListenerList)
        {
            if(listener.getType().equals(str))
                ListenerList.remove(listener);
        }
    }

    @Override
    public void dispatchEvent(Event event) {
        for(Listener listener:ListenerList)
        {
            if(event.getEventType() == listener.getType())
            {
                IEventHandler eventHandler = listener.getHandler();
                eventHandler.callback(event);
            }
        }
        MyCustomClass nekaj=MyCustomClass.getInstance();
        nekaj.addEvenetListener("nekaj", new IEventHandler() {
            @Override
            public void callback(Event event) {

            }
        });
    }

    @Override
    public boolean hasEventListener(String str) {
        return false;
    }

    @Override
    public void removeAllListeners() {

    }
}
