package org.ift2905.musicbrainz.service.bookmarks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;

import java.util.ArrayList;
import java.util.List;

public class BookmarksService extends SQLiteOpenHelper {

    public BookmarksService(Context context) {
        super(context, "bookmarks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table bookmarks (id text, name text, comment text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isBookmarked(Artist artist) {
        Cursor c = getReadableDatabase()
                .query("bookmarks", new String[] {"count(*)"}, "id=?", new String[] {artist.id}, null, null, null);
        c.moveToFirst();
        return c.getInt(0) >= 1;
    }

    public void addBookmark(Artist artist) {
        ContentValues v = new ContentValues();
        v.put("id", artist.id);
        v.put("name", artist.name);
        v.put("comment", artist.disambiguation);
        getWritableDatabase().insert("bookmarks", null, v);
    }

    public void removeBookmark(Artist artist) {
        getWritableDatabase().delete("bookmarks", "id=?", new String[] {artist.id});
    }

    public List<Artist> getBookmarks() {
        Cursor c = getReadableDatabase()
                .query("bookmarks", new String[] {"id", "name", "comment"}, null, null, null, null, "name collate nocase asc");
        List<Artist> list = new ArrayList<>();
        while (c.moveToNext()) {
            Artist artist = new Artist();
            artist.id = c.getString(0);
            artist.name = c.getString(1);
            artist.disambiguation = c.getString(2);
            list.add(artist);
        }
        return list;
    }
}
