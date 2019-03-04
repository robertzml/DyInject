package robertzml.dyinject;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/*
 Hook类
 */
public class Hook {

    private HttpClient client = new HttpClient();

    private XC_LoadPackage.LoadPackageParam lpparam;

    public Hook(XC_LoadPackage.LoadPackageParam lpparam) {
        this.lpparam = lpparam;
    }

    /**
     * Hook 评论
     */
    public void Comment() {
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

                        String r = client.Post("data", js);
                        super.afterHookedMethod(param);

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
                    }
                });
    }

    /**
     * 粉丝列表
     */
    public void Follower() {

        findAndHookMethod("com.ss.android.ugc.aweme.following.b.a", lpparam.classLoader, "getItems",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower Start---");

                        // XposedBridge.log(param.args[1].getClass().getName());
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower End---");

                        Object result = param.getResult();

                        if (result != null) {
                            // XposedBridge.log(result.toString());

                            String js = JSON.toJSONString(result);
                            // XposedBridge.log(js);

                            JSONArray jsonArray = JSON.parseArray(js);
                            int size = jsonArray.size();

                            XposedBridge.log(Integer.toString(size));

                            if (size > 20) {
                                List<Object> ls = jsonArray.subList(size - 20, size);
                                js = JSON.toJSONString(ls);
                            }

                            HttpRunnable runnable = new HttpRunnable(js);
                            Thread thread1 = new Thread(runnable);
                            thread1.start();
                        }

                        super.afterHookedMethod(param);
                    }
                });
    }

    /**
     * 用户个人信息页
     */
    public void Profile() {
        Class<?> u = XposedHelpers.findClass("com.ss.android.ugc.aweme.profile.model.User", lpparam.classLoader);

        findAndHookMethod("com.ss.android.ugc.aweme.profile.api.g", lpparam.classLoader, "c",
                u, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Profile Start---");

                        Object p = param.args[0];
                        if (p != null) {
                            String js = JSON.toJSONString(p);
                            //XposedBridge.log(js);

                            ProfileRunnable runnable = new ProfileRunnable(js);
                            Thread thread1 = new Thread(runnable);
                            thread1.start();
                        }

                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Profile End---");

                        super.afterHookedMethod(param);
                    }
                });
    }


    private void HookFollower2() {

        Class<?> c = XposedHelpers.findClass("com.ss.android.ugc.aweme.following.b.b", lpparam.classLoader);

        findAndHookMethod("com.ss.android.ugc.aweme.app.api.a", lpparam.classLoader, "a",
                String.class, Class.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower Start---");
                        XposedBridge.log("first = " + param.args[0]);

                        // XposedBridge.log(param.args[1].getClass().getName());
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower End---");

                        Object result = param.getResult();

                        if (result != null) {
                            XposedBridge.log(result.toString());

                            String js = JSON.toJSONString(result);
                            XposedBridge.log(js);

                            String r = client.Post("http://192.168.1.106:5000/data", js);
                        }
                        super.afterHookedMethod(param);
                    }
                });
    }


    public void HookChatActivity() {

        findAndHookMethod("com.ss.android.ugc.aweme.im.sdk.chat.ChatRoomActivity", lpparam.classLoader, "onCreate",
                Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook chat Start---");
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook chat End---");

                        // Object result = param.getResult();

                        super.afterHookedMethod(param);
                    }
                });
    }


    /**
     *  微视粉丝列表
     */
    public void HookWsFollower() {
        Class<?> c = XposedHelpers.findClass("com.tencent.oscar.model.User", lpparam.classLoader);
        Class<?> list = XposedHelpers.findClass("java.util.List", lpparam.classLoader);
        Class<?> d = XposedHelpers.findClass("NS_KING_INTERFACE.stGetUsersRsp", lpparam.classLoader);


        findAndHookMethod("com.tencent.oscar.module.main.profile.ProfileFansActivity", lpparam.classLoader, "a",
                d, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower Start---");

                        Object param0 = param.args[0];

                        if (param0 != null) {
                            String js = JSON.toJSONString(param0);
                            XposedBridge.log(js);
                        }

                        //XposedBridge.log("first = " + param.args[0]);
                        //XposedBridge.log("second = " + param.args[1]);

                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("---Hook Follower End---");

                        super.afterHookedMethod(param);
                    }
                });
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
