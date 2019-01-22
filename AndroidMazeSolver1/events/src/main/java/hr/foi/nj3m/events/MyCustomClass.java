package hr.foi.nj3m.events;

public class MyCustomClass extends EventDispatcher{
    private static final MyCustomClass ourInstance = new MyCustomClass();

    public static MyCustomClass getInstance() {
        return ourInstance;
    }

    private MyCustomClass() {
    }
    public void myCallback(){
        Event event=new Event(Event.COMPLETE);
        dispatchEvent(event);


    }
}
