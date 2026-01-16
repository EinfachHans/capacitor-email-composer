package de.einfachhans.emailcomposer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.Html;
import com.getcapacitor.JSArray;
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
    List<String> toList = call.getArray("to", new JSArray()).toList();
    String[] to = new String[toList.size()];
    to = toList.toArray(to);

    // CC
    List<String> ccList = call.getArray("cc", new JSArray()).toList();
    String[] cc = new String[ccList.size()];
    cc = ccList.toArray(cc);

    // BCC
    List<String> bccList = call.getArray("bcc", new JSArray()).toList();
    String[] bcc = new String[bccList.size()];
    bcc = bccList.toArray(bcc);

    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(MAILTO_SCHEME));
    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    intent.putExtra(Intent.EXTRA_TEXT, bodyText);
    intent.putExtra(Intent.EXTRA_EMAIL, to);
    intent.putExtra(Intent.EXTRA_CC, cc);
    intent.putExtra(Intent.EXTRA_BCC, bcc);

    // Attachments
    JSArray attachments = call.getArray("attachments", new JSArray());
    return getIntentAccordingToAttachments(attachments, intent);
  }

  /**
   * Get the right intent according to the attachments.
   * In case of no attachments return the original intent.
   * @param attachments the attachments
   * @param intent the intent
   * @return the right intent
   * @throws JSONException in case of a json parsing error
   */
  private Intent getIntentAccordingToAttachments(JSArray attachments, Intent intent) throws JSONException {
    if (attachments == null || attachments.length() == 0) {
      return intent;
    }
    ArrayList<Uri> uris = new ArrayList<>();
    AssetUtil assets = new AssetUtil(ctx);

    for (int i = 0; i < attachments.length(); i++) {
      JSONObject attachment = attachments.getJSONObject(i);
      String path = attachment.getString("path");
      String type = attachment.getString("type");
      String name = attachment.optString("name");

      Uri uri = assets.parse(path, type, name);
      if (uri != null && uri != Uri.EMPTY) {
        uris.add(uri);
      }
    }

    if (uris.isEmpty()) {
      return intent;
    }

    if (uris.size() == 1) {
      intent.setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_STREAM, uris.get(0));
      return createFilteredEmailChooser(intent);
    }

    intent
      .setAction(Intent.ACTION_SEND_MULTIPLE)
      .setType("application/octet-stream")
      .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      .putExtra(Intent.EXTRA_STREAM, uris);
    return createFilteredEmailChooser(intent);
  }

  /**
   * This creates a chooser intent with only e-mail apps
   * @param originalIntent the original intent to wrap with the chooser intent
   * @return a new intent
   */
  private Intent createFilteredEmailChooser(Intent originalIntent) {
    PackageManager pm = this.ctx.getPackageManager();

    // First, get all true email apps by checking mailto support
    Intent mailtoIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:test@example.com"));
    List<ResolveInfo> emailApps = pm.queryIntentActivities(mailtoIntent, 0);

    // Filter to only include apps that are in both lists
    List<Intent> emailIntents = new ArrayList<>();

    for (ResolveInfo resolveInfo : emailApps) {
      String packageName = resolveInfo.activityInfo.packageName;

      // Only include if it's a known email app
      Intent targetIntent = new Intent(originalIntent);
      targetIntent.setPackage(packageName);
      targetIntent.setClassName(packageName, resolveInfo.activityInfo.name);
      emailIntents.add(targetIntent);
    }

    if (emailIntents.isEmpty()) {
      return originalIntent;
    }

    // Create chooser with filtered email apps only
    Intent chooserIntent = Intent.createChooser(emailIntents.remove(0), "Send email...");
    if (!emailIntents.isEmpty()) {
      chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, emailIntents.toArray(new Intent[0]));
    }

    return chooserIntent;
  }
}
