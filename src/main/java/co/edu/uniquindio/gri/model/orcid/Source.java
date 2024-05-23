package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Source {
	@JsonProperty("source-orcid")
	public SourceOrcid sourceOrcid;
	@JsonProperty("source-client-id")
	public SourceClientId sourceClientId;
	@JsonProperty("source-name")
	public SourceName sourceName;
	@JsonProperty("assertion-origin-orcid")
	public AssertionOriginOrcid assertionOriginOrcid;
	@JsonProperty("assertion-origin-client-id")
	public String assertionOriginClientId;
	@JsonProperty("assertion-origin-name")
	public AssertionOriginName assertionOriginName;
}
