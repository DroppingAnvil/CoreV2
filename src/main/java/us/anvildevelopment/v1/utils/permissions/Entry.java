package us.anvildevelopment.v1.utils.permissions;

public interface Entry {
    String getName();
    boolean getAllow();
    void setAllow(boolean b);
    Integer getWeight();
}
