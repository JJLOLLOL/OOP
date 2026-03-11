package models;

public class CareerRank {
  public static final Object[][] RANK = {
    {"Intern", 1.0},
    {"Junior Employee", 1.25},
    {"Employee", 1.5},
    {"Senior Employee", 1.8},
    {"Manager", 2.2},
    {"Director", 2.8},
    {"Executive", 3.5}
  };

  public static String getTitle(int rank) {
    validateRank(rank);
    return (String) RANK[rank - 1][0];
  }

  public static double getSalaryMultiplier(int rank) {
    validateRank(rank);
    return (double) RANK[rank - 1][1];
  }

  private static void validateRank(int rank) {
    if (rank < 1 || rank > RANK.length) {
      throw new IllegalArgumentException("Invalid rank");
    }
  }
}