package worldOfZuul;

public enum Commands
{
    GO("sejl"), QUIT("quit"), HELP("hjælp"), UNKNOWN("?"),
    DISPOSE("genbrug"), COLLECT("indsaml"), INFO("info"),
    COMPASS("kompas");

    
    private String commandName;
    
    Commands(String commandString)
    {
        this.commandName = commandString;
    }
    
    public String toString()
    {
        return commandName;
    }
}
