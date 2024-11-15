package gods;

public abstract class God {
	private String nameEnglish;
	private String mainType;
	private String subType;
	private String description;
	private String imagePath;

	public God(String nameEnglish, String mainType, String subType, String description, String imagePath) {
		this.nameEnglish = nameEnglish;
		this.mainType = mainType;
		this.subType = subType;
		this.description = description;
		this.imagePath = imagePath;
	}

	public String getNameEnglish() { return nameEnglish; }
	public String getMainType() { return mainType; }
	public String getSubType() { return subType; }
	public String getDescription() { return description; }
	public String getImagePath() { return imagePath; }

	@Override
	public String toString() {
		return nameEnglish + " - " + mainType + " - " + subType;
	}
}
