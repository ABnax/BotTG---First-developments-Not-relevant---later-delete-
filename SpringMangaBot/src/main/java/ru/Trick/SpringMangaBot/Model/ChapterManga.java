package ru.trick.springMangaBot.model;

public class ChapterManga {
    private int id;
    private int chapter_manga;
    private String address;


    public ChapterManga(int chapter_manga, String address) {
        this.chapter_manga = chapter_manga;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChapter_manga() {
        return chapter_manga;
    }

    public void setChapter_manga(int chapter_manga) {
        this.chapter_manga = chapter_manga;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
