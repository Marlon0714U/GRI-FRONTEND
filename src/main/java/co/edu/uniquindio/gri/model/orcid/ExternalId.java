package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ExternalId {
	@JsonProperty("external-id-type")
	public String externalIdType;
	@JsonProperty("external-id-value")
	public String externalIdValue;
	@JsonProperty("external-id-normalized")
	public ExternalIdNormalized externalIdNormalized;
	@JsonProperty("external-id-normalized-error")
	public ExternalIdNormalizedError externalIdNormalizedError;
	@JsonProperty("external-id-url")
	public ExternalIdUrl externalIdUrl;
	@JsonProperty("external-id-relationship")
	public String externalIdRelationship;
	
}
