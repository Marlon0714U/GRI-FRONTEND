package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class History {
	@JsonProperty("creation-method")
	public String creationMethod;
	@JsonProperty("completion-date")
	public Object completionDate;
	@JsonProperty("submission-date")
	public SubmissionDate submissionDate;
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public boolean claimed;
	public Object source;
	@JsonProperty("deactivation-date")
	public Object deactivationDate;
	@JsonProperty("verified-email")
	public boolean verifiedEmail;
	@JsonProperty("verified-primary-email")
	public boolean verifiedPrimaryEmail;
}
