package Data;

/**
 * Data representation of the result given by COUNT statement.
 */
public class DataCount extends Data {
    /** The COUNT value. */
    public final int value;

    /**
     * Constructs a new DataCount object.
     * @param index int - The sequential record number.
     * @param value int - The COUNT value.
     */
    public DataCount(int index, int value) {
        super(index);
        this.value = value;
    }

    @Override
    public String toString() {
        return "[rN: " + this.recordNumber + ", C: " + this.value + "]";
    }
}
