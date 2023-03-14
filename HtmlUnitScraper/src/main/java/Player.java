import lombok.Data;

@Data
public class Player {

    private final String name;
    private final String id;
    private final Integer rating;
    private final Integer quickRating;

    public Player(String name, String id, Integer rating, Integer quickRating) {
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.quickRating = quickRating;
    }
}
