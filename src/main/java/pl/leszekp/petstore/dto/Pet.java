package pl.leszekp.petstore.dto;

import java.util.List;

public class Pet {
    public enum Status {
        AVAILABLE, PENDING, SOLD
    }

    private Long id;
    private String name;
    private List<String> photoUrls;
    private List<String> tag;
    private Status status;

    public Pet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(final List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(final List<String> tag) {
        this.tag = tag;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }
}
