package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A person registered to borrow books from the library.
 */
@DatabaseTable(tableName = "members")
public final class Member {

    /** Database-generated member identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Member's display name. */
    @DatabaseField(canBeNull = false)
    private String name;

    /** Member's email address. */
    @DatabaseField(canBeNull = false)
    private String email;

    /** Creates an empty member for ORMLite. */
    public Member() {
    }

    /**
     * Creates a member with a name and email address.
     *
     * @param name member's display name.
     * @param email member's email address.
     */
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the database identifier.
     *
     * @return member identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the database identifier.
     *
     * @param id member identifier.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the member's display name.
     *
     * @return member name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the member's display name.
     *
     * @param name member name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the member's email address.
     *
     * @return email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the member's email address.
     *
     * @param email email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
