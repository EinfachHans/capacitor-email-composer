package de.einfachhans.emailcomposer;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

final class AssetUtil {

    // Path where to put tmp the attachments.
    private static final String ATTACHMENT_FOLDER = "/email_composer";

    // Application context
    private final Context ctx;

    AssetUtil(Context ctx) {
        this.ctx = ctx;
    }

    static void cleanupAttachmentFolder(Context ctx) {
        try {
            String path = ctx.getExternalCacheDir() + ATTACHMENT_FOLDER;
            File dir    = new File(path);

            if (!dir.isDirectory())
                return;

            File[] files = dir.listFiles();

            assert files != null;
            for (File file : files) { file.delete(); }
        } catch (Exception ignored){
        }
    }

    Uri parse(String path, String type, String name) {
        switch (type) {
            case "absolute":
                return this.getUriForAbsolutePath(path);
            case "resource":
                return this.getUriForResourcePath(path);
            case "asset":
                return this.getUriForAssetPath(path);
            case "base64":
                return this.getUriForBase64Content(path, name);
            default:
                throw new RuntimeException("Unknown Attachment Type");
        }
    }

    private Uri getUriForAbsolutePath(String path) {
        File file = new File(path);

        if (!file.exists()) {
            throw new RuntimeException("File does not exist at absolute path");
        }

        return getUriForFile(ctx, file);
    }

    private Uri getUriForAssetPath(String path) {
        String resPath = "public/assets" + path;
        String fileName = resPath.substring(resPath.lastIndexOf('/') + 1);
        File dir = ctx.getExternalCacheDir();

        if (dir == null) {
            return Uri.EMPTY;
        }

        String storage = dir.toString() + ATTACHMENT_FOLDER;
        File file = new File(storage, fileName);
        new File(storage).mkdir();

        try {
            AssetManager assets = ctx.getAssets();
            InputStream in = assets.open(resPath);
            FileOutputStream out = new FileOutputStream(file);
            copyStream(in, out);
        } catch (Exception e) {
            throw new RuntimeException("File does not exist at assets");
        }

        return getUriForFile(ctx, file);
    }

    private Uri getUriForResourcePath(String path) {
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        String resName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = path.substring(path.lastIndexOf('.'));
        File dir = ctx.getExternalCacheDir();
        int resId = getResId(path);

        if (dir == null) {
            return Uri.EMPTY;
        }

        if (resId == 0) {
            throw new RuntimeException("File does not exist at resources");
        }

        String storage = dir.toString() + ATTACHMENT_FOLDER;
        File file = new File(storage, resName + extension);
        new File(storage).mkdir();

        try {
            Resources res = ctx.getResources();
            InputStream in = res.openRawResource(resId);
            FileOutputStream out = new FileOutputStream(file);
            copyStream(in, out);
        } catch (Exception e) {
            throw new RuntimeException("File does not exist at resources");
        }

        return getUriForFile(ctx, file);
    }

    private Uri getUriForBase64Content(String base64, String name) {
        File dir = ctx.getExternalCacheDir();

        if (dir == null) {
            return Uri.EMPTY;
        }

        String storage = dir.toString() + ATTACHMENT_FOLDER;
        File file = new File(storage, name);
        new File(storage).mkdir();

        try {
            byte[] bytes = Base64.decode(base64, 0);
            InputStream in = new ByteArrayInputStream(bytes);
            FileOutputStream out = new FileOutputStream(file);
            copyStream(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getUriForFile(ctx, file);
    }

    private Uri getUriForFile(Context ctx, File file) {
        String authority = ctx.getPackageName() + ".fileprovider";

        try {
            return Provider.getUriForFile(ctx, authority, file);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get uri for file");
        }
    }

    private void copyStream(InputStream in, FileOutputStream out) {
        byte[] buffer = new byte[1024];
        int read;

        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getResId(String resPath) {
        Resources res = ctx.getResources();
        String pkgName = ctx.getPackageName();
        String dirName = "drawable";
        String fileName = resPath;

        if (resPath.contains("/")) {
            dirName = resPath.substring(0, resPath.lastIndexOf('/'));
            fileName = resPath.substring(resPath.lastIndexOf('/') + 1);
        }

        String resName = fileName.substring(0, fileName.lastIndexOf('.'));
        int resId = res.getIdentifier(resName, dirName, pkgName);

        if (resId == 0) {
            resId = res.getIdentifier(resName, "mipmap", pkgName);
        }

        if (resId == 0) {
            resId = res.getIdentifier(resName, "drawable", pkgName);
        }

        return resId;
    }

}
