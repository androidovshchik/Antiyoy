package io.androidovshchik.antiyoy.stuff.object_pool;

import java.util.ArrayList;
import java.util.ListIterator;
import yio.tro.antiyoy.stuff.Yio;

public abstract class ObjectPoolYio<ObjectType extends ReusableYio> {
    private ArrayList<ObjectType> freeObjects = new ArrayList();

    public abstract ObjectType makeNewObject();

    public void add(ObjectType object) {
        Yio.addByIterator(this.freeObjects, object);
    }

    public void addWithCheck(ObjectType object) {
        if (!contains(object)) {
            add(object);
        }
    }

    public boolean contains(ObjectType object) {
        return this.freeObjects.contains(object);
    }

    public void clear() {
        this.freeObjects.clear();
    }

    public ObjectType getNext() {
        if (this.freeObjects.size() > 0) {
            ListIterator<ObjectType> iterator = this.freeObjects.listIterator();
            ReusableYio next = (ReusableYio) iterator.next();
            iterator.remove();
            next.reset();
            return next;
        }
        ObjectType object = makeNewObject();
        object.reset();
        return object;
    }

    public void showInConsole() {
        System.out.println();
        if (this.freeObjects.size() == 0) {
            System.out.println("Empty pool");
            return;
        }
        System.out.println("Pool(" + this.freeObjects.size() + "): " + ((ReusableYio) this.freeObjects.get(0)).getClass().getSimpleName());
    }

    public boolean hasDuplicates() {
        for (int i = 0; i < this.freeObjects.size(); i++) {
            for (int j = i + 1; j < this.freeObjects.size(); j++) {
                if (this.freeObjects.get(i) == this.freeObjects.get(j)) {
                    System.out.println("Found duplicate in pool");
                    return true;
                }
            }
        }
        return false;
    }
}
