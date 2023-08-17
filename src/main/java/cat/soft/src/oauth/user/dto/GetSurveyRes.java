package cat.soft.src.oauth.user.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
