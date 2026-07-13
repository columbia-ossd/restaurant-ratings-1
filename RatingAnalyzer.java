import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RatingAnalyzer {

    protected ReviewReader reviewReader;
    protected Set<Review> reviews;

    public RatingAnalyzer(String file) {
        reviewReader = new ReviewReader(file);
        List<Review> reviewList = reviewReader.readReviews();
        reviews = new HashSet<>();
        for (Review r : reviewList) {
            reviews.add(r);
        }
    }

    public RatingAnalyzer() {

    }

    public double averageRating(String cuisine) {

        if (cuisine == null || cuisine.isBlank()) {
          return 0;
        }

        if (reviews == null) {
          return 0;
        }

        double total = 0;
        int count = 0;

        for (Review r : reviews) {
            if (r.cuisine != null && r.cuisine.equals(cuisine)) {

                if (r.rating != null && r.rating.isBlank() == false) {
                    try {
                        double value = Double.parseDouble(r.rating);
                        if (value > 0) {
                            total += value;
                            count++;
                        }
                    } catch (NumberFormatException e) {
                        // skip non-numeric ratings
                    }
                }
            }
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }

    public List<Review> topRatedRestaurants(String cuisine) {

        List<Review> topRestaurants = new ArrayList<>();
        double topRating = -1;

        for (Review r : reviews) {
            if (r.cuisine != null && r.cuisine.equals(cuisine)) {

                if (r.rating != null && r.rating.isBlank() == false) {
                    try {
                        double value = Double.parseDouble(r.rating);

                        if (value > 0) {
                            if (value > topRating) {
                                topRating = value;
                                topRestaurants.clear();
                                topRestaurants.add(r);
                            } else if (value == topRating) {
                                topRestaurants.add(r);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // skip non-numeric ratings
                    }
                }
            }
        }

        Collections.sort(topRestaurants, new Comparator<Review>() {
            public int compare(Review r1, Review r2) {
                return r1.restaurant.compareTo(r2.restaurant);
            }
        });

        return topRestaurants;
    }
    
    public static void main(String[] args) {
        RatingAnalyzer ra = new RatingAnalyzer("reviews.csv");
        String input = "";
        Scanner in = new Scanner(System.in);

        while (input.equalsIgnoreCase("quit") == false) {
            System.out.print("Enter the cuisine type ('quit' to exit): ");
            input = in.next();

            if (input.equalsIgnoreCase("quit") == false) {
                System.out.println("Avg rating: " + ra.averageRating(input));

                List<Review> topRestaurants = ra.topRatedRestaurants(input);

                if (topRestaurants.isEmpty()) {
                    System.out.println("No restaurants found for that cuisine.");
                } else {
                    System.out.println("Top rated restaurant(s):");
                    for (Review r : topRestaurants) {
                        System.out.println(r.restaurant + ": " + r.rating);
                    }
                }

                System.out.println();
            }
        }

        System.out.println("Good bye");
    }
}