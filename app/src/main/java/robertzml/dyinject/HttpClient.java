package robertzml.dyinject;

import java.io.IOException;

import de.robv.android.xposed.XposedBridge;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http 客户端
 */
public class HttpClient {

    //region Field
    private static final MediaType Media = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private String host = "http://192.168.1.10:8819/";
    // private String host = "http://192.168.1.106:5000/";
    //endregion Field

    /**
     * 上传抓包内容
     * @param url 远程地址
     * @param json 上传数据
     * @return 返回结果
     */
    public String Post(String url, String json) {
        RequestBody body = RequestBody.create(Media, json);
        Request request = new Request.Builder()
                .url(host + url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            XposedBridge.log("Exception: " + e.getMessage());
        }

        return "";
    }


}
