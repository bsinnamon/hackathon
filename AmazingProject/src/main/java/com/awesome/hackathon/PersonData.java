package com.awesome.hackathon;

import java.io.Serializable;

public class PersonData implements Serializable {

	private static final long serialVersionUID = 8533668020080188713L;
	private ImageData mPhoto;
	private String mName, mGender, mComments;
	private int mAge;
	
	PersonData(ImageData photo, String name, String gender, int age, String comments)
	{
		mPhoto = photo;
		mName = name;
		mGender = gender;
		mAge = age;
		mComments = comments;
	}
	
	
	public ImageData getmPhoto() {
		return mPhoto;
	}

	public void setmPhoto(ImageData mPhoto) {
		this.mPhoto = mPhoto;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmGender() {
		return mGender;
	}

	public void setmGender(String mGender) {
		this.mGender = mGender;
	}

	public String getmComments() {
		return mComments;
	}

	public void setmComments(String mComments) {
		this.mComments = mComments;
	}

	public int getmAge() {
		return mAge;
	}

	public void setmAge(int mAge) {
		this.mAge = mAge;
	}
}
