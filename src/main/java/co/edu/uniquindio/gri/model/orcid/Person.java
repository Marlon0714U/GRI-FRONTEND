																																																																																																																																																																																																																																													package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Person {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Name name;
	@JsonProperty("other-names")
	public OtherNames otherNames;
	public Biography biography;
	@JsonProperty("researcher-urls")
	public ResearcherUrls researcherUrls;
	public Emails emails;
	public Addresses addresses;
	public Keywords keywords;
	@JsonProperty("external-identifiers")
	public ExternalIdentifiers externalIdentifiers;
	public String path;
}
