package team.projectpulse.instructor;

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
 * The instructor search lowercases the search text in Java and the stored value in the database, then compares
 * the two. The Java side must not depend on the server's default locale: in Turkish, "I" lowercases to a dotless
 * "ı", which the database's "ivan" never equals.
 */
@ExtendWith(MockitoExtension.class)
class InstructorSpecsTest {

    @Mock
    Root<Instructor> root;
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
    void hasFirstNameLowercasesTheSearchTextIndependentOfServerLocale() {
        InstructorSpecs.hasFirstName("IVAN").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).equal(any(), eq("ivan"));
    }

    @Test
    void hasLastNameLowercasesTheSearchTextIndependentOfServerLocale() {
        InstructorSpecs.hasLastName("IVANOV").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).equal(any(), eq("ivanov"));
    }

    @Test
    void hasEmailLowercasesTheSearchTextIndependentOfServerLocale() {
        InstructorSpecs.hasEmail("I.IVANOV@TCU.EDU").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).equal(any(), eq("i.ivanov@tcu.edu"));
    }
}
