package hr.foi.nj3m.interfaces;

import java.util.List;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public interface IMessageACK {
    void GetMessage (String listOfMessage, int numberOfMessages);
    String ReturnFinalMessage();

}
