package ua.tunepoint.audio.model.response.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Resource avatar;
}
