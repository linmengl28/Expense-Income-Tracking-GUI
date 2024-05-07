package finalprojects;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of members within the system. This class is responsible for
 * adding, retrieving, and searching for members based on various criteria.
 * It ensures that only distinct members are added to the list, avoiding duplicates.
 */
public class MemberManager {
    private List<Member> members;

    /**
     * Constructs a new MemberManager. Initializes the internal list that will hold
     * the members.
     */
    public MemberManager() {
        members = new ArrayList<>();
    }
    
    /**
     * only distinct member would be added to list
     * @param member
     */
    public void addMemberToList(Member member) {
        if (member != null && !members.contains(member)) {
            members.add(member);
        }
    }

    /**
     * Retrieves the list of all members currently managed by this manager.
     *
     * @return A list of {@link Member} objects.
     */
    public List<Member> getMembers() {
        return members;
    }

    /**
     * Finds a member by their name, ignoring case considerations.
     * This search is case-insensitive, meaning "JOHN DOE" and "john doe" will be treated as equal.
     *
     * @param name The name of the member to find.
     * @return The {@link Member} object if found, or null if no member with the given name exists.
     */
    public Member findMemberByName(String name) {
        for (Member member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return member;
            }
        }
        return null;  
    }
}

