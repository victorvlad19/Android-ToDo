package com.example.vves.workshop12;

public class SideItem {
    private Long iditem;
    private String nameitem;
    private int bage;
    private int imagePath;
    private int category;
    private String folder_group_item;


    public SideItem(Long iditem, String nameitem, int imagePath, int bage, int category, String folder_group_item) {
        this.iditem = iditem;
        this.nameitem = nameitem;
        this.imagePath = imagePath;
        this.bage = bage;
        this.category = category;
        this.folder_group_item = folder_group_item;
    }

    public String getFolder_group_item() {
        return folder_group_item;
    }

    public void setFolder_group_item(String folder_group_item) {
        this.folder_group_item = folder_group_item;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getBage() {
        return bage;
    }

    public void setBage(int bage) {
        this.bage = bage;
    }

    public Long getIditem() {
        return iditem;
    }

    public void setIditem(Long iditem) {
        this.iditem = iditem;
    }

    public String getNameitem() {
        return nameitem;
    }

    public void setNameitem(String nameitem) {
        this.nameitem = nameitem;
    }


    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }
}


