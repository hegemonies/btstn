package org.bravo.newscollector.model

import javax.persistence.*

@Entity
@Table(name = "news_grabber_news")
data class News(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    val message: String,

    val source: String,

    @Column(name = "object_id")
    val objectId: Long,

    @Column(name = "created_at")
    val createdAt: Long,

    @OneToMany(mappedBy = "news")
    val newsTags: Set<NewsTags> = emptySet()
) {

    override fun toString(): String {
        return "News(" +
                "id=$id" +
                ", message=$message" +
                ", source=$source," +
                ", objectId=$objectId" +
                ", createdAt=$createdAt" +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (other !is News) return false

        if (this.id != other.id) return false
        if (this.message != other.message) return false
        if (this.source != other.source) return false
        if (this.objectId != other.objectId) return false
        if (this.createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + objectId.hashCode()
        result = 31 * result + createdAt.hashCode()

        return result
    }
}
