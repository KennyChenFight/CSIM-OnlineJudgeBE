package csim.scu.onlinejudge.api.functional;

import csim.scu.onlinejudge.api.base.BaseApi;
import csim.scu.onlinejudge.common.exception.CourseNotFoundException;
import csim.scu.onlinejudge.common.exception.JudgeNotFoundException;
import csim.scu.onlinejudge.common.exception.ProblemNotFoundException;
import csim.scu.onlinejudge.common.exception.StudentNotFoundException;
import csim.scu.onlinejudge.common.message.ApiMessageCode;
import csim.scu.onlinejudge.common.message.Message;
import csim.scu.onlinejudge.service.CommonService;
import csim.scu.onlinejudge.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Api(value = "StudentApi", description = "學生的相關Api")
@RequestMapping("/api/student")
@RestController
public class StudentApi extends BaseApi {

    @ApiOperation(value = "更改學生密碼",
            notes = "先用原本的密碼(oriPassword)驗證，驗證成功後，才更換密碼(newPassword)")
    @PostMapping(value = "/changePassword")
    private Message changePassword(@RequestBody Map<String, String> map,
                                   HttpSession session) {
        Message message;

        String account = map.get("account");
        String oriPassword = map.get("oriPassword");
        String newPassword = map.get("newPassword");
        String userType = getUserType(session);
        int code = commonService.updateUserPassword(account, oriPassword, newPassword, userType);
        // code=-1 代表更新失敗
        if (code != -1) {
            message = new Message(ApiMessageCode.SUCCESS_STATUS, "");
        }
        else {
            message = new Message(ApiMessageCode.CHANGE_PASSWORD_ERROR, "");
        }
        return message;
    }

    @ApiOperation(value = "取得學生的全部課程",
            notes = "取得account，來獲得學生的全部課程")
    @GetMapping(value = "/courseList")
    private Message getCourseList(HttpSession session) {
        Message message;

        String account = getUserAccount(session);
        try {
            message = new Message(ApiMessageCode.SUCCESS_STATUS, commonService.getStudentCoursesInfo(account));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
            message = new Message(ApiMessageCode.GET_COURSE_LIST_ERROR, "");
        }
        return message;
    }

    @ApiOperation(value = "取得學生個人資料",
            notes = "存取該學生的個人資料")
    @GetMapping(value = "/info")
    private Message getInfo(String courseId, HttpSession session) {
        String account = getUserAccount(session);
        Message message;
        try {
            message = new Message(ApiMessageCode.SUCCESS_STATUS, commonService.findStudentCourseInfo(Long.parseLong(courseId), account));
        } catch (StudentNotFoundException | JudgeNotFoundException | CourseNotFoundException e) {
            e.printStackTrace();
            message = new Message(ApiMessageCode.GET_INFO_ERROR, "");
        }
        return message;
    }

    @ApiOperation(value = "取得課程下的學生歷史成績及題目資訊",
            notes = "取得courseId，來獲得課程下的學生歷史成績及題目資訊")
    @GetMapping(value = "/historyScore")
    private Message getHistoryScore(String courseId, HttpSession session) {
        Message message;
        return null;
    }

    @ApiOperation(value = "課程下的學生所有題目資料",
            notes = "取得courseId、type、isJudge，來獲得課程下的學生所有題目資料")
    @GetMapping(value = "/problemInfo")
    private Message getProblemInfo(String courseId, String type, boolean isJudge, HttpSession session) {
        Message message;

        String account = getUserAccount(session);
        try {
            message = new Message(ApiMessageCode.SUCCESS_STATUS, commonService.getStudentProblemInfo(Long.parseLong(courseId), type, isJudge, account));
        } catch (CourseNotFoundException | StudentNotFoundException e) {
            e.printStackTrace();
            message = new Message(ApiMessageCode.GET_STUDENT_PROBLEM_INFO_ERROR, "");
        }
        return message;
    }

    @ApiOperation(value = "課程下的學生對題目的難易度評分",
            notes = "取得problemId、rate，來對題目的難易度評分")
    @PostMapping(value = "/updateRate")
    private Message updateRate(@RequestBody Map<String, String> map, HttpSession session) {
        Message message;

        String problemId = map.get("problemId");
        String rate = map.get("rate");
        String account = getUserAccount(session);
        try {
            int code = commonService.updateJudgeRateByProblemIdAndAccount(Double.parseDouble(rate), Long.parseLong(problemId), account);
            if (code != -1) {
                message = new Message(ApiMessageCode.SUCCESS_STATUS, "");
            }
            else {
                message = new Message(ApiMessageCode.UPDATE_STUDENT_PROBLEM_RATE_ERROR, "");
            }
        } catch (ProblemNotFoundException | StudentNotFoundException e) {
            e.printStackTrace();
            message = new Message(ApiMessageCode.UPDATE_STUDENT_PROBLEM_RATE_ERROR, "");
        }
        return message;
    }


}
