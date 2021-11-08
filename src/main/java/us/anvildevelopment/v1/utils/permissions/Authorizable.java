package us.anvildevelopment.v1.utils.permissions;

/**
 * All methods default to false and should return false in case of an error, it would be best to structure supporting code with this in mind.
 */
public interface Authorizable {
    /**
     * Used for managing access.
     * @param accessor String unique to accessor
     * @return boolean representing access to resource
     */
    boolean allowed(String accessor, String action);

    boolean addEntry(String accessor, String id, Entry entry);

    boolean editPermission(String accessor, String id, Entry entry);

    /**
     * Used to determine if an administrative action to an entry can be executed by the accessor
     * Please note that if the accessor's weight in the entry being compared is zero (default) they should never be able to execute an administrative action
     * @param accessor Accessor ID
     * @param id Target ID
     * @param entry Entry
     * @return
     */
    boolean allowedRelational(String accessor, String id, Entry entry);
}
