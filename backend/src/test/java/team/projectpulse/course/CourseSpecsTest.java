package team.projectpulse.course;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * The course search lowercases the search text in Java and the stored value in the database. The Java side must
 * not depend on the server's default locale: in Turkish, "I" lowercases to a dotless "ı", which the database's
 * "i" never matches.
 */
@ExtendWith(MockitoExtension.class)
class CourseSpecsTest {

    @Mock
    Root<Course> root;
    @Mock
    CriteriaQuery<?> query;
    @Mock
    CriteriaBuilder criteriaBuilder;

    private Locale originalLocale;

    @BeforeEach
    void useTurkishServerLocale() {
        this.originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.forLanguageTag("tr-TR"));
    }

    @AfterEach
    void restoreLocale() {
        Locale.setDefault(this.originalLocale);
    }

    @Test
    void containsCourseNameLowercasesTheSearchTextIndependentOfServerLocale() {
        CourseSpecs.containsCourseName("SENIOR DESIGN").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).like(any(), eq("%senior design%"));
    }

    @Test
    void containsCourseDescriptionLowercasesTheSearchTextIndependentOfServerLocale() {
        CourseSpecs.containsCourseDescription("CAPSTONE I").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).like(any(), eq("%capstone i%"));
    }
}
