package gods;

public class GreekGod extends God {
	private String nameGreek;

	public GreekGod(String nameEnglish, String nameGreek, String mainType, String subType, String description, String imagePath) {
		super(nameEnglish, mainType, subType, description, imagePath);
		this.nameGreek = nameGreek;
	}

	public String getNameGreek() {
		return nameGreek;
	}

	@Override
	public String toString() {
		return getNameEnglish() + " (" + nameGreek + ") - " + getMainType() + " - " + getSubType();
	}
}

