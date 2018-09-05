package robertzml.dyinject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.IOException;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import okhttp3.*;
import com.alibaba.fastjson.*;

public class MainXposed implements IXposedHookLoadPackage {

    public static final MediaType Media = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.ss.android.ugc.aweme")) {
            return;
        }
        XposedBridge.log("Injection Loaded app: " + lpparam.packageName);

        HookFollower(lpparam);
        HookFollowing(lpparam);
        HookComment(lpparam);
    }

    private void HookComment(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod("com.ss.android.ugc.aweme.comment.api.CommentApi", lpparam.classLoader, "a",
                String.class, long.class, int.class, int.class, String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Comment Start---");
                        XposedBridge.log("first = " + param.args[0]);
                        //XposedBridge.log(param.getResult().toString());
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Comment End---");
                        XposedBridge.log("first = " + param.args[0]);
                        Object result = param.getResult();

                        XposedBridge.log(result.toString());

                        String js = JSON.toJSONString(result);
                        XposedBridge.log(js);

                        // String r = post("http://192.168.1.106:5000/comment", js);
                        /*
                        StringBuilder sbName = new StringBuilder();
                        StringBuilder sbValue = new StringBuilder();
                        String[] fieldNames = getFiledName(result);

                        for(int j=0 ; j<fieldNames.length ; j++){     //遍历所有属性
                            String name = fieldNames[j];    //获取属性的名字
                            Object value = getFieldValueByName(name, result);
                            sbName.append(name);
                            sbValue.append(value);
                            if(j != fieldNames.length - 1) {
                                sbName.append("/");
                                sbValue.append("/");
                            }
                        }

                        XposedBridge.log("Field: " + sbName.toString());
                        XposedBridge.log("Value:" + sbValue.toString());
                        */
                        super.afterHookedMethod(param);
                    }
                });
    }

    // 关注列表
    private void HookFollowing(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> c = XposedHelpers.findClass("com.ss.android.ugc.aweme.following.a.c", lpparam.classLoader);

        findAndHookMethod("com.ss.android.ugc.aweme.following.b.c", lpparam.classLoader, "a",
                c, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Following Start---");
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Following End---");

                        super.afterHookedMethod(param);
                    }
                });
    }

    // 粉丝列表
    private void HookFollower(XC_LoadPackage.LoadPackageParam lpparam) {

        Class<?> c = XposedHelpers.findClass("com.ss.android.ugc.aweme.following.a.c", lpparam.classLoader);

        findAndHookMethod("com.ss.android.ugc.aweme.following.b.a", lpparam.classLoader, "a",
                c, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower Start---");
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower End---");

                        super.afterHookedMethod(param);
                    }
                });
    }

    private String post(String url, String json) {
        RequestBody body = RequestBody.create(Media, json);
        Request request = new Request.Builder()
                .url(url)
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

    /**
     * 获取属性名数组
     * */
    private static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            // System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 根据属性名获取属性值
     * */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {

            return null;
        }
    }
}
