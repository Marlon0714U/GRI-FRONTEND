package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressA {

	@JsonProperty("created-date")
	public CreatedDate createdDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Source source;
	public Country country;
	public String visibility;
	public String path;
	@JsonProperty("put-code")
	public int putCode;
	@JsonProperty("display-index")
	public int displayIndex;
	public String city;
	public String region;

}
