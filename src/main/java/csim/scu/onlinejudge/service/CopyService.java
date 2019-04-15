package csim.scu.onlinejudge.service;

import csim.scu.onlinejudge.dao.domain.copy.Copy;
import csim.scu.onlinejudge.dao.domain.problem.Problem;
import csim.scu.onlinejudge.service.base.BaseService;

import java.util.List;

public interface CopyService extends BaseService<Copy, Long> {

    List<Copy> findByStudentTwoAccount(String account);

    List<Copy> findByProblem(Problem problem);
}
