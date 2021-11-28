package com.example.lib_processor;


/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/1/6  15:54
 **/
public class FragmentInfo {
    private String description;
    private int id;

    public FragmentInfo(String description, int id) {
        this.description = description;
        this.id = id;
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
