package tourGuide.model;

import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;

public class UserReward {
	public final VisitedLocationBean visitedLocation;
	public final AttractionBean attraction;
	private int rewardPoints;

	public UserReward(VisitedLocationBean visitedLocation, AttractionBean attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	public UserReward(VisitedLocationBean visitedLocation, AttractionBean attraction) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
	}

	public int getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public VisitedLocationBean getVisitedLocation() {
		return visitedLocation;
	}

	public AttractionBean getAttraction() {
		return attraction;
	}
}