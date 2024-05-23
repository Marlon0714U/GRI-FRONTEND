package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Name {
	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	@JsonProperty("given-names")
	public GivenNames givenNames;
	@JsonProperty("family-name")
	public FamilyName familyName;
	@JsonProperty("credit-name")
	public CreditName creditName;
	public String source;
	public String visibility;
	public String path;
}
