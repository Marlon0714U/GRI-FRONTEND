package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceSummary {
	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Source source;
	@JsonProperty("put-code")
	public int putCode;
	@JsonProperty("department-name")
	public String departmentName;
	@JsonProperty("role-title")
	public String roleTitle;
	@JsonProperty("start-date")
	public StartDate startDate;
	@JsonProperty("end-date")
	public EndDate endDate;
	public Organization organization;
	public Url url;
	@JsonProperty("external-ids")
	public ExternalIds externalIds;
	@JsonProperty("display-index")
	public String displayIndex;
	public String visibility;
	public String path;
}
