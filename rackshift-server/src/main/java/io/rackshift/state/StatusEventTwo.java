package io.rackshift.state;

public class StatusEventTwo {

    private LifeStatus status;
    private LifeEvent event;

    public StatusEventTwo(LifeStatus status, LifeEvent event) {
        this.status = status;
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusEventTwo that = (StatusEventTwo) o;
        return that.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return status.hashCode() + event.hashCode();
    }
}
