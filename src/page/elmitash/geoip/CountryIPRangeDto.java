package page.elmitash.geoip;

public class CountryIPRangeDto {
	String country;
	String firstIp;
	String lastIp;
	long firstNum;
	long lastNum;

	public CountryIPRangeDto(String country, String firstIp, String lastIp, long firstNum, long lastNum) {
		super();
		this.country = country;
		this.firstIp = firstIp;
		this.lastIp = lastIp;
		this.firstNum = firstNum;
		this.lastNum = lastNum;
	}

	@Override
	public String toString() {
		return firstIp + "," + lastIp + "," + country;
	}
}
