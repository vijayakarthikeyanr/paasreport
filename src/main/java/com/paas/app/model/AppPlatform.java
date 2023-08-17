package com.paas.app.model;

public class AppPlatform {
	private String domain;
	private String org;
	private String applicationInstance;
	private String clNo;
	private String environment;
	private double microservices;
	private String technologyServiceOwner;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getApplicationInstance() {
		return applicationInstance;
	}

	public void setApplicationInstance(String applicationInstance) {
		this.applicationInstance = applicationInstance;
	}

	public String getClNo() {
		return clNo;
	}

	public void setClNo(String clNo) {
		this.clNo = clNo;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public double getMicroservices() {
		return microservices;
	}

	public void setMicroservices(double microservices) {
		this.microservices = microservices;
	}

	public String getTechnologyServiceOwner() {
		return technologyServiceOwner;
	}

	public void setTechnologyServiceOwner(String technologyServiceOwner) {
		this.technologyServiceOwner = technologyServiceOwner;
	}

}
