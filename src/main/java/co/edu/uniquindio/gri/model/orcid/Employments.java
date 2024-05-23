package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Employments {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	@JsonProperty("affiliation-group")
	public ArrayList<AffiliationGroup> affiliationGroup;
	public String path;
}
