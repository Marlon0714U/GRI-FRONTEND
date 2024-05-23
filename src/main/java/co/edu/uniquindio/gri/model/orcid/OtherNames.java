package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class OtherNames {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	@JsonProperty("other-name")
	public ArrayList<OtherName> otherName;
	public String path;
}
