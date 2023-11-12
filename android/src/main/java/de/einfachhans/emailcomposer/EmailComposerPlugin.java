package de.einfachhans.emailcomposer;

import android.content.Intent;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import org.json.JSONException;

@CapacitorPlugin(name = "EmailComposer")
public class EmailComposerPlugin extends Plugin {

    private EmailComposer implementation;

    @Override
    public void load() {
        this.implementation = new EmailComposer(this.getContext());
        AssetUtil.cleanupAttachmentFolder(this.getContext());
    }

    @PluginMethod
    public void hasAccount(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("hasAccount", true);
        call.resolve(ret);
    }

    @PluginMethod
    public void open(PluginCall call) throws JSONException {
        try {
            Intent draft = implementation.getIntent(call);
            startActivityForResult(call, draft, "openCallback");
        } catch (RuntimeException exception) {
            call.reject(exception.getLocalizedMessage());
        }
    }

    @ActivityCallback
    private void openCallback(PluginCall call, ActivityResult result) {
        call.resolve();
    }
}
