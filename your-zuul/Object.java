
/**
 * Beschreiben Sie hier die Klasse Object.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Object
{
    int objectNR;
    String objectDescription;
    int objectWeight;
    
    public Object(String objectDescription, int objectWeight)
    {
        this.objectDescription = objectDescription;
        this.objectWeight = objectWeight;
    }
    public int getWeight()
    {
        return objectWeight;
    }
    public String getDescription()
    {
        return objectDescription;
    }
}
