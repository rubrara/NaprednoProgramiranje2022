package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        this.movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return this.movies.stream()
                .sorted(Comparator.comparing(Movie::averageRating).reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {

        int max = this.movies.stream()
                .mapToInt(Movie::numberOfRatings)
                .max().getAsInt();


        Comparator<Movie> comparator = (l, r) -> {
            double first = l.averageRating() * l.numberOfRatings() / max;
            double second = r.averageRating() * r.numberOfRatings() / max;

            int result = Double.compare(second, first);
            if (result == 0) return l.getTitle().compareTo(r.getTitle());

            return result;
        };

        return this.movies.stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());

    }
}


class Movie {
    private String title;
    private List<Integer> ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = new ArrayList<>();

        for (int rating : ratings) {
            this.ratings.add(rating);
        }
    }

    public String getTitle() {
        return title;
    }

    public double averageRating() {
        return ratings.stream().mapToDouble(i -> i).average().orElse(0);
    }

    public int numberOfRatings() {
        return ratings.size();
    }

    // Love on the Run (1979) (7.33) of 6 ratings

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, averageRating(), numberOfRatings());
    }
}
