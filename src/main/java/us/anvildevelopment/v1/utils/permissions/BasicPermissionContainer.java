package us.anvildevelopment.v1.utils.permissions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A flexible permission container designed to be embedded in many server applications
 */
public class BasicPermissionContainer implements Authorizable, Serializable {
    public Map<String, Map<String, Entry>> permissionSet = new HashMap<>();
    @Override
    public boolean allowed(String s, String action) {
        try {
            if (permissionSet.containsKey(s)) {
                HashMap<String, Entry> iPS = (HashMap<String, Entry>) permissionSet.get(s);
                if (iPS.containsKey(action)) {
                    return iPS.get(action).getAllow();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean addEntry(String accessor, String id, Entry p) {
        String entry = p.getName();
        if (doesEntryExist(id, entry)) return false;
        if (accessor.equals(id)) {
            if (allowed(accessor, Actions.ADD_ENTRY.name())) {
                if (getEntryWeight(accessor, Actions.ADD_ENTRY.name()) <= p.getWeight()) {
                    return false;
                } else {
                    addEntry(id, p);
                    return true;
                }
            }
        } else {
            if (allowedRelational(accessor, id, p)) {
                addEntry(id, p);
                return true;
            }
        }
        return false;
    }

    /**
     * Method for easily checking entry weights
     * @param id Unique entry target
     * @param entryType Entry type as String
     * @return Entry weight, 0 if the entry does not exist
     */
    public Integer getEntryWeight(String id, String entryType) {
        if (permissionSet.containsKey(id)) {
            Map<String, Entry> iPS = permissionSet.get(id);
            if (iPS.containsKey(entryType)) {
                return iPS.get(entryType).getWeight();
            }
        }
        return 0;
    }
    private void addEntry(String id, Entry e) {
        if (permissionSet.containsKey(id)) {
            HashMap<String, Entry> iPS = (HashMap<String, Entry>) permissionSet.get(id);
            iPS.putIfAbsent(id, e);
        }
    }


    public boolean doesEntryExist(String id, String entry) {
        if (!permissionSet.containsKey(id)) return false;
        Map<String, Entry> iPS = permissionSet.get(id);
        return iPS.containsKey(entry);
    }

    @Override
    public boolean editPermission(String accessor, String id, Entry newEntry) {
        if (allowed(accessor, Actions.EDIT_ENTRY.name()) && doesEntryExist(id, newEntry.getName()) && allowedRelational(accessor, id, newEntry)) {
            Map<String, Entry> iPS = permissionSet.get(id);
            iPS.replace(newEntry.getName(), newEntry);
            return iPS.get(newEntry.getName()) == newEntry;
        }
        return false;
    }

    @Override
    public boolean allowedRelational(String accessor, String id, Entry entry) {
        try {
            String action = entry.getName();
            Integer w1 = 0;
            Integer w2 = 0;
            if (permissionSet.containsKey(accessor)) {
                Map<String, Entry> plist = permissionSet.get(accessor);
                if (plist.containsKey(action)) {
                    w1 = plist.get(action).getWeight();
                }
            }
            if (permissionSet.containsKey(id)) {
                Map<String, Entry> plist = permissionSet.get(id);
                if (plist.containsKey(action)) {
                    w2 = plist.get(action).getWeight();
                }
            }
            return (w1 > w2) && (entry.getWeight() < w1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
