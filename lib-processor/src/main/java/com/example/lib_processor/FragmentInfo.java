package com.example.lib_processor;


/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/1/6  15:54
 **/
public class FragmentInfo {
    private String title;
    private String description;
    private int preview;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPreview() {
        return preview;
    }

    public void setPreview(int preview) {
        this.preview = preview;
    }

    public FragmentInfo(){

    }

    public FragmentInfo(String description, int id) {
        this.description = description;
        this.id = id;
    }

    public FragmentInfo(String description, int id, String title, int preview) {
        this.description = description;
        this.id = id;
        this.title = title;
        this.preview = preview;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
