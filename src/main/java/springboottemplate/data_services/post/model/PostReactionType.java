package springboottemplate.data_services.post.model;

public enum PostReactionType {
    LIKE("👍️"),
    LOVE("❤️"),
    HAHA("😂️"),
    SAD("😢️");
    public final String emoji;
    PostReactionType(String emoji) {
        this.emoji = emoji;
    }
}
