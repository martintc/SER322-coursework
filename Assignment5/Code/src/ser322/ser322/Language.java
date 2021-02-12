package ser322;

public class Language {
	private int language_id; // pk
	private String name;

	public void setLanguageID (int i) {
		language_id = i;
	}

	public void setName (String s) {
		name = s;
	}

	public int getLanguageID () {
		return language_id;
	}

	public String getName () {
		return name;
	}
}