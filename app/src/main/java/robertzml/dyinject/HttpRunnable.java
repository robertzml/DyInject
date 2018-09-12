package robertzml.dyinject;

/**
 * HTTP请求线程实现
 */
public class HttpRunnable implements Runnable {

    private HttpClient client = new HttpClient();

    private String content;

    public HttpRunnable(String content) {
        this.content = content;
    }

    @Override
    public void run() {
        String r = client.Post("data", content);
    }
}
