package co.edu.uniquindio.gri.model.orcid;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CompletionDate {
	public Year year;
	public Month month;
	public Day day;
	public String value;

}
