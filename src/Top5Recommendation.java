import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Top5Recommendation {
	static HashMap<Integer, String> movies = new HashMap<Integer, String>();
	static HashMap<Integer, HashMap<Integer, Integer>> ratings = new HashMap<Integer, HashMap<Integer, Integer>>();
	static HashMap<Integer, HashMap<Integer, Double>> hold = new HashMap<Integer, HashMap<Integer, Double>>();
	static double[][] sim;

	public static void main(String[] args) throws FileNotFoundException {
		loadRatings();
		loadMovies();
		recommend();
		calculate();
		topFive();
		// compare(list, list2, hold);

	}

	static void loadRatings() throws FileNotFoundException {
		String array[];
		BufferedReader reader;
		BufferedReader reader2;
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\Brandon-PC.000\\Desktop\\Java Books\\All Java\\Data Structures\\Final\\src\\ratings.dat"));
			String line = reader.readLine();
			while (line != null) {
				String[] tokens = line.split("\t");
				int key = Integer.parseInt(tokens[1]);
				int userid = Integer.parseInt(tokens[0]);
				HashMap<Integer, Integer> ratingsMap;
				ratingsMap = ratings.get(key);
				if (ratingsMap == null) {
					ratingsMap = new HashMap<Integer, Integer>();
					ratings.put(key, ratingsMap);
					ratingsMap.put(userid, Integer.parseInt(tokens[2]));
				} else {
					ratingsMap.put(userid, Integer.parseInt(tokens[2]));
				}
				HashMap<Integer, Double> ratingsEstimate;
				ratingsEstimate = hold.get(userid);
				if (ratingsEstimate == null) {
					hold.put(userid, new HashMap<Integer, Double>());
				}
				line = reader.readLine();
			}
//			for (Integer movieID : ratings.keySet()) {
//				System.out.println(movieID);
//				HashMap<Integer, Integer> ratingsMap;
//				ratingsMap = ratings.get(movieID);
//				for (Integer userID : ratingsMap.keySet()) {
//					System.out.println("\t" + userID + " " + ratingsMap.get(userID));
//				}
//			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void loadMovies() throws FileNotFoundException {
		String array[];
		BufferedReader reader;
		BufferedReader reader2;
		int counter = 0;
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\Brandon-PC.000\\Desktop\\Java Books\\All Java\\Data Structures\\Final\\src\\movies.dat"));
			String line = reader.readLine();
			while (line != null) {
				// System.out.println(line);
				String[] tokens = line.split("\\|");
				// System.out.println(tokens[0] + "|" + tokens[1]);
				movies.put(Integer.parseInt(tokens[0]), tokens[1]);
				// list2.add(tokens[0]);
				line = reader.readLine();
			}

//			for (Integer movieID : movies.keySet()) {
//				System.out.println(movieID + " " + movies.get(movieID));
//			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void recommend() {
		int size = movies.keySet().size();
		sim = new double[size][size];
		double numerator = 0;
		double denom1 = 0;
		double denom2 = 0;
		HashMap<Integer, Integer> movieRating1;
		HashMap<Integer, Integer> movieRating2;
		Integer rating1;
		Integer rating2;
		int r1;
		int r2;

		//n^2
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == j) {
					sim[i][j] = 1;
				} else {
					movieRating1 = ratings.get(i + 1);
					movieRating2 = ratings.get(j + 1);
					numerator = 0;
					denom1 = 0;
					denom2 = 0;
					for (int userid : hold.keySet()) {
						rating1 = movieRating1.get(userid);
						rating2 = movieRating2.get(userid);
						if (rating1 == null) {
							r1 = 0;
						} else {
							r1 = rating1.intValue();
						}
						if (rating2 == null) {
							r2 = 0;
						} else {
							r2 = rating2.intValue();
						}
						numerator += (r1 * r2);
						denom1 += (r1 * r1);
						denom2 += (r2 * r2);
					}
					sim[i][j] = (numerator / (Math.sqrt(denom1) * Math.sqrt(denom2)));
				}
			}
		}
		// System.out.println("Similarity Table");
		// for (int i = 0; i < size; i++) {
		// for (int j = 0; j < size; j++) {
		// System.out.printf("%.2f ", sim[i][j]);
		// }
		// System.out.println();
		// }
	}

	static void calculate() {
		HashMap<Integer, Integer> movieRatings;
		HashMap<Integer, Integer> userMovieRatings;
		HashMap<Integer, Double> estimatedRatings;
		int size = movies.keySet().size();
		Integer rating;
		Integer userRating;
		int r1;
		double num;
		double denom;

		for (int i = 1; i <= size; i++) {
			movieRatings = ratings.get(i);
			for (int userid : hold.keySet()) {
				rating = movieRatings.get(userid);
				if (rating == null) {
					num = 0;
					denom = 0;
					for (int j = 1; j <= size; j++) {
						userMovieRatings = ratings.get(j);
						userRating = userMovieRatings.get(userid);
						if (userRating != null) {
							r1 = userRating.intValue();
							num += (r1 * sim[i - 1][j - 1]);
							denom += sim[i - 1][j - 1];
						}
					}
					estimatedRatings = hold.get(userid);
					estimatedRatings.put(i, (num / denom));
				}
			}
		}
//		for (Integer userID : hold.keySet()) {
//			System.out.println(userID);
//			HashMap<Integer, Double> ratingsMap;
//			ratingsMap = hold.get(userID);
//			for (Integer movieid : ratingsMap.keySet()) {
//				System.out.println("\t" + movieid + " " + ratingsMap.get(movieid));
//			}
//		}
	}

	static void topFive() {
		int fiveMovies[] = new int[5];
		double fiveRatings[] = new double[5];
		int counter = 0;
		int lowestRating = 0;
		
		HashMap<Integer, Double> ratingsMap;
		for (int userid : hold.keySet()) {
			ratingsMap = hold.get(userid);
			counter = 0;
			for (int movie : ratingsMap.keySet()) {
				if(counter < 5) {
					fiveMovies[counter] = movie;
					fiveRatings[counter] = ratingsMap.get(movie);
					counter++;
				}else {
					lowestRating = 0;
					for(int i = 0; i < fiveRatings.length; i++) {
						if(fiveRatings[i] < lowestRating) {
							lowestRating = i;
						}
					}
					if(ratingsMap.get(movie) > fiveRatings[lowestRating]) {
						fiveMovies[lowestRating] = movie;
						fiveRatings[lowestRating] = ratingsMap.get(movie);
					}
				}
			}
			for(int i = 0; i < counter; i++) {
				int highestRating = i;
				int tempMovieID = 0;
				double tempRating = 0;
				for(int j = i + 1; j < counter; j++) {
					if(fiveRatings[j] > fiveRatings[highestRating]) {
						highestRating = j;
					}
				}
				tempMovieID = fiveMovies[i];
				fiveMovies[i] = fiveMovies[highestRating];
				fiveMovies[highestRating] = tempMovieID;
				
				tempRating = fiveRatings[i];
				fiveRatings[i] = fiveRatings[highestRating];
				fiveRatings[highestRating] = tempRating;
			}
			System.out.print("User ID: " + userid + " top " + counter + " recommendations: ");
			for(int i = 0; i < counter; i++) {
				System.out.print(movies.get(fiveMovies[i]) + "::" + fiveRatings[i] + "| ");
			}
			System.out.println("\n");
		}
	}
}
