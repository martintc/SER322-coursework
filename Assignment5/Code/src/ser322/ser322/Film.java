package ser322;

public class Film {
	int film_id; // pk
	String title;
	String description;
	int release_year;
	int language_id; // fk to Language
	int length;
}