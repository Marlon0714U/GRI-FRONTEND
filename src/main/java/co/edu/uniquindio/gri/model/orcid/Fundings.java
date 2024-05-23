package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Fundings {
	@JsonProperty("last-modified-date")
	public Object lastModifiedDate;
	public ArrayList<Group> group;
	public String path;
}
