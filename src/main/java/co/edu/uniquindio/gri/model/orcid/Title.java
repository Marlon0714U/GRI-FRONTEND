package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Title {
	public Title title;
	public Object subtitle;
	@JsonProperty("translated-title")
	public Object translatedTitle;
	public String value;
}
