package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FundingSummary {
	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Source source;
	public Title title;
	@JsonProperty("external-ids")
	public ExternalIds externalIds;
	public Object url;
	public String type;
	@JsonProperty("start-date")
	public StartDate startDate;
	@JsonProperty("end-date")
	public EndDate endDate;
	public Organization organization;
	public String visibility;
	@JsonProperty("put-code")
	public int putCode;
	public String path;
	@JsonProperty("display-index")
	public String displayIndex;

}
