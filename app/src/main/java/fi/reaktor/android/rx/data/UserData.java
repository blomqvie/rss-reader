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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserData {

    private static final String TAG = "UserData";
    private Set<String> favoriteFeedIds = new HashSet<>();

    private Map<String, Set<String>> readArticlesByFeed = new HashMap<>();
    private final static String favoritesFileName = "FavoriteFeeds.ser";
    private final static String readArticlesFileName = "ReadArticles.ser";

    public UserData(Set<String> favoriteFeedIds, Map<String, Set<String>> readArticlesByFeed) {
        this.favoriteFeedIds = favoriteFeedIds;
        this.readArticlesByFeed = readArticlesByFeed;
    }

    public Set<String> markArticleAsRead(String feedId, String articleId) {

        if(readArticlesByFeed.containsKey(feedId)){
            readArticlesByFeed.get(feedId).add(articleId);
        } else {
            Set<String> read = new HashSet<>();
            read.add(articleId);
            readArticlesByFeed.put(feedId, read);
        }
        return getReadArticles(feedId);
    }

    private Set<String> getReadArticles(String feedId) {
        return readArticlesByFeed.get(feedId);
    }

    public Set<String> markFeedAsFavorite(String feedId) {
        favoriteFeedIds.add(feedId);
        return getFavoriteFeeds();
    }

    private Set<String> getFavoriteFeeds() {
        return favoriteFeedIds;
    }

    public void persist(Context context) {
        writeFile(context, favoritesFileName, favoriteFeedIds);
        writeFile(context, readArticlesFileName, readArticlesByFeed);
    }

    private void writeFile(Context context, String filename, Object object) {
        try {
            Log.d(TAG, "Writing " + filename);
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserData load(Context context) {

        Set<String> favoriteFeedIds = (Set<String>) readFromFile(context, favoritesFileName);
        Map<String, Set<String>> readArticlesByFeed = (HashMap<String, Set<String>>) readFromFile(context, readArticlesFileName);
        return new UserData(favoriteFeedIds, readArticlesByFeed);
    }

    private static Object readFromFile(Context context, String fileName) {
        Object o = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            o = objectInputStream.readObject();
            //Log.d(TAG, "Read " + list.size() + " feeds from " + fileName);
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            // this is normal on first run
            Log.d(TAG, "No prior persisted feed data in " + fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error reading feeds from " + fileName, e);
        } finally {
            return o;
        }
    }}
