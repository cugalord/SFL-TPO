package Data;

import java.time.LocalDateTime;
import java.util.Vector;

/**
 * Data representation of job information.
 */
public class DataJob extends Data {
    /** The job ID. */
    public final int jobID;
    /** The time and date when job was created. */
    public final LocalDateTime created;
    /** The time and date when job was completed. */
    public final LocalDateTime completed;
    /** The job type ID. */
    public final int jobTypeID;
    /** The job status ID. */
    public final int jobStatusID;
    /** The username of staff to whom the job is assigned. */
    public final String username;
    /** The list of parcel IDs that belong to this job. */
    public final Vector<String> parcelIDs;

    /**
     * Constructs a new DataJob object.
     * @param index int - The sequential record number.
     * @param jobID int - The job ID.
     * @param created LocalDateTime - The time and date when job was created.
     * @param completed LocalDateTime - The time and date when job was completed.
     * @param jobTypeID int - The job type ID.
     * @param jobStatusID int - The job status ID.
     * @param username String - The username of staff to whom the job is assigned.
     */
    public DataJob(int index, int jobID, LocalDateTime created, LocalDateTime completed, int jobTypeID, int jobStatusID, String username) {
        super(index);
        this.jobID = jobID;
        this.created = created;
        this.completed = completed;
        this.jobTypeID = jobTypeID;
        this.jobStatusID = jobStatusID;
        this.username = username;
        this.parcelIDs = new Vector<>();
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataJob{" +
                "recordNumber=" + recordNumber +
                ", jobID=" + jobID +
                ", created=" + created +
                ", completed=" + completed +
                ", jobTypeID=" + jobTypeID +
                ", jobStatusID=" + jobStatusID +
                ", username='" + username + '\'' +
                ", parcelIDs=" + parcelIDs +
                '}';
    }
}
