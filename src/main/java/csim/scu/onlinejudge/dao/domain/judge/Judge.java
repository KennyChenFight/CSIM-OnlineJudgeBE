package csim.scu.onlinejudge.dao.domain.judge;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import csim.scu.onlinejudge.dao.domain.problem.Problem;
import csim.scu.onlinejudge.dao.domain.student.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;


@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "json", typeClass = JsonStringType.class)})
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Judge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long judgeId;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "problem_id")
    private Problem problem;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;
    private double rate;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<HistoryCode> historyCodes;

    public Judge(Problem problem, Student student, double rate, List<HistoryCode> historyCodes) {
        this.problem = problem;
        this.student = student;
        this.rate = rate;
        this.historyCodes = historyCodes;
    }
}
