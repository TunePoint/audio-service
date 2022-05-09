package ua.tunepoint.audio.model.response.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String pseudonym;
    private String firstName;
    private String lastName;
    private Resource avatar;
}
