package tourGuide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import tourGuide.beans.ProviderBean;
import tourGuide.beans.VisitedLocationBean;

public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private UserPreferences userPreferences = new UserPreferences();
	private List<ProviderBean> tripDeals = new ArrayList<>();
	private final List<VisitedLocationBean> visitedLocations = new ArrayList<>();
	private final List<UserReward> userRewards = new ArrayList<>();

	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public List<ProviderBean> getTripDeals() {
		return tripDeals;
	}
	public void setTripDeals(List<ProviderBean> tripDeals) {
		this.tripDeals = tripDeals;
	}

	public List<VisitedLocationBean> getVisitedLocations() {
		return visitedLocations;
	}

	public List<UserReward> getUserRewards() {
		return userRewards;
	}

	//TODO THIS FOUR METHODS MUST BE MOVED ?

	//TODO CHECK IF OBJECT ALREADY EXIST / MOVE METHODE ?
	public void addUserReward(UserReward userReward) {
		userRewards.add(userReward);
	}

	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	public void addToVisitedLocations(VisitedLocationBean visitedLocation) {
		visitedLocations.add(visitedLocation);
	}

	public VisitedLocationBean getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}
}