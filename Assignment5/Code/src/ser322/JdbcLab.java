package ser322;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

import ser322.Language;
import ser322.Actor;
import ser322.Film;
import ser322.FilmActor;

import java.io.File;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class JdbcLab {

	public static void main (String[] args) {
		System.out.println("running.....");
		if (args.length < 5) {
			System.out.println("Not correct number of arguments\n Run as: java JdbcLab <url> <user> <pwd> <driver> <qeury/export>");
			System.exit(1);
		}

		ResultSet rs = null;
		Statement stmt = null;
		Connection conn = null;

		String url, user, pwd, driver, query, film_id, actor_id, new_length, language_name;
		url = args[0];
		user = args[1];
		pwd = args[2];
		driver = args[3];
		query = args[4];
		film_id = "";
		actor_id = "";
		new_length = "";
		language_name = "";

		String fileName = "";

		if (query.equals("query1")) {
			if (args.length < 5) {
				System.out.println("Incorrect amount of parameters. \n Run as 'java JdbcLab <url> <user> <pwd> <driver> query1'");
				System.exit(1);
			}
		} else if (query.equals("query2")) {
			if (args.length != 6) {
				System.out.println("Incorrect amount of parameters. \n Run as 'java JdbcLab <url> <user> <pwd> <driver> query2 <Language>'");
				System.exit(1);
			}
			language_name = args[5];
		} else if (query.equals("updateFilm")) {
			if (args.length != 7) {
				System.out.println("Incorrect amount of parameters. \n Run as 'java JdbcLab <url> <user> <pwd> <driver> updateFilm <film_id> <new_length>'");
				System.exit(1);
			}
			film_id = args[5];
			new_length = args[6];
		} else if (query.equals("addActor")) {
			if (args.length != 7) {
				System.out.println("Incorrect amount of parameters. \n Run as 'java JdbcLab <url> <user> <pwd> <driver> addActor <actor_id> <film_id>'");
				System.exit(1);
			}
			actor_id = args[5];
			film_id = args[6];
		} else if (query.equals("export")) {
			if (args.length != 6) {
				System.out.println("Export requires 6 parameters.");
				System.out.println("Run as: java JdbcLab <url> <user> <pwd> <driver> export <filename>");
				System.exit(1);
			}
			fileName = args[5];
		}

		try {
			try {
				Class.forName(driver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn = DriverManager.getConnection(url, user, pwd);
			} catch (Exception e) {
				System.out.println("Could not gather a connection here");
			}
			stmt = conn.createStatement();

			switch (query) {
				case "query1" :
					query1(stmt);
					break;
				case "query2" :
					query2(stmt, language_name);
					break;
				case "updateFilm" :
					updateFilm(stmt, film_id, new_length);
					query1(stmt);
					break;
				case "addActor" :
					addActor(stmt, actor_id, film_id);
					break;
				case "export" :
					export(stmt, fileName);
					System.out.println("Export complete.");
					break;
				default :
					System.out.println("Not a proper query called.");
					System.exit(1);
			}

		} catch (Exception e) {
			System.out.println("Error occured");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (Throwable t) {

			}
			try {
				conn.close();
			} catch (Throwable t2) {
				System.err.println("Connection leak has occured!");
			}
		}
	}

	public static void query1 (Statement stmt) throws Exception {
		ResultSet rs = makeQuery1(stmt);
		System.out.println("Results for query:");
		while (rs.next()) {
			System.out.print(rs.getString(2) + "\t"); // title
			System.out.print(rs.getInt(1) + "\t"); // film id
			System.out.print(rs.getInt(3) + "\t"); // length
			System.out.print(rs.getInt(4) + "\t"); // year
			System.out.print(rs.getString(5) + "\t"); // language
			System.out.println("\n");
		}
	}

	public static ResultSet makeQuery1 (Statement stmt) throws Exception {
		ResultSet rs;
		rs = stmt.executeQuery("select film.film_id, film.title, film.length, film.release_year, language.name from film, language where film.language_id = language.language_id order by film.title asc");
		return rs;
	}

	public static ResultSet makeQuery2 (Statement stmt, String lang) throws Exception {
		ResultSet rs;
		rs = stmt.executeQuery("select films.title, films.release_year, films.film_id, ifnull(films.actor_count,0) from (select film.title, film.release_year, film.film_id, film.language_id, actors.actor_count from film left join (select film_actor.film_id, count(film_actor.actor_id) as actor_count from film_actor group by film_actor.film_id) as actors on film.film_id = actors.film_id) as films, (select language_id from language where name = " +  "'" + lang + "'" + ") as l where l.language_id = films.language_id");
		return rs;
	}

	public static void query2 (Statement stmt, String lang) throws Exception {
		ResultSet rs = makeQuery2(stmt, lang);
		while (rs.next()) {
			System.out.print(rs.getString(1) + "\t"); // title
			System.out.print(rs.getInt(3) + "\t"); // film id
			System.out.print(rs.getInt(2) + "\t"); // year
			System.out.print(rs.getInt(4) + "\t"); // Number of actors
			System.out.println("\n");
		}
	}

	public static void updateFilm (Statement stmt, String film, String length) throws Exception {
		ResultSet rs;
		int film_id = Integer.parseInt(film);
		int length_id = Integer.parseInt(length);
		stmt.executeUpdate("update film set film.length = " + length + " where film.film_id =" + film);
	}

	public static void addActor (Statement stmt, String actor, String film) throws Exception {
		stmt.executeUpdate("insert into film_actor (film_id, actor_id) values ("+ film + ", " + actor + ")");
	}

	public static void export (Statement stmt, String file_name) throws Exception {
		//File file = new File(file_name);
//		file.createNewFile();
		List<Language> lang = null;
		List<Actor> actor = null;
		List<Film> film = null;
		List<FilmActor> fa = null;
		Document doc = newDoc();
		// get all of the data out of the database and into datastructures
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("select * from language");
			lang = returnLanguageList(rs);
			rs = stmt.executeQuery("select * from actor");
			actor = returnActorList(rs);
			rs = stmt.executeQuery("select * from film");
			film = returnFilmList(rs);
			rs = stmt.executeQuery("select * from film_actor");
			fa = returnFilmActorList(rs);
		} catch (SQLException e) {
			System.out.println("Issue trying to query the database and read in all database data");
			throw new Exception();
		}

		// start building the data into the xml file
		Element root = doc.createElement("movies");
		doc.appendChild(root);
		createLanguageEntries(doc, root, lang);
		createActorEntries(doc, root, actor);
		createFilmEntries(doc, root, film);
		createFilmActorEntries(doc, root, fa);

		// write the xml file
		try {
			writeToXMLFile(doc, file_name);
		} catch (TransformerException e) {
			System.err.println("Unable to write the file.");
			e.printStackTrace();
			throw new Exception();
		}
	}

	public static Document newDoc () throws Exception {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		return documentBuilder.newDocument();
	}

	public static void writeToXMLFile (Document doc, String fileName) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(doc);
		StreamResult streamResult = new StreamResult(new File(fileName));
		transformer.transform(domSource, streamResult);
	}

	public static List returnLanguageList (ResultSet rs) throws SQLException {
		List<Language> list = new ArrayList<>();
		while (rs.next()) {
			Language l = new Language();
			int temp = rs.getInt(1);
			l.setLanguageID(temp);
			l.setName(rs.getString(2));
			list.add(l);
		}
		return list;
	}

	public static List returnActorList (ResultSet rs) throws SQLException {
		List<Actor> list = new ArrayList<>();
		while (rs.next()) {
			Actor a = new Actor();
			int temp = rs.getInt(1);
			if (rs.wasNull()) {
				continue;
			} else {
				a.actor_id = temp;
				a.first_name = rs.getString(2);
				a.last_name = rs.getString(3);
				list.add(a);
			}
		}
		return list;
	}

	public static List returnFilmList (ResultSet rs) throws SQLException {
		List<Film> list = new ArrayList<>();
		while (rs.next()) {
			Film f = new Film();
			f.film_id = rs.getInt(1);
			f.title = rs.getString(2);
			f.description = rs.getString(3);
			f.release_year = rs.getInt(4);
			f.language_id = rs.getInt(5);
			f.length = rs.getInt(6);
			list.add(f);
		}
		return list;
	}

	public static List returnFilmActorList (ResultSet rs) throws SQLException {
		List<FilmActor> list = new ArrayList<>();
		while (rs.next()) {
			FilmActor fa = new FilmActor();
			fa.actor_id = rs.getInt(1);
			fa.film_id = rs.getInt(2);
			list.add(fa);
		}
		return list;
	}

	public static void createLanguageEntries (Document doc, Element root, List<Language> lang) {
		Element languages = doc.createElement("languages");
		for (Language l : lang) {
			Element language = doc.createElement("language");
			Attr language_id = doc.createAttribute("language_id");
			language_id.setValue(Integer.toString(l.getLanguageID()));
			language.setAttributeNode(language_id);
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(l.getName()));
			language.appendChild(name);
			languages.appendChild(language);
		}
		root.appendChild(languages);

	}

	public static void createActorEntries (Document doc, Element root, List<Actor> actorList) {
		Element actors = doc.createElement("actors");
		for (Actor a : actorList) {
			Element actor = doc.createElement("actor");
			Attr actor_id = doc.createAttribute("actor_id");
			actor_id.setValue(Integer.toString(a.actor_id));
			actor.setAttributeNode(actor_id);
			Element firstName = doc.createElement("first_name");
			firstName.appendChild(doc.createTextNode(a.first_name));
			Element lastName = doc.createElement("last_name");
			lastName.appendChild(doc.createTextNode(a.last_name));
			actor.appendChild(firstName);
			actor.appendChild(lastName);
			actors.appendChild(actor);
		}
		root.appendChild(actors);

	}

	public static void createFilmEntries (Document doc, Element root, List<Film> filmList) {
		Element films = doc.createElement("films");
		for (Film f : filmList) {
			Element film = doc.createElement("film");
			Attr film_id = doc.createAttribute("film_id");
			film_id.setValue(Integer.toString(f.film_id));
			film.setAttributeNode(film_id);
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(f.title));
			Element description = doc.createElement("description");
			description.appendChild(doc.createTextNode(f.description));
			Element release_year = doc.createElement("release_year");
			release_year.appendChild(doc.createTextNode(Integer.toString(f.release_year)));
			Element language_id = doc.createElement("language_id");
			language_id.appendChild(doc.createTextNode(Integer.toString(f.language_id)));
			Element length = doc.createElement("length");
			length.appendChild(doc.createTextNode(Integer.toString(f.length)));
			film.appendChild(title);
			film.appendChild(description);
			film.appendChild(release_year);
			film.appendChild(language_id);
			film.appendChild(length);
			films.appendChild(film);
		}
		root.appendChild(films);

	}

	public static void createFilmActorEntries (Document doc, Element root, List<FilmActor> faList) {
		Element film_actors = doc.createElement("film_actors");
		for (FilmActor fa : faList) {
			Element fac = doc.createElement("film_actor");
			Attr actor_id = doc.createAttribute("actor_id");
			actor_id.setValue(Integer.toString(fa.actor_id));
			fac.setAttributeNode(actor_id);
			Attr film_id = doc.createAttribute("film_id");
			film_id.setValue(Integer.toString(fa.film_id));
			fac.setAttributeNode(film_id);
			film_actors.appendChild(fac);
		}
		root.appendChild(film_actors);

	}

}
