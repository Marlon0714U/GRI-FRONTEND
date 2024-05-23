package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Distinctions {
	@JsonProperty("last-modified-date")
	public Object lastModifiedDate;
	@JsonProperty("affiliation-group")
	public ArrayList<Object> affiliationGroup;
	public String path;
}
