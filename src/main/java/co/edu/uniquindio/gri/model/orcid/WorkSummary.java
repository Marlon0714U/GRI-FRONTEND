package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class WorkSummary {
	@JsonProperty("put-code")
	public int putCode;
	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Source source;
	public Title title;
	@JsonProperty("external-ids")
	public ExternalIds externalIds;
	public Url url;
	public String type;
	@JsonProperty("publication-date")
	public PublicationDate publicationDate;
	@JsonProperty("journal-title")
	public JournalTitle journalTitle;
	public String visibility;
	public String path;
	@JsonProperty("display-index")
	public String displayIndex;
}
