package test.vtd.koreanguage.model;

public class movieObject {
    private String bannerUrl;
    private String title;
    private String videoUrl;


    public movieObject() {
    }

    public movieObject(String bannerUrl, String title, String videoUrl) {
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
