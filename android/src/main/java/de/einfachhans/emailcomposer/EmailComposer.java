package de.einfachhans.emailcomposer;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import com.getcapacitor.PluginCall;
import java.util.List;
import org.json.JSONException;

public class EmailComposer {

    private static final String MAILTO_SCHEME = "mailto:";

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
        return intent;
    }
}
