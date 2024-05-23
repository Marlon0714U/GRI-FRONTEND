package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Keyword {
	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Source source;
	public String content;
	public String visibility;
	public String path;
	@JsonProperty("put-code")
	public int putCode;
	@JsonProperty("display-index")
	public int displayIndex;
}
