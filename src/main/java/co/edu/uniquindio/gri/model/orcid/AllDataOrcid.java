package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AllDataOrcid {
	
	@JsonProperty("activities-summary")
	public ActivitiesSummary activitiesSummary;
	public History history;
	@JsonProperty("orcid-identifier")
	public OrcidIdentifier orcidIdentifier;
	public String path;
	public Person person;
	public Preferences preferences;
	public long id;
	

}
