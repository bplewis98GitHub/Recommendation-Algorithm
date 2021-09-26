import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class FinalTest {
	static HashMap ratingsMap = new HashMap();
	static HashMap moviesMap = new HashMap();
	static HashMap hold = new HashMap();
	static ArrayList list = new ArrayList();
	static ArrayList list2 = new ArrayList();

	public static void main(String[] args) throws FileNotFoundException {
		loadRatings(list);
		loadMovies(list2);
		compare(list, list2, hold);

	}

	static ArrayList loadRatings(ArrayList list) throws FileNotFoundException {
		BufferedReader reader;
		BufferedReader reader2;
		try {
			reader = new BufferedReader(new FileReader("D:/Movie Recommendation Project/ratings.dat"));
			String line = reader.readLine();
			while (line != null) {
				list.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	static ArrayList loadMovies(ArrayList list2) throws FileNotFoundException {
		String array[];
		BufferedReader reader;
		BufferedReader reader2;
		int counter = 0;
		try {
			reader = new BufferedReader(new FileReader("D:/Movie Recommendation Project/movies.dat"));
			String line = reader.readLine();
			while (line != null) {
				String[] tokens = line.split("\\(");
				list2.add(tokens[0]);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list2;

	}

	static HashMap compare(ArrayList list, ArrayList list2, HashMap hold) {
		int j = 0;
		int i = 0;
		int counter = 0;

		while (i < list.size()) {
			// Ratings
			String str1 = (String) list.get(i);
			// Movies
			String str2 = (String) list2.get(j);
			String[] tokens = str2.split("\\|");
			String[] sub = str1.split("\\t");
			
			//If they are equal
			if (sub[1].equals(tokens[0])) {
				hold.put(counter, str2.concat(" = " + str1));
				i++;
			} 
			//If they are not equal but i does not equal ratings size
			else if (!sub[1].equals(tokens[0]) && i != list.size()) {
				i++;
				
			} 
			//If they are not equal and i equals rating size
			else if (!sub[1].equals(tokens[0]) && i == list.size()) {
				i = 0;
				j++;
			}
		}
		for (int q = 0; q < hold.size(); q++) {
			System.out.println(hold.get(q));
		}
		return hold;
	}

}