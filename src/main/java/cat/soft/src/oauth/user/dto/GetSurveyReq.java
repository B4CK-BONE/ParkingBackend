package cat.soft.src.oauth.user.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetSurveyReq {
    @NotNull(message = "내용을 입력하세요")
    private String contents;
    private String img;

}
