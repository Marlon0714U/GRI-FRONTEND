package co.edu.uniquindio.gri.model.orcid; 
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class PublicationDate{
    public Year year;
    public Object month;
    public Object day;
}
