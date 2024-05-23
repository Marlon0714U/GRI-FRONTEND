package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResearcherUrls {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	@JsonProperty("researcher-url")
	public ArrayList<ResearcherUrl> researcherUrl;
	public String path;
}
