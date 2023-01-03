package skipList;

import java.lang.reflect.Array;
import java.util.*;

public class SkipListSet<T extends Comparable<T>> implements SortedSet<T>, Set<T>, Iterable<T>, Collection<T> {
    SkipListSetItem<T> header;
    SkipListSetItem<T> sentinel;
    Random randomizer = new Random();
    int size;
    int height;
    double itemCount;

    // Create a skip list
    public SkipListSet() {
        SkipListSetItem<T> head = new SkipListSetItem<>();
        SkipListSetItem<T> senti = new SkipListSetItem<>();
        head.up = null;
        head.right = null;
        head.left = null;
        head.down = null;
        senti.up = null;
        senti.right = null;
        senti.left = null;
        senti.down = null;

        header = head;
        sentinel = senti;
        header.right = sentinel;
        sentinel.left = header;
        height = 1;
        size = 0;
        itemCount = 0;
    }

    // Create a skip list from a collection
    public SkipListSet(Collection<T> a) {
        SkipListSetItem<T> head = new SkipListSetItem<>();
        SkipListSetItem<T> senti = new SkipListSetItem<>();
        head.up = null;
        head.right = null;
        head.left = null;
        head.down = null;
        senti.up = null;
        senti.right = null;
        senti.left = null;
        senti.down = null;

        header = head;
        sentinel = senti;
        header.right = sentinel;
        sentinel.left = header;
        height = 1;
        size = 0;
        itemCount = 0;

        addAll(a);
    }

    public Comparator<? super T> comparator() {
        return null;
    }

    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException();
    }


    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException();
    }

    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException();
    }

    // Finds the first element within the skip list
    public T first() {
        SkipListSetItem<T> temp;
        temp = header;

        while (temp != null) {
            if (temp.down == null)
                break;

            temp = temp.down;
        }

        if (temp == null) throw new AssertionError();
        temp = temp.right;

        return temp.data;
    }

    // Find the last element in the skip list
    public T last() {
        SkipListSetItem<T> temp;
        temp = sentinel;

        while (temp != null) {
            if (temp.down == null)
                break;

            temp = temp.down;
        }

        if (temp == null) throw new AssertionError();
        temp = temp.left;

        return temp.data;
    }

    // Custom Iterator to go through skip list
    public class SkipListSetIterator implements Iterator<T> {

        SkipListSetItem<T> current = null;

        // Finds out if the data structure has another element
        public boolean hasNext() {
            if (current == null) {
                current = header;

                while (current != null) {
                    if (current.down == null)
                        break;

                    current = current.down;
                }

            }

            return Objects.requireNonNull(current).right.data != null;
        }

        // Gives the value of the next element
        public T next() {


            if (current == null) {
                current = header;

                while (current != null) {
                    if (current.down == null)
                        break;

                    current = current.down;
                }

            }

            if (Objects.requireNonNull(current).right.data != null) {
                current = current.right;
                return current.data;
            } else
                throw new NoSuchElementException();
        }

        // Default remove application
        public void remove() {

        }
    }

    // Skip list data structure item
    public static class SkipListSetItem<T extends Comparable<T>> {
        T data;
        SkipListSetItem<T> up;
        SkipListSetItem<T> right;
        SkipListSetItem<T> left;
        SkipListSetItem<T> down;

    }

    // Function to rebalance all elements in skip list
    public void reBalance() {
        Iterator<T> itr = this.iterator();
        ArrayList<T> temp = new ArrayList<>();

        while (itr.hasNext())
            temp.add(itr.next());

        header.down = null;
        header.right = sentinel;
        sentinel.left = header;
        sentinel.down = null;
        height = 1;
        size = 0;
        itemCount = 0;
        addAll(temp);
    }

    // Traverse to function for skip list
    public SkipListSetItem<T> traverseTo(T data) {
        SkipListSetItem<T> temp;
        temp = header;

        while (temp != null) {
            if (temp.data == data && temp.down != null) {
                temp = temp.down;
            } else if (temp.data == data) {
                return temp;
            } else if (temp.right.data == null && temp.down != null) {
                temp = temp.down;
            } else if (temp.right.data != null && data.compareTo(temp.right.data) > 0) {
                temp = temp.right;
            } else if (temp.right.data != null && temp.down != null && data.compareTo(temp.right.data) < 0) {
                temp = temp.down;
            } else if (temp.right.data != null && data.compareTo(temp.right.data) == 0) {
                temp = temp.right;
            } else {
                return temp;
            }
        }

        return null;
    }

    // Returns the size of skip list
    public int size() {
        return size;
    }

    // Returns true if empty and false otherwise
    public boolean isEmpty() {
        SkipListSetItem<T> temp;
        temp = header;

        while (temp != null) {
            if (temp.down == null)
                break;

            temp = temp.down;
        }

        return Objects.requireNonNull(temp).right == null;
    }

    // Checks skip list if it contains a certain element
    @SuppressWarnings("unchecked")
    public boolean contains(Object data) {
        SkipListSetItem<T> temp;

        temp = traverseTo((T) data);
        if (temp.data == null)
            return false;
        else
            return temp.data.equals(data);
    }

    public Iterator<T> iterator() {
        return new SkipListSetIterator();
    }

    public Object[] toArray() {
        Object[] objArr = new Object[size];
        Iterator<T> itr = this.iterator();
        int count = 0;

        while (itr.hasNext())
            objArr[count++] = itr.next();

        return objArr;
    }

    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size) {
            a = (T1[]) Array.newInstance(a.getClass().getComponentType(), size);
        } else if (a.length > size) {
            a[size] = null;
        }

        Iterator<T> itr = this.iterator();
        int count = 0;

        while (itr.hasNext())
            a[count++] = (T1) itr.next();

        return a;
    }

    // Adds element to array and then continues on to increaseHeight function
    public boolean add(T data) {
        SkipListSetItem<T> traversedTemp;
        SkipListSetItem<T> addedItem = new SkipListSetItem<>();

        addedItem.data = data;

        traversedTemp = traverseTo(data);
        if (traversedTemp.data != null && traversedTemp.data.equals(data)) {
            return false;
        } else {
            addedItem.left = traversedTemp;
            traversedTemp.right.left = addedItem;
            addedItem.right = traversedTemp.right;
            traversedTemp.right = addedItem;
            size++;
            itemCount++;
            increaseHeight(addedItem);
            return true;
        }
    }

    // Increases the height of each node by 50%, then 25%, then etc and increase height of header if all items exceeds 2^height)
    public void increaseHeight(SkipListSetItem<T> addedItem) {
        SkipListSetItem<T> tempStart;
        SkipListSetItem<T> tempUp;
        SkipListSetItem<T> temp;
        int tempHeight = 1,
                countHeight = 1;

        tempStart = addedItem;
        while (randomizer.nextDouble() < .50) {
            SkipListSetItem<T> randomHeightItem = new SkipListSetItem<>();

            if (Math.pow(2, height) <= itemCount) {
                SkipListSetItem<T> head = new SkipListSetItem<>();
                SkipListSetItem<T> senti = new SkipListSetItem<>();

                head.down = header;
                head.right = senti;
                header.up = head;
                senti.left = head;
                senti.down = sentinel;
                sentinel.up = senti;
                header = head;
                sentinel = senti;
                height++;
            }

            if (tempHeight < height) {
                addedItem.up = randomHeightItem;
                randomHeightItem.down = addedItem;
                randomHeightItem.data = addedItem.data;
                itemCount++;
                tempHeight++;

                for (temp = tempStart.right; randomHeightItem.right == null; temp = Objects.requireNonNull(temp).right) {
                    for (tempUp = temp; tempUp != null; tempUp = tempUp.up) {
                        if (countHeight == tempHeight) {
                            randomHeightItem.right = tempUp;
                            randomHeightItem.left = tempUp.left;
                            tempUp.left.right = randomHeightItem;
                            tempUp.left = randomHeightItem;
                            break;
                        }
                        countHeight++;
                    }
                    countHeight = 1;
                }

                addedItem = randomHeightItem;
            }
        }
    }

    // Removes a specified element from the skip list
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        SkipListSetItem<T> traversedTemp;
        SkipListSetItem<T> temp = header;
        int count = 0;
        traversedTemp = traverseTo((T) o);

        if (traversedTemp.data != null && traversedTemp.data.equals(o)) {
            while (traversedTemp != null) {
                traversedTemp.left.right = traversedTemp.right;
                traversedTemp.right.left = traversedTemp.left;
                traversedTemp.right = null;
                traversedTemp.left = null;
                if (traversedTemp.down != null)
                    traversedTemp.down.up = null;
                traversedTemp.down = null;
                traversedTemp.data = null;
                traversedTemp = traversedTemp.up;
                count++;
            }

            itemCount -= count;
            if (Math.pow(2, height) >= itemCount && height > 1) {
                header = header.down;
                while (temp != sentinel) {
                    if (temp.down != null)
                        temp.down.up = null;

                    temp.down = null;
                    temp = temp.right;
                    temp.left = null;
                }
                sentinel = sentinel.down;
                sentinel.up = null;
                height--;
            }

            size--;
            return true;
        } else
            return false;
    }

    // Checks the skip list if it contains all that is within the skip list
    public boolean containsAll(Collection<?> c) {
        int count = 0;

        for (Object o : c) {
            if (contains(o)) {
                count++;
            }
        }

        return count == c.size();
    }

    // Adds the whole collection to the skip list at once
    public boolean addAll(Collection<? extends T> c) {

        for (T n : c)
            add(n);

        return size == c.size();
    }

    // Checks if the whole collection is within the skip list if one element isn't in there it returns false
    public boolean retainAll(Collection<?> c) {
        SkipListSetItem<T> temp = header;
        int flag = 0;

        ArrayList<Object> holdsCollection = new ArrayList<>(c);

        while (temp != null) {
            if (temp.down == null)
                break;

            temp = temp.down;
        }

        while (Objects.requireNonNull(temp).right != null) {
            if (!holdsCollection.contains(temp.right.data) && temp.right.data != null) {
                flag = 1;
                remove(temp.right.data);
            }

            temp = temp.right;
        }

        return flag == 1;
    }

    // Removes all the elements from the skip list that are within the collection
    public boolean removeAll(Collection<?> c) {
        int flag = 0;

        for (Object o : c) {
            remove(o);
            flag = 1;
        }

        return flag == 1;
    }

    // Deletes the whole skip list
    public void clear() {
        header = null;
        sentinel = null;
        size = 0;
        height = 0;
        itemCount = 0;
    }

//     Prints out the skip list to validate what I'm doing
//    public void printSkipList() {
//        SkipListSetItem<T> temp;
//        SkipListSetItem<T> tempUp;
//        temp = header;
//
//        while (temp != null) {
//            if (temp.data == null && temp.down == null)
//                System.out.print("| " + null + " |");
//            else if (temp.data == null)
//                System.out.print("| " + null + " ");
//
//            if (temp.down == null) {
//                System.out.println();
//                break;
//            }
//
//            temp = temp.down;
//        }
//
//        for (temp = Objects.requireNonNull(temp).right; temp != null; temp = temp.right) {
//            for (tempUp = temp; tempUp != null; tempUp = tempUp.up) {
//                if (tempUp.data == null && tempUp.up == null)
//                    System.out.print("| " + temp.data + " |");
//                else if (tempUp.data == null)
//                    System.out.print("| " + temp.data + " ");
//                else if (tempUp.down == null && tempUp.up == null)
//                    System.out.print("|  " + temp.data + "  |");
//                else if (tempUp.down == null)
//                    System.out.print("|  " + temp.data);
//                else if (tempUp.up == null)
//                    System.out.print("  |  " + temp.data + "  |");
//                else
//                    System.out.print("  |  " + temp.data);
//            }
//            System.out.println();
//        }
//    }
}