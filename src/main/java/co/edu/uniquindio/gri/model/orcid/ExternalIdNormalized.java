package co.edu.uniquindio.gri.model.orcid; 
import com.fasterxml.jackson.annotation.JsonProperty; 

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ExternalIdNormalized{
    public String value;
    @JsonProperty("transient") 
    public boolean mytransient;
}
