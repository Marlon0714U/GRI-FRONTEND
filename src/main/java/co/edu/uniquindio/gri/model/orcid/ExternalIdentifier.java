package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ExternalIdentifier {
	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Source source;
	@JsonProperty("external-id-type")
	public String externalIdType;
	@JsonProperty("external-id-value")
	public String externalIdValue;
	@JsonProperty("external-id-url")
	public ExternalIdUrl externalIdUrl;
	@JsonProperty("external-id-relationship")
	public String externalIdRelationship;
	public String visibility;
	public String path;
	@JsonProperty("put-code")
	public int putCode;
	@JsonProperty("display-index")
	public int displayIndex;
}
