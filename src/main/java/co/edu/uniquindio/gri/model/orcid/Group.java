package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Group {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	@JsonProperty("external-ids")
	public ExternalIds externalIds;
	@JsonProperty("work-summary")
	public ArrayList<WorkSummary> workSummary;
	@JsonProperty("funding-summary")
	public ArrayList<FundingSummary> fundingSummary;
}
