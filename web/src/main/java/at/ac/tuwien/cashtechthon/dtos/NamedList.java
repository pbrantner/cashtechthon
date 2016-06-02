package at.ac.tuwien.cashtechthon.dtos;

import java.util.List;

/**
 * Jackson friendly list
 * Created by j on 02.06.16.
 */
public final class NamedList<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public NamedList setList(List<T> list) {
        this.list = list;
        return this;
    }
}
