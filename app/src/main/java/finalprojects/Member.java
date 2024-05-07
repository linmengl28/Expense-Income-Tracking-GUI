package finalprojects;


public class Member {
    private String fullName;

    /**
     * Constructs a new Member with the specified full name.
     *
     * @param fullName The full name of the member, cannot be null.
     */
    public Member(String fullName) {
        this.fullName = fullName;
    }


    /**
     * Returns the full name of the member.
     *
     * @return The full name of the member.
     */
    public String getName() {
        return fullName;
    }

    /**
     * Sets the full name of the member.
     *
     * @param fullName The new full name of the member, cannot be null.
     */
    public void setName(String fullName) {
        this.fullName= fullName;
    }

    /**
     * Returns a string representation of the member, which is the full name of the member.
     *
     * @return A string representation of the member.
     */
    @Override
    public String toString() {
        return this.fullName;
    }

    /**
     * Compares this member to the specified object. The result is {@code true} if and only if
     * the argument is not {@code null} and is a {@code Member} object that has the same full name
     * as this object.
     *
     * @param o The object to compare this {@code Member} against.
     * @return {@code true} if the given object represents a {@code Member} equivalent to this member, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return fullName.equals(member.fullName);
    }



}

