package co.edu.uniquindio.gri.model.orcid;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;
@Data
@ToString

public class Addresses {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public ArrayList<AddressA> address;
	public String path;
}
