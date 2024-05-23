package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Organization {
	public String name;
	public Address address;
	@JsonProperty("disambiguated-organization")
	public DisambiguatedOrganization disambiguatedOrganization;
}
