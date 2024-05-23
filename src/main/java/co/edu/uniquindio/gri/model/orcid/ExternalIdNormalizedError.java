package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalIdNormalizedError {
	
	@JsonProperty("error-code")
	public String errorCode;
	@JsonProperty("error-message")
    public String errorMessage;
    @JsonProperty("transient") 
    public boolean mytransient;

}
