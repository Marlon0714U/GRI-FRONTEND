package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ExternalIdentifiers {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	@JsonProperty("external-identifier")
	public ArrayList<ExternalIdentifier> externalIdentifier;
	public String path;
}
