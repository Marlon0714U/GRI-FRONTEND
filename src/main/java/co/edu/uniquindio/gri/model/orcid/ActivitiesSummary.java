package co.edu.uniquindio.gri.model.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ActivitiesSummary {
	@JsonProperty("last-modified-date")
	public LastModifiedDate lastModifiedDate;
	public Distinctions distinctions;
	public Educations educations;
	public Employments employments;
	public Fundings fundings;
	@JsonProperty("invited-positions")
	public InvitedPositions invitedPositions;
	public Memberships memberships;
	@JsonProperty("peer-reviews")
	public PeerReviews peerReviews;
	public Qualifications qualifications;
	@JsonProperty("research-resources")
	public ResearchResources researchResources;
	public Services services;
	public Works works;
	public String path;
}
