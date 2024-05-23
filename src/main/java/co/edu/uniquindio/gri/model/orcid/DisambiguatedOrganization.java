package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class DisambiguatedOrganization {
	@JsonProperty("disambiguated-organization-identifier")
	public String disambiguatedOrganizationIdentifier;
	@JsonProperty("disambiguation-source")
	public String disambiguationSource;
}
