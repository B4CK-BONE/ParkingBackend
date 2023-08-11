package cat.soft.oauth.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String email;
    private String provider;
    private String provider_id;
    private String password;
}
