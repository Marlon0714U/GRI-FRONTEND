package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Summary {
	
	@JsonProperty("membership-summary")
	public MembershipSummary membershipSummary;
	@JsonProperty("education-summary")
	public EducationSummary educationSummary;
	@JsonProperty("employment-summary")
	public EmploymentSummary employmentSummary;
	@JsonProperty("service-summary")
	public ServiceSummary serviceSummary;
}
