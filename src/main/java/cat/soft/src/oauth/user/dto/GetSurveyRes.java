package cat.soft.src.oauth.user.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetSurveyRes {
    private Integer rownum;
    private String contents;
    private String img;
    private Date date;
}
