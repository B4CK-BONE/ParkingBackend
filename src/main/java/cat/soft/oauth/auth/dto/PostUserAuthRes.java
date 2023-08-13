package cat.soft.oauth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserAuthRes {
    private Integer idx;
    private Integer roomIdx;
    private Integer role;
}
