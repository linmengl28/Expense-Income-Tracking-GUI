package finalprojects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MemberManagerTest {

    private MemberManager memberManager;

    @BeforeEach
    public void setUp() {
        memberManager = new MemberManager();
    }

    @Test
    public void testAddMemberToList() {
        Member member1 = new Member("John Doe");
        Member member2 = new Member("Jane Doe");
        Member member3 = null;

        // Add two distinct members
        memberManager.addMemberToList(member1);
        memberManager.addMemberToList(member2);
        memberManager.addMemberToList(member3);

        List<Member> members = memberManager.getMembers();

        // Check if both members are added
        assertEquals(2, members.size());
        assertTrue(members.contains(member1));
        assertTrue(members.contains(member2));
        assertFalse(members.contains(member3));

        // Adding the same member again should not change the list
        memberManager.addMemberToList(member1);
        assertEquals(2, members.size());
    }

    @Test
    public void testFindMemberByName() {
        Member member1 = new Member("John Doe");
        Member member2 = new Member("Jane Doe");
        Member member3 = new Member("John Smith");

        // Add members to the manager
        memberManager.addMemberToList(member1);
        memberManager.addMemberToList(member2);

        // Find existing member by name
        Member foundMember = memberManager.findMemberByName("John Doe");
        assertNotNull(foundMember);
        assertEquals(member1, foundMember);

        // Try to find a non-existing member
        foundMember = memberManager.findMemberByName("John Smith");
        assertNull(foundMember);
    }
}
