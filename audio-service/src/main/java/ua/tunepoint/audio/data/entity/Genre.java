package ua.tunepoint.audio.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genres")
public class Genre {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private Set<Genre> sub;

    public static Genre create(Long id, Long parentId, String name) {
        var genre = new Genre();
        genre.setId(id);
        genre.setParentId(parentId);
        genre.setName(name);

        return genre;
    }
}
