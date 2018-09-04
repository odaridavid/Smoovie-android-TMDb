package smoovie.apps.com.kayatech.smoovie.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel(Parcel.Serialization.BEAN)
public class MovieVideos {

    @SerializedName("id")
    private String idTrailer;

    @SerializedName("key")
    private String keyTrailer;

    @SerializedName("name")
    private String nameTrailer;

    @SerializedName("site")
    private String siteTrailer;

    @SerializedName("size")
    private String sizeTrailer;

    @SerializedName("type")
    private String typeTrailer;

     MovieVideos() {
    }
    public String getSizeTrailer() {
        return sizeTrailer;
    }

    public String getTypeTrailer() {
        return typeTrailer;
    }

    public String getIdTrailer() {
        return idTrailer;
    }

    public String getKeyTrailer() {
        return keyTrailer;
    }

    public String getNameTrailer() {
        return nameTrailer;
    }

    public String getSiteTrailer() {
        return siteTrailer;
    }
}
