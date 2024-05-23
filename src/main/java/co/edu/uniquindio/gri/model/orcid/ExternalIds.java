package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ExternalIds {
	@JsonProperty("external-id")
	public ArrayList<ExternalId> externalId;
}
