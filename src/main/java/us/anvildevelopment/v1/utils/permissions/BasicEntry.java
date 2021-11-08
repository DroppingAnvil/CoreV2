package us.anvildevelopment.v1.utils.permissions;

public class BasicEntry implements Entry {
    public String action;
    public boolean allow;
    public Integer weight;

    public BasicEntry(String action, Boolean allow, Integer weight) {
        this.action = action;
        this.allow = allow;
        this.weight = weight;
    }

    @Override
    public String getName() {
        return action;
    }

    @Override
    public boolean getAllow() {
        return allow;
    }

    @Override
    public void setAllow(boolean b) {
        allow = b;
    }

    @Override
    public Integer getWeight() {
        return weight;
    }
}
