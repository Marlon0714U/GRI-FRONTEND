package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JournalTitle {
	 public String value;
}
