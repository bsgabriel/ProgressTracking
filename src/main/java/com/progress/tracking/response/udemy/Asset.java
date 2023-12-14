
package com.progress.tracking.response.udemy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Asset {

    @SerializedName("_class")
    @Expose
    private String _class;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("asset_type")
    @Expose
    private String assetType;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("created")
    @Expose
    private String created;

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
