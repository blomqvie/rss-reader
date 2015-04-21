package fi.reaktor.android.rx.data;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Feeds {
    private static final String TAG = Feeds.class.getSimpleName();
    private static final String fileName = "Feeds.ser";

    private List<Feed> feeds = new ArrayList<>();

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void add(Feed feed) {
        boolean found = false;
        for (Feed f : feeds) {
            if(f.getGuid().equals(feed.getGuid())) {
                found = true;
                f.update(feed);
                break;
            }
        }
        if(!found) {
            feeds.add(feed);
        }
    }

    public void persist(Context context) {
        try {
            Log.d(TAG, "Writing " + fileName);
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(feeds);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(Context context) {
        feeds = readFromFile(context);
    }

    private static List<Feed> readFromFile(Context context) {
        List<Feed> list = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            list = (List<Feed>) objectInputStream.readObject();
            Log.d(TAG, "Read " + list.size() + " feeds from " + fileName);
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            // this is normal on first run
            Log.d(TAG, "No prior persisted feed data in " + fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error reading feeds from " + fileName, e);
        } finally {
            return list;
        }
    }

    public Article findArticle(String guid) {
        for (Feed feed : feeds) {
            Article article = feed.findArticle(guid);
            if (article != null) {
                return article;
            }
        }
        return null;
    }
}
