package goorm.reinput.reminder.service;

import goorm.reinput.folder.domain.Folder;
import goorm.reinput.insight.domain.Insight;
import goorm.reinput.reminder.domain.RemindType;
import goorm.reinput.reminder.domain.Reminder;
import goorm.reinput.reminder.domain.ReminderDate;
import goorm.reinput.reminder.domain.dto.req.ReminderAnswerReqDto;
import goorm.reinput.reminder.domain.dto.req.ReminderCalenderReqDto;
import goorm.reinput.reminder.domain.dto.res.ReminderAnswerResDto;
import goorm.reinput.reminder.domain.dto.res.ReminderCalenderResDto;
import goorm.reinput.reminder.domain.dto.res.ReminderQuestionResponseDto;
import goorm.reinput.reminder.repository.ReminderQuestionRepository;
import goorm.reinput.reminder.repository.ReminderRepository;
import goorm.reinput.user.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static goorm.reinput.reminder.domain.QReminder.reminder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(ReminderService.class) // ReminderService를 컨텍스트에 등록
public class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderQuestionRepository reminderQuestionRepository;

    @Autowired
    EntityManager em;

    private Long userId = 1L;


    @BeforeEach
    void setUp() {
        // 테스트 데이터 세팅
        User user =User.builder()
                .userEmail("userEmail")
                .userName("userName")
                .userPassword("userPassword")
                .build();
        Folder folder = Folder.builder()
                .folderName("folderName")
                .user(user)
                .build();
        Insight insight = Insight.builder()
                .folder(folder)
                .insightTitle("insightTitle")
                .insightUrl("insightUrl")
                .insightMemo("insightMemo")
                .insightSummary("insightSummary")
                .viewCount(0)
                .insightMainImage("insightMainImage")
                .build();
        Reminder reminder = Reminder.builder()
                .insight(insight)
                .isEnable(true)
                .lastRemindedAt(null)
                .build();

        List<Integer> days = List.of(1, 2, 3, 4, 5, 6, 7);
        ReminderDate reminderDate = ReminderDate.builder()
                .reminder(reminder)
                .remindType(RemindType.WEEK)
                .remindDays(days)
                .build();
        // reminder 필드 세팅...
        LocalDate today = LocalDate.now();
        int todayMonthDay = today.getDayOfMonth();

        Insight insight1 = Insight.builder()
                .folder(folder)
                .insightTitle("insightTitle")
                .insightUrl("insightUrl")
                .insightMemo("insightMemo")
                .insightSummary("insightSummary")
                .viewCount(0)
                .insightMainImage("insightMainImage")
                .build();
        Reminder reminderMonthly = Reminder.builder()
                .insight(insight1)
                .isEnable(true)
                .lastRemindedAt(null)
                .build();
        ReminderDate reminderDateMonthly = ReminderDate.builder()
                .reminder(reminderMonthly)
                .remindType(RemindType.MONTH)
                .remindDays(List.of(todayMonthDay)) // 현재 날짜에 맞춰 리마인드 설정
                .build();



        em.persist(user);
        em.persist(folder);
        em.persist(insight);
        em.persist(reminder);
        em.persist(reminderDate);
        em.persist(insight1);
        em.persist(reminderMonthly);
        em.persist(reminderDateMonthly);
        em.flush();
        em.clear();
    }

    @Test
    void getReminderCalender() {
        // given
        ReminderCalenderReqDto reqDto = ReminderCalenderReqDto.builder()
                .requestDate(LocalDate.now())
                .build();
        // when
        ReminderCalenderResDto resDto = reminderService.getReminderCalender(userId, reqDto);
        // then
        assertThat(resDto.getRemindTotal()).isEqualTo(1);
        assertThat(resDto.getRemindInsightList().get(0).getInsightTitle()).isEqualTo("insightTitle");
    }
    @Test
    void remindTypeMonthOnSpecificDay() {
        // given
        LocalDate today = LocalDate.now();

        // 월별 리마인드 타입 및 현재 날짜에 맞춰 새 리마인더 추가


        // when
        ReminderCalenderResDto resDto = reminderService.getReminderCalender(userId, ReminderCalenderReqDto.builder()
                .requestDate(today)
                .build());

        // then
        assertThat(resDto.getRemindTotal()).isGreaterThanOrEqualTo(1);
        assertThat(resDto.getRemindInsightList().get(0).getInsightTitle()).isEqualTo("insightTitle");
    }
}