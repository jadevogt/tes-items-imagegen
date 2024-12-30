package best.tigers.inventory.util;

import best.tigers.inventory.items.Item;
import work.socialhub.kbsky.BlueskyFactory;
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetAuthorFeedRequest;
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedPostRequest;
import work.socialhub.kbsky.api.entity.com.atproto.repo.RepoUploadBlobRequest;
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest;
import work.socialhub.kbsky.auth.BearerTokenAuthProvider;
import work.socialhub.kbsky.model.app.bsky.embed.EmbedImages;
import work.socialhub.kbsky.model.app.bsky.embed.EmbedImagesImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlueSky {
  public static final int HOURS_THREEDAYS = 72;

  static class PostSorter implements Comparator<Post> {

    @Override
    public int compare(Post o1, Post o2) {
      int c1 = o1.likeCount() + o1.repostCount();
      int c2 = o2.likeCount() + o2.repostCount();
      return Integer.compare(c1, c2);
    }
  }
  
  public static List<Post> getPosts() {
    return getPosts(HOURS_THREEDAYS);
  }

  public static List<Post> getPosts(int numPosts) {
    var createSessionRequest = new ServerCreateSessionRequest();
    createSessionRequest.setIdentifier(System.getenv("BLUESKY_ACC_TARGET"));
    createSessionRequest.setPassword(System.getenv("BLUESKY_ACC_PASS"));
    var response = BlueskyFactory.INSTANCE
        .instance("https://bsky.social")
        .server()
        .createSession(createSessionRequest);

    var auth = new BearerTokenAuthProvider(response.getData().accessJwt);
    var authorFeedRequest = new FeedGetAuthorFeedRequest(auth);
    authorFeedRequest.setActor(System.getenv("BLUESKY_ACC_SOURCE"));
    List<Post> foundPosts = new ArrayList<>();
    while (foundPosts.size() < numPosts) {
      var posts = BlueskyFactory.INSTANCE.instance("https://bsky.social").feed().getAuthorFeed(authorFeedRequest);
      for (var post : posts.getData().getFeed()) {
        int likeCount = post.getPost().getLikeCount();
        int repostCount = post.getPost().getRepostCount();
        String content = post.getPost().getRecord().getAsFeedPost().getText();
        var n = new Post(content, likeCount, repostCount);
        foundPosts.add(n);
      }
      authorFeedRequest.setCursor(posts.getData().getCursor());
    }
    foundPosts.sort((new PostSorter()).reversed());
    return foundPosts.stream().filter(i -> Parser.itemFilter(i.content())).limit(12).toList();
  }
  
  public static void createPost(BufferedImage image, List<Item> items) throws IOException {
    var createSessionRequest = new ServerCreateSessionRequest();
    createSessionRequest.setIdentifier(System.getenv("BLUESKY_ACC_TARGET"));
    createSessionRequest.setPassword(System.getenv("BLUESKY_ACC_PASS"));
    var response = BlueskyFactory.INSTANCE
        .instance("https://bsky.social")
        .server()
        .createSession(createSessionRequest);
    var baos = new ByteArrayOutputStream();
    BufferedImage newBufferedImage = new BufferedImage(image.getWidth(),
        image.getHeight(), BufferedImage.TYPE_INT_RGB);
    newBufferedImage.getGraphics().drawImage(image, 0, 0, null);
    ImageIO.write(newBufferedImage, "jpg", baos);
    byte[] bytes = baos.toByteArray();
    var auth = new BearerTokenAuthProvider(response.getData().accessJwt);
    var imgUploadRequest = new RepoUploadBlobRequest(auth, bytes, "compiledimg.jpeg", "image/jpeg");
    var imageUploadResponse = BlueskyFactory.INSTANCE.instance("https://bsky.social").repo().uploadBlob(imgUploadRequest);
    var embedImages = new EmbedImages();
    var embedImage =  new EmbedImagesImage();
    embedImage.setImage(imageUploadResponse.getData().getBlob());
    embedImage.setAlt(String.join(", ", items.stream().map(Item::name).toList()));
    embedImages.setImages(List.of(embedImage));
    FeedPostRequest postRequest = new FeedPostRequest(auth);
    postRequest.setEmbed(embedImages);
    BlueskyFactory.INSTANCE.instance("https://bsky.social").feed().post(postRequest);
  }
}
