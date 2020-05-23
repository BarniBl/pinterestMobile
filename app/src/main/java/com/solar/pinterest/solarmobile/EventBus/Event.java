package com.solar.pinterest.solarmobile.EventBus;

public class Event implements Comparable{
    private String name;

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        return name.equals(other.getName());
    }

    @Override
    public int compareTo(Object o) throws IllegalArgumentException {
        if (getClass() != o.getClass()) {
            throw new IllegalArgumentException();
        }
        Event other = (Event) o;
        return name.compareTo(other.getName());
    }
}

