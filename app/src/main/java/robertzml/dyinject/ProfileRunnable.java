package robertzml.dyinject;

/**
 * 用户信息上传线程
 */
public class ProfileRunnable implements Runnable {

    private HttpClient client = new HttpClient();

    private String content;

    public ProfileRunnable(String content) {
        this.content = content;
    }

    @Override
    public void run() {
        String r = client.Post("profile", content);
    }
}
