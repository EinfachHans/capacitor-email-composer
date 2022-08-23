package de.einfachhans.emailcomposer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailComposer {

    private static final String MAILTO_SCHEME = "mailto:";
    private final Context ctx;

    public EmailComposer(Context ctx) {
        this.ctx = ctx;
    }

    public Intent getIntent(PluginCall call) throws JSONException {
        // Subject
        String subject = call.getString("subject", "");

        // Body
        String body = call.getString("body", "");
        CharSequence bodyText = call.getBoolean("isHtml", false) ? Html.fromHtml(body) : body;

        // To
        List<String> toList = call.getArray("to").toList();
        String[] to = new String[toList.size()];
        to = toList.toArray(to);

        // CC
        List<String> ccList = call.getArray("cc").toList();
        String[] cc = new String[ccList.size()];
        cc = ccList.toArray(cc);

        // BCC
        List<String> bccList = call.getArray("bcc").toList();
        String[] bcc = new String[bccList.size()];
        bcc = bccList.toArray(bcc);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MAILTO_SCHEME));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, bodyText);
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.putExtra(Intent.EXTRA_BCC, bcc);

        // Attachments
        this.setAttachments(call, intent);

        return intent;
    }

    private void setAttachments(PluginCall call, Intent intent) throws JSONException {
        JSArray attachments = call.getArray("attachments", new JSArray());
        ArrayList<Uri> uris = new ArrayList();
        AssetUtil assets = new AssetUtil(ctx);

        for (int i = 0; i < attachments.length(); i++) {
            JSONObject attachment = attachments.getJSONObject(i);
            String path = attachment.getString("path");
            String type = attachment.getString("type");
            String name = attachment.optString("name");

            Uri uri = assets.parse(path, type, name);
            if (uri != null && uri != Uri.EMPTY) uris.add(uri);
        }

        if (uris.isEmpty()) {
            return;
        }

        intent.setAction(Intent.ACTION_SEND_MULTIPLE)
                .setType("*/*")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra(Intent.EXTRA_STREAM, uris);

        if (uris.size() > 1) {
            return;
        }

        intent.setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_STREAM, uris.get(0));
    }
}
