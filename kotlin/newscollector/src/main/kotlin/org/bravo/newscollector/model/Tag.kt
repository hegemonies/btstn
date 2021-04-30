package org.bravo.newscollector.model

import javax.persistence.*

@Entity
@Table(name = "news_grabber_tags")
data class Tag(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    val tag: String,

    @OneToMany(mappedBy = "tag")
    val newsTags: Set<NewsTags> = emptySet()
) {

    override fun toString(): String {
        return "Tag(" +
                "id=$id" +
                ", tag=$tag" +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (other !is Tag) return false

        if (id != other.id) return false
        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + tag.hashCode()

        return result
    }
}
