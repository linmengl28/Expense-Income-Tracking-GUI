
package finalprojects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Menglin Lin
 */
class MemberTest {

    @Test
    void getName() {
    }
    @Test
    public void testMemberConstructorAndGetters() {
        String fullName = "John Doe";
        Member member = new Member(fullName);
        assertEquals(fullName, member.getName());
    }

    @Test
    public void testMemberSetters() {
        String fullName = "John Doe";
        Member member = new Member("");
        member.setName(fullName);
        assertEquals(fullName, member.getName());
    }

    @Test
    public void testToString() {
        String fullName = "John Doe";
        Member member = new Member(fullName);
        assertEquals(fullName, member.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        String fullName1 = "John Doe";
        String fullName2 = "Jane Smith";
        Member member1 = new Member(fullName1);
        Member member2 = new Member(fullName1);
        Member member3 = new Member(fullName2);

        // Test equals
        assertTrue(member1.equals(member2));
        assertFalse(member1.equals(member3));
        assertFalse(member1.equals(fullName1));


    }
}
