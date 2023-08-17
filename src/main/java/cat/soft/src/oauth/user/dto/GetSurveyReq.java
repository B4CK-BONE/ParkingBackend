package cat.soft.src.oauth.user.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetSurveyReq {
    @NotBlank(message = "내용을 입력하세요")
    private String contents;
    @Pattern(regexp="^(data:image/(jpg|jpeg|png);base64,(/9j/|iVBORw)).*",message = "이미지만 업로드 가능 합니다")
    private String img;

}
