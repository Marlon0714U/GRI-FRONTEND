package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResearcherUrl {
	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Source source;
	@JsonProperty("url-name")
	public Object urlName;
	public Url url;
	public String visibility;
	public String path;
	@JsonProperty("put-code")
	public int putCode;
	@JsonProperty("display-index")
	public int displayIndex;

}
