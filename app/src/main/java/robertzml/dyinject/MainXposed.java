package robertzml.dyinject;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XposedBridge;


public class MainXposed implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.ss.android.ugc.aweme")) {
            return;
        }
        XposedBridge.log("Injection Loaded app: " + lpparam.packageName);

        //HookFollower2(lpparam);
        //HookFollowing(lpparam);
        //HookComment(lpparam);
        //HookChatActivity(lpparam);
    }
}