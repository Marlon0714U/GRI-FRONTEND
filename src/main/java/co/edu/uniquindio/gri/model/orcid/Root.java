package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Root {
	@JsonProperty("orcid-identifier")
	public OrcidIdentifier orcidIdentifier;
	public Preferences preferences;
	public History history;
	public Person person;
	@JsonProperty("activities-summary")
	public ActivitiesSummary activitiesSummary;
	public String path;
}
