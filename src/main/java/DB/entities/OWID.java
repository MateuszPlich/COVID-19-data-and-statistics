package DB.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
//@EqualsAndHashCode(exclude = "country")
//@ToString(exclude = "country")
@Entity
public class OWID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  //  @Column(name = "ISO_CODE", nullable = false)
    @ManyToOne
    @JoinColumn(name="ISO_CODE")
    private Country country;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE", nullable = false)
    private Date date;

    @Column(name = "TOTAL_CASES", nullable = false)
    private Integer total_cases;

    @Column(name = "DAILY_NEW_CASES")
    private Integer new_cases;

    @Column(name = "TOTAL_DEATHS")
    private Integer total_deaths;

    @Column(name = "DAILY_NEW_DEATHS")
    private Integer new_deaths;

    @Column(name = "ICU_PATIENTS")
    private Integer icu_patients;

    @Column(name = "HOSPITALIZED_PATIENTS")
    private Integer hosp_patients;

    @Column(name = "TOTAL_TESTS")
    private Integer total_tests;

    @Column(name = "DAILY_NEW_TESTS")
    private Integer new_tests;
}