package hr.foi.nj3m.events;


public class Listener {
    private String Type;
    IEventHandler Handler;

    public Listener(String type, IEventHandler handler)
    {
        this.Type = type;
        this.Handler = handler;
    }

    public String getType()
    {
        return this.Type;
    }
    public  IEventHandler getHandler()
    {
        return this.Handler;
    }
}
